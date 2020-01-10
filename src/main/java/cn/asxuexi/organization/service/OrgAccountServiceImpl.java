package cn.asxuexi.organization.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Update.PushOperatorBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.WriteResult;

import cn.asxuexi.message.tool.MessageTool;
import cn.asxuexi.order.dao.OrderDao;
import cn.asxuexi.organization.dao.OrgAccountDao;
import cn.asxuexi.organization.entity.CashFlowEntity;
import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.JsonData;
import cn.asxuexi.tool.RandomTool;

@Service
public class OrgAccountServiceImpl implements OrgAccountService{

	@Resource
	private OrgAccountDao dao;
	@Resource
	private OrderDao orderDao;
	
	@Autowired
	private MongoTemplate mongo;
	@Autowired
	private MessageTool messageTool;
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	private static final double serviceFeeRate=0.006;

	//查询账户信息
	@Override
	public JsonData getAccountInfo() {
		String orgId = GetOrgIdFromRedis.getOrgId();
		JsonData accountInfo = dao.getAccountInfo(orgId);
		return JsonData.success(accountInfo);
	}
	
	//查询所有的资金流信息
	@SuppressWarnings("unchecked")
	@Override
	public JsonData getCashFlowInfo() {
		String orgId = GetOrgIdFromRedis.getOrgId();
		Query query=new Query(Criteria.where("orgId").is(orgId));
				query.with(new Sort(Sort.Direction.DESC, "cashFlows.$.amount"));
//		query.with(new Sort(new Order(Direction.DESC, "_id")));
		Map<String,Object> findOne = mongo.findOne(query, HashMap.class,"cash_flow");
		List<CashFlowEntity> object=new ArrayList<>();
		if (null!=findOne) {
			object = (List<CashFlowEntity>) findOne.get("cashFlows");	
		} 
		return JsonData.success(object);
	}

	//申请提现
	@Override
	@Transactional(rollbackFor=Exception.class)
	public JsonData getCashOut(CashFlowEntity cashOutEntity) throws Exception {
		JsonData json=JsonData.error("");
		//后台验证数据,提现金额不能超额
		String orgId = GetOrgIdFromRedis.getOrgId();
		JsonData accountInfo = dao.getAccountInfo(orgId);
		//计算可提现金额
		double amountUsable=((BigDecimal) accountInfo.get("amount_usable")).doubleValue();
		double amountOfGetOut = cashOutEntity.getAmount();
		if (amountUsable>=amountOfGetOut) {
			/*
			 * 提现操作 1.扣减钱包金额 2.将申请存入sqlserver 3.将日志记录保存到mongo 3.发消息给后台工作人员
			 */
			int updateAmountResult = dao.updateAccountInfo(orgId, -amountOfGetOut, "amount_usable");
			if (updateAmountResult==1) {
				cashOutEntity.setGmt_created(LocalDateTime.now().toString());
				cashOutEntity.setOrgId(orgId);
				cashOutEntity.setId(RandomTool.randomId("cash"));
				List<CashFlowEntity> list = new ArrayList<>();
				list.add(cashOutEntity);
				
				dao.insertCashOutApplication(cashOutEntity);
				
				int n=recordCashFlowLogs(orgId,list);
				if (n==0) {
					throw new Exception("提现申请异常");
				}
				//发送消息
				List<String> addresseeList=new ArrayList<>();
				addresseeList.add("000033");  //todo:获取所有的负责提现申请的员工,发送消息
				messageTool.sendMessageToList("提现申请", "有新的提现申请待处理", addresseeList, 2, 300, 3, null);
				json=JsonData.success();
			}
		}
		return json;
	}
	
	
	/**
	 * [日志] 记录机构账户变化日志
	 * 
	 * @author fanjunguo
	 * @param orgId
	 * @param cashFlowEntity
	 * @return 更新影响的行数
	 */
	private int recordCashFlowLogs(String orgId,List<CashFlowEntity> list) {
		Query query=new Query(Criteria.where("orgId").is(orgId));
		Update update=new Update();
		PushOperatorBuilder push = update.push("cashFlows");
		push.each(list);
		WriteResult upsertResult = mongo.upsert(query, update, CashFlowEntity.class, "cash_flow");
		return upsertResult.getN();
	}
	
	
	/**
	 * 更新机构账户(只更新单个字段,更新的字段由传入的参数决定)
	 * 
	 * @param orderId 造成账户变更的订单号,用于定位机构id
	 * @param amountOfEntering 变化的金额的绝对值
	 * @param fieldName 要更改的字段名字.可用的选项:amount_entering-入账中金额; amount_usable -账户可用金额 ; deposit - 机构押金
	 * @param operation 操作:1.订单收入 2.提现 3.退款 4.服务费
	 * @param flowDirection 资金流向 1表示资金流入 -1表示资金流出
	 * @return
	 */
	@Override
	public JsonData updateAccountOfSingleField(String orderId,double amountOfEntering,String fieldName,int operation,int flowDirection) {
		String orgId = orderDao.getOrgIdByOrderId(orderId);
		//计算手续费
		double serviceFee=(double)(Math.round(amountOfEntering*serviceFeeRate*100)/100.0) ;
		
		//实际更新的金额,应该扣掉手续费
		double amountOfEntering2=amountOfEntering-serviceFee;
		//此方法金额需要分正负,所以要金额*资金流向
		int updateResult = dao.updateAccountInfo(orgId, amountOfEntering2*flowDirection,fieldName);
		if (updateResult>0) {
			List<CashFlowEntity> list = new ArrayList<>();
			CashFlowEntity cashFlowEntity=new CashFlowEntity(orderId,amountOfEntering, flowDirection, operation, LocalDateTime.now().toString(), orgId);
			list.add(cashFlowEntity);
			if (Math.abs(serviceFee)>0) {
				//手续费正好是跟资金流向相反的,所以记录手续费的时候,记录相反数 -flowDirection
				CashFlowEntity cashFlowEntityOfServiceFee=new CashFlowEntity(orderId,serviceFee, -flowDirection, 4, LocalDateTime.now().toString(), orgId);
				list.add(cashFlowEntityOfServiceFee);
			}
			recordCashFlowLogs(orgId,list);
			return JsonData.success();
		} else {
			logger.error("[严重bug]订单状态变更,账户金额变更失败");
			return JsonData.error();
		}
	}
	
	
	/**
	 * 更新机构账户入账中金额和可用金额.用户确认付款给机构时,入账中金额转为可用金额
	 * 
	 * @author fanjunguo
	 * @param orgId 机构ID
	 * @param amount 变化的金额
	 * @return 600/400
	 */
	@Override
	public JsonData updateAccountOfUsableAndEnterring(String orgId,double amount) {
		int updateResult = dao.updateAccountOfUsableAndEnterring(orgId,amount);
		JsonData json;
		if (updateResult==1) {
			json=JsonData.success();
		} else {
			json=JsonData.error();
		}
		return json;
	}
	

}
