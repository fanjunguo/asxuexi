package cn.asxuexi.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.asxuexi.message.tool.MessageTool;
import cn.asxuexi.order.controller.AlipayController;
import cn.asxuexi.order.dao.OrderDao;
import cn.asxuexi.order.entity.OrderEntity;
import cn.asxuexi.organization.service.OrgAccountService;
import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.JsonData;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.Token_JWT;

@Service
public class OrderServiceImpl implements OrderService {
	@Resource
	private OrderDao dao;
	@Resource
	private OrgAccountService orgAccountService;
	@Resource
	private WXPayService wxPayService;
	@Autowired
	private MessageTool messageTool;
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	
	private int orderStatus=10;
	
	/* 保存订单
	 * 先验证订单信息是否正确,是否有变更.当通过验证时,再创建订单
	 * 
	 * @version 2019.7.19 订单中增加org_id的字段.后期要多处用到这个字段,联合查询太麻烦,所以在新增的订单时候存入表中
	 * */
	@Override
	public Map<String, Object> createOrder(OrderEntity order) {
		Map<String, Object> result = verifyOrder(order.getPackageId(), order.getPaymentAmount());
		int resultCode=(int)result.get("code");
		if (resultCode==600) {
			//验证通过
			String userId = (String) Token_JWT.verifyToken().get("userId");
			//生成订单号 和订单创建时间
			String orderId = new Date().getTime() + ""+ RandomTool.getRandomInt(8) ;
			result.put("orderId", orderId);
			
			int chargeType=(int)result.get("chargeType");
			if (chargeType==1) {
				order.setOrderStatus(10);
			} else {
				order.setOrderStatus(20);
			}
			order.setChargeType(chargeType);
			order.setUserId(userId);
			order.setOrderId(orderId);
			order.setCourseId(result.get("course_id").toString());
			order.setOrgId(result.get("orgId").toString());
			
			try {
				dao.createOrder(order);
				result.put("data", order);
			} catch (Exception e) {
				int code=400;
				result.put("code", code);
				logger.error("保存订单发生异常",e);
			}
			
		}
		
		return result;
	}
	
	
	//根据订单号,查询订单信息
	@Override
	public Map<String, Object> getOrderInfo(String orderId){
		String userId = (String)Token_JWT.verifyToken().get("userId");
		Map<String, Object> json= new HashMap<>();
		int code;
		String message;
		if (orderId==null) {
			code=412;
			message="订单号为空,请求不合法";
		} else {
			try {
				code=600;
				message="success";
				Map<String, Object> orderInfo = dao.getOrderInfo(orderId,userId,orderStatus);
				json.put("data", orderInfo);
			} catch (Exception e) {
				code=410;
				message="查询数据库异常";
				logger.error(message,e);
			}
		}
		
		json.put("code", code);
		json.put("message", message);
		return json;
	}
	
	
	
	/**
	 * 校验订单信息:
	 * 	1.产品是否失效
	 * 	2.价格是否变更
	 * 	3.其他优惠信息是否变更(暂无)
	 * 	本方法中,订单价格参数传入,产品有效时间后台查询
	 * 
	 * @version 
	 *  1.不再校验时间
	 *  2.验证订单的时候,根据package查询下机构id,并在结果中返回.
	 * 
	 * @param packageId 套餐id
	 * @param orderPrice 前台传入的套餐价格
	 * @return json: 
	 * 			code:600-验证通过; 411-价格变更;
	 * 			orgId:机构id
	 * */
	public Map<String, Object> verifyOrder(String packageId,double orderPrice) {
		Map<String, Object> packageInfo = dao.getPackageInfo(packageId);
		
		int code=600;
		double packagePrice = ((BigDecimal) packageInfo.get("package_price")).doubleValue();
		
		if(packagePrice!=orderPrice) {
			code=411;
		}
		packageInfo.put("code", code);
		return packageInfo;
	}
	
	
	
	
	//获取订单支付状态
	@Override
	public JsonData getOrderPaymentResult(String orderId){
		JsonData json=null;
		String userId = (String)Token_JWT.verifyToken().get("userId");
		JsonData orderInfo = dao.getOrderStatus(orderId);
		if (userId.equals((String)orderInfo.get("userId"))) {
			json=JsonData.success(orderInfo);
		} else {
			json=JsonData.illegalParam();
		}
		return json;
	}
	
