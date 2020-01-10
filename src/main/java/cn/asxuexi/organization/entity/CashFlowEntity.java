package cn.asxuexi.organization.entity;
/**
 * 机构 资金流 实体类,记录资金流动信息
 *
 * @author fanjunguo
 * @version 2019年6月21日 下午3:23:33
 */

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class CashFlowEntity {
	
	private String id; //标识id
	@NotNull
	private double amount;					//变化金额

	int flowDirection;  //资金流向 1:收入 -1:支出 如提现、退款等
	int operation; //1.订单收入 2.提现 3.退款 4.服务费
	
	@NotNull(groups= {GetCashOut.class})
	private String cardNumber; 				//提现时提供的银行卡号
	@NotNull(groups= {GetCashOut.class}) @NotEmpty(groups= {GetCashOut.class})
	private String ownerName; 				//持卡人姓名
	private String bankCode; 				//银行代码
	private String bankName;
	private String cardType; 				//卡片类型DC: "储蓄卡",CC: "信用卡",SCC: "准贷记卡",PC: "预付费卡"
	private String cardTypeName; 			//卡片类型名称
	private String gmt_created;
	private String gmt_modified;
	private String orgId;  //申请的机构
	
	public CashFlowEntity() {
		super();
	}
	
	/**
	 * 订单状态变更时,记录日志所需要的信息
	 * 
	 * @param amount 变化的金额的绝对值
	 * @param flowDirection 资金流向 1表示资金流入 -1表示资金流出
	 * @param operation 操作:1.订单收入 2.提现 3.退款 4.服务费
	 * @param gmt_created
	 * @param orgId 对应的机构id
	 */
	public CashFlowEntity(String id, double amount, int flowDirection, int operation, String gmt_created, String orgId) {
		super();
		this.id = id;
		this.amount = amount;
		this.flowDirection = flowDirection;
		this.operation = operation;
		this.gmt_created = gmt_created;
		this.orgId = orgId;
	}


	public CashFlowEntity(String id ,double amount, int flowDirection, int operation, String cardNumber, String ownerName,
			String bankCode, String bankName, String cardType, String cardTypeName, String gmt_created,
			String gmt_modified, String orgId) {
		super();
		this.id=id;
		this.amount = amount;
		this.flowDirection = flowDirection;
		this.operation = operation;
		this.cardNumber = cardNumber;
		this.ownerName = ownerName;
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.cardType = cardType;
		this.cardTypeName = cardTypeName;
		this.gmt_created = gmt_created;
		this.gmt_modified = gmt_modified;
		this.orgId = orgId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getFlowDirection() {
		return flowDirection;
	}


	public void setFlowDirection(int flowDirection) {
		this.flowDirection = flowDirection;
	}


	public int getOperation() {
		return operation;
	}

	/**
	 * 设置操作类型
	 * 
	 * @param operation 1.订单收入 2.提现 3.退款 4.服务费
	 */
	public void setOperation(int operation) {
		this.operation = operation;
	}


	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	public String getCardNumber() {
		return cardNumber;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}



	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}


	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public String getBankCode() {
		return bankCode;
	}


	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getCardType() {
		return cardType;
	}


	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	public String getCardTypeName() {
		return cardTypeName;
	}


	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}


	public String getGmt_created() {
		return gmt_created;
	}


	public void setGmt_created(String gmt_created) {
		this.gmt_created = gmt_created;
	}


	public String getGmt_modified() {
		return gmt_modified;
	}


	public void setGmt_modified(String gmt_modified) {
		this.gmt_modified = gmt_modified;
	}
	
	
	
	
}
