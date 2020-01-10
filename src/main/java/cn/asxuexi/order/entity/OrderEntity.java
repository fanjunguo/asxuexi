package cn.asxuexi.order.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 订单实体类
 *
 * @author fanjunguo
 * @version 2019年4月12日 下午3:15:13
 */
public class OrderEntity implements Serializable {

	private static final long serialVersionUID = 123456L;
	
	@NotNull @NotBlank
	private String packageId; // 套餐id
	private String orderId; // 订单号
	//订单状态:10-待付款 11-支付成功 12-交易完成 13-退款中 14-已退款 20-进行中 21-交易完成 -1-已取消
	private int orderStatus;
	private String userId;
	private String courseId;
	private double orderPrice; //订单总金额(包含优惠,红包等,暂时不用)
	@NotNull
	private double paymentAmount; // 支付总金额
	@NotNull @NotBlank
	private String studentId;
	private String studentName;
	private int chargeType; // 收费方式: 1.预付 2.后付
	private String tel; //学生联系手机号
	
	//为了个人订单中心,新加的属性
	private String packageName;
	private String courseName;
	private String address; //机构地址
	private String courseImg;
	private String orgName;
	//订单对应的机构
	private String orgId; 
	private String gmt_create; //订单创建时间
	/**
	 * 支付方式:1-支付宝(默认) 2-微信 
	 */
	private int payType=1; 
	
	public OrderEntity(String packageId, String orderId, int orderStatus, String userId, String courseId,
			double orderPrice, double paymentAmount, String studentId, String studentName, int chargeType, String tel,
			String packageName, String courseName, String address, String courseImg, String orgName, String orgId,
			String gmt_create, int payType) {
		super();
		this.packageId = packageId;
		this.orderId = orderId;
		this.orderStatus = orderStatus;
		this.userId = userId;
		this.courseId = courseId;
		this.orderPrice = orderPrice;
		this.paymentAmount = paymentAmount;
		this.studentId = studentId;
		this.studentName = studentName;
		this.chargeType = chargeType;
		this.tel = tel;
		this.packageName = packageName;
		this.courseName = courseName;
		this.address = address;
		this.courseImg = courseImg;
		this.orgName = orgName;
		this.orgId = orgId;
		this.gmt_create = gmt_create;
		this.payType = payType;
	}

	public OrderEntity() {

	}

	
	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getGmt_create() {
		return gmt_create;
	}

	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAddress() {
		return address;
	}

	public String getCourseImg() {
		return courseImg;
	}

	public void setCourseImg(String courseImg) {
		this.courseImg = courseImg;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public int getChargeType() {
		return chargeType;
	}

	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

}