	//普通用户-查询所有的订单(包括信息)
	@Override
	public Map<String, Object> getAllOrders(int page, int rows,int orderStatus){
		Map<String, Object> json=new HashMap<>(16);
		int code=400;
		String message="failure";
		String userId = (String)Token_JWT.verifyToken().get("userId");
		if (userId!=null) {
			PageHelper.startPage(page, rows);
			List<OrderEntity> allOrders = dao.getAllOrders(userId,orderStatus);
			PageInfo<OrderEntity> pageInfo = new PageInfo<>(allOrders);
			Map<String, Object> billOfUser = getBillOfUser();
			if ((int)billOfUser.get("code")==600) {
				List<Map<String, Object>> jsonData=new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> allBills=JSONObject.parseObject(JSONObject.toJSONString(billOfUser.get("data")),new TypeReference<List<Map<String, Object>>>() {});
				
					for (OrderEntity eachOrder : allOrders) {
						String courseId = eachOrder.getCourseId();
						List<Map<String, Object>> billData=new ArrayList<Map<String, Object>>();
						Map<String, Object> eachOrderDate=new HashMap<>();
						for (Map<String, Object> bill : allBills) {
							String courseIdOfBill = (String) bill.get("course_id");
							if (courseId.equals(courseIdOfBill)) {
								billData.add(bill);
							}
						}
						eachOrderDate.put("billList", billData);
						eachOrderDate.put("orderMap", eachOrder);
						jsonData.add(eachOrderDate);
					}
				json.put("data", jsonData);
				json.put("totalPage", pageInfo.getPages());
				json.put("total", pageInfo.getTotal());

				code=600;
				message="success";
			}
		}
		json.put("code", code);
		json.put("message", message);
		
		return json;
	}
	
	/*
	 * 机构--查询所有订单
	 */
	@Override
	public Map<String, Object> getAllOrdersOfOrg(int page, int rows,int orderStatus){
		Map<String, Object> json=new HashMap<>();
		int code=400;
		String message="failure";
		String orgId = GetOrgIdFromRedis.getOrgId();
		if (null!=orgId) {
			PageHelper.startPage(page, rows);
			List<Map<String, Object>> orderList = dao.getAllOrdersOfOrg(orgId,orderStatus);
			PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(orderList);
			json.put("data", orderList);
			json.put("totalPage", pageInfo.getPages());
			json.put("total", pageInfo.getTotal());
			code=600;
			message="success";
		}
		json.put("code", code);
		json.put("message", message);
		return json;
	}
	
	
	
	
	/*
	 * 取消订单
	 * 	先查看当前订单状态和订单类型.
	 * 	1.如果是预付,状态是10,直接取消订单
	 * 	2.如果状态是11,需要先退款后取消
	 * 	3.如果是后付,状态是20.并且没有未结算的账单,可以退款;
	 * 
	 * 还需要确定退款金额:全部退款还是部分退款.目前的需求,都是全部退款.后期如果需要部分退款
	 * 	1.需要传参确定金额; 2.根据固定规则,如30%.确定退款金额
	 * 
	 * 2019.8.28 增加判断支付途径,像不同的第三方取消订单/申请退款
	 */
	@Override
	public JsonData cancelOrder(String orderId,int reasonCode,boolean isOrg){
		JsonData json=JsonData.error();
		//身份校验是否一致的标志
		boolean verifyIdentity=false;
		JsonData orderInfo= dao.getOrderStatus(orderId);
		if (null!=orderInfo) {
			if (isOrg) {
				verifyIdentity=GetOrgIdFromRedis.getOrgId().equals( (String) orderInfo.get("orgId"));
			} else {
				String userId = (String)Token_JWT.verifyToken().get("userId");
				verifyIdentity=userId.equals((String)orderInfo.get("userId"));
			}
		}
		
		if (verifyIdentity) {
			Integer orderStatus=(Integer) orderInfo.get("orderStatus");
			if (null!=orderStatus) {
				//后付订单
				if(orderStatus.compareTo(20)==0) {
					//todo:还需要确定是否有未结算账单
					dao.cancelOrder(orderId, -1,reasonCode);
					json=JsonData.success();
				}
				//预付订单,判断不同的订单状态
				else {
					Double orderPrice =((BigDecimal) orderInfo.get("paymentAmount")).doubleValue();
					//如果支付金额为0,或者是待支付状态,直接取消订单,绕开支付宝接口.
					if (orderPrice==0||orderStatus==10) {
						dao.cancelOrder(orderId, -1,reasonCode);
						json=JsonData.success();
					} else {
						/*
						 * 11已支付;
						 * 12表示用户已经打款给机构.此时的订单只有机构能够取消,用户无法取消
						 * */
						if(orderStatus.compareTo(11)==0||(orderStatus.compareTo(12)==0&&isOrg)) {
							//调用第三方退款接口
							double refundAmount=dao.getPaymentAmount(orderId);
							JsonData refundResult=thirdPlatformRefund(dao.getPayType(orderId), orderId, refundAmount);
							if ((int)refundResult.get("code") == 600) {
								dao.cancelOrder(orderId, 14,reasonCode);
								//更新账户
								String fieldName;
								if (orderStatus.compareTo(11)==0) {
									fieldName="amount_entering";
								} else {
									fieldName="amount_usable";
								}
								orgAccountService. updateAccountOfSingleField(orderId, refundAmount, fieldName, 3, -1);
								json=JsonData.success();
							}
						}

					}
				}
			}
		}else {
			json=JsonData.illegalParam();
		}
		
		return json;
	}
	
