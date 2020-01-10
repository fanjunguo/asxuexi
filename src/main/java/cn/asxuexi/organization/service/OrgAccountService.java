package cn.asxuexi.organization.service;

import cn.asxuexi.organization.entity.CashFlowEntity;
import cn.asxuexi.tool.JsonData;

public interface OrgAccountService {

	/**
	 * 查询机构账户信息,余额、押金等
	 * 
	 * 
	 * @author fanjunguo
	 */
	JsonData getAccountInfo();

	/**
	 * 提现申请
	 * 
	 * @author fanjunguo
	 * @param cashInfo
	 * @return
	 * @throws Exception 
	 */
	JsonData getCashOut(CashFlowEntity cashInfo) throws Exception;

	/**
	 * 查询所有的资金流信息
	 * 
	 * @author fanjunguo
	 * @return
	 */
	JsonData getCashFlowInfo();

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
	JsonData  updateAccountOfSingleField(String orderId, double amountOfEntering, String fieldName, int operation,int flowDirection);

	/**
	 * 更新机构账户入账中金额和可用金额.用户确认付款给机构时,入账中金额转为可用金额
	 * 
	 * @author fanjunguo
	 * @param orgId 机构ID
	 * @param amount 变化的金额
	 * @return 600/400
	 */
	JsonData updateAccountOfUsableAndEnterring(String orgId, double amount);

}
