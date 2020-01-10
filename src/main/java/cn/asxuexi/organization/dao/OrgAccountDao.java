package cn.asxuexi.organization.dao;

import org.apache.ibatis.annotations.Param;

import cn.asxuexi.organization.entity.CashFlowEntity;
import cn.asxuexi.tool.JsonData;

public interface OrgAccountDao {

	/**
	 * 查询机构账户信息,余额、押金等
	 * 
	 * 
	 * @author fanjunguo
	 */
	JsonData getAccountInfo(String orgId);
	

	/**
	 * 保存提现申请
	 * 
	 * @param cashOutEntity 提现申请信息
	 */
	int insertCashOutApplication(CashFlowEntity cashOutEntity);
	
	
	/**
	 * 更新机构账户信息.该方法不是固定修改具体字段,而是根据传入的参数名字决定修改哪个字段
	 * 该方法只能修改单个字段
	 * 
	 * @author fanjunguo
	 * @param orgId 机构id
	 * @param amountOfChange 帐户变化的金额.参数传的都是更改的金额,金额增加传正值,金额减少传负值
	 * @param fieldName 要修改的字段名称.可用的选项:amount_entering-入账中金额; amount_usable -账户可用金额 ; deposit - 机构押金
	 * @return
	 */
	int updateAccountInfo(@Param("orgId")String orgId,@Param("amountOfChange") double amountOfChange, @Param("fieldName")String fieldName);

	/**
	 * 创建机构账户记录
	 * 
	 * @author fanjunguo
	 * @param orgId
	 */
	int createOrgAccount(String orgId);


	/**
	 * 同时更新机构账户入账中金额和可用金额.用户确认付款给机构时,入账中金额转为可用金额
	 * 
	 * @author fanjunguo
	 * @param orgId 机构id
	 * @param amount 变化的金额
	 */
	int updateAccountOfUsableAndEnterring(@Param("orgId")String orgId, @Param("amount")double amount);

}