	/**
	 * 第三方平台退款
	 * 
	 * @param payType 支付渠道 1-支付宝 2-微信 ...
	 * @param orderId 订单号
	 * @param refundAmount 退款金额,单位为元
	 * @return 成功600,失败400
	 */
	private JsonData thirdPlatformRefund(int payType,String orderId,double refundAmount) {
		JsonData result=JsonData.error();
		switch (payType) {
		case 1:
			result = AlipayController.refund(orderId, refundAmount);
			break;
		case 2:
			//微信退款
			int refundFee=(int) (refundAmount*100);
			result=wxPayService.refund(orderId, refundFee);
		}
		return result;
	}
	
	
	
	
	//查询订单详情
	@Override
	public Map<String, Object> getOrderDetail(String orderId){
		Map<String, Object> json=new HashMap<>();
		int code=600;
		String message="success";
		
		Map<String, Object> orderDetail = dao.getOrderDetail(orderId);
		if (orderDetail==null) {
			code=500;
			message="noData";
		}else {
			String cancelRules = dao.getCancelRules((int) orderDetail.get("charge_type"));
			orderDetail.put("cancelRules", cancelRules);
			String courseImg = dao.getCourseImg((String) orderDetail.get("course_id"));
			orderDetail.put("courseImg", courseImg);
		}
		json.put("code", code);
		json.put("message", message);
		json.put("data", orderDetail);
		return json;
	}
	
	
	//在加载订单的时候,查询该用户所有的账单信息
	@Override
	public  Map<String, Object> getBillOfUser(){
		Map<String, Object> json=new HashMap<>();
		int code=600;
		String message="success";
		String userId = (String)Token_JWT.verifyToken().get("userId");
		if (null!=userId) {
			List<Map<String, Object>> bill = dao.getBillOfUser(userId);
			json.put("data", bill);
		}else {
			code=400;
			message="failure";
		}
		json.put("code", code);
		json.put("message", message);
		return json;
	}

	
	//删除订单
	@Override
	public Map<String, Object> deleteOrder(String orderId, String identity) {
		Map<String, Object> json=new HashMap<>();
		int code=600;
		String message="success";
		int deleteResult = dao.deleteOrder(orderId,identity);
		if (0==deleteResult) {
			code=400;
			message="failure";
		}
		json.put("code", code);
		json.put("message", message);
		return json;
	}

	//更新订单状态
	@Override
	public JsonData updateOrderStatus(String orderId) {
		//必须校验,订单金额是否为0,防止非法请求
		Map<String, Object> orderDetail = dao.getOrderDetail(orderId);
		double paymentAmount = ((BigDecimal) orderDetail.get("payment_amount")).doubleValue();
		if (paymentAmount==0) {
			int orderStatus=11;
			int updateResult = dao.updateOrder(orderId, orderStatus);
			if (updateResult==1) {
				return JsonData.success();
			} else {
				return JsonData.error();
			}
		}
		return JsonData.illegalParam();
		
	}

	/**
	 * 用户付款给机构
	 * 需要校验订单状态是否为已支付
	 * 
	 * @param orderId 订单号
	 */
	
	//todo:增加事务回滚
	@Override
	public JsonData PersonPayToOrg(String orderId) {
		JsonData json;
		Map<String, Object> orderDetail = dao.getOrderDetail(orderId);
		int orderStatus = (int) orderDetail.get("order_status");
		if (orderStatus==11) {
			dao.updateOrder(orderId, 12);
			orgAccountService.updateAccountOfUsableAndEnterring((String)orderDetail.get("orgId"), ((BigDecimal)orderDetail.get("payment_amount")).doubleValue());
			json=JsonData.success();
			String orgId = (String) orderDetail.get("org_id");
			messageTool.sendMessage("订单收入入账通知", "您的订单:"+orderId+",学生已确认打款,金额已入账", orgId, 2, 207, 2, null);
		} else {
			json=JsonData.illegalParam();
		}
		return json;
	}
}
