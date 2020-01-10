package cn.asxuexi.entity;

public class AlipayUser {

	//支付宝用户id
	private String useId;
	//支付宝用户头像
	private String avatar;
	//支付宝用户昵称
	private String nickName;
	//支付宝用户省份
	private String province;
	//支付宝用户城市
	private String city;
	//支付宝用户性别 M为男性，F为女性
	private String gender;
	//支付宝用户类型 1代表公司账号，2代表个人账号
	private String userType;
	/*
	 * 支付宝用户状态
	 * Q代表快速注册用户
	 * T代表已认证用户
	 * B代表被冻结账户
	 * W代表已注册，未激活的账户 
	 * 
	 * */
	private String userStatus;
	//支付宝用户是否通过实名认证 T代表通过，F是没有
	private String isCertified;
	//支付宝用户是否是学生 T是学生，F不是学生
	private String  isStudentCertified;
	
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUseId() {
		return useId;
	}
	public void setUseId(String useId) {
		this.useId = useId;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getIsCertified() {
		return isCertified;
	}
	public void setIsCertified(String isCertified) {
		this.isCertified = isCertified;
	}
	public String getIsStudentCertified() {
		return isStudentCertified;
	}
	public void setIsStudentCertified(String isStudentCertified) {
		this.isStudentCertified = isStudentCertified;
	}
	

}
