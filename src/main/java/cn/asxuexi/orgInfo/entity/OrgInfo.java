package cn.asxuexi.orgInfo.entity;

import java.math.BigDecimal;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import cn.asxuexi.organization.entity.CreateGroup;
import cn.asxuexi.organization.entity.UpdateGroup;

public class OrgInfo {
	@NotBlank(groups= {UpdateGroup.class,CreateGroup.class},message="机构名称不能为空")
	private String orgName;
	@NotBlank(groups= {UpdateGroup.class,CreateGroup.class},message="电话不能为空") @Pattern(regexp="1[3-9]\\d{9}",message="手机号格式错误")
	private String orgtel;
	@NotBlank(groups= {UpdateGroup.class,CreateGroup.class},message="机构负责人不能为空")
	private String orgLegalPerson; //负责人 
	private String orgAddress;
	private String orgDes;
	private BigDecimal lat;
	private BigDecimal lng;
	private String localId;	//县区id
	private String roomNumber; //门牌号
	@NotBlank(groups= {UpdateGroup.class,CreateGroup.class},message="头像不能为空")
	private String logoUrl;
	@NotBlank(groups= {UpdateGroup.class},message="机构id必传")
	private String orgId;
	
	/**
	 * 初始化所有属性的构造方法
	 */
	public OrgInfo(String orgName, String orgtel, String orgLegalPerson, String orgAddress, String orgDes,
			BigDecimal lat, BigDecimal lng, String localId,String roomNumber,String logoUrl,String orgId) {
		this.orgName = orgName;
		this.orgtel = orgtel;
		this.orgLegalPerson = orgLegalPerson;
		this.orgAddress = orgAddress;
		this.orgDes = orgDes;
		this.lat = lat;
		this.lng = lng;
		this.localId = localId;
		this.roomNumber=roomNumber;
		this.logoUrl=logoUrl;
		this.orgId=orgId;
	}
	
	/**
	 * 不带orgId的构造方法
	 */
	public OrgInfo(String orgName, String orgtel, String orgLegalPerson, String orgAddress, String orgDes,
			BigDecimal lat, BigDecimal lng, String localId,String roomNumber,String logoUrl) {
		this.orgName = orgName;
		this.orgtel = orgtel;
		this.orgLegalPerson = orgLegalPerson;
		this.orgAddress = orgAddress;
		this.orgDes = orgDes;
		this.lat = lat;
		this.lng = lng;
		this.localId = localId;
		this.roomNumber=roomNumber;
		this.logoUrl=logoUrl;
	}
	
	public OrgInfo() {
		super();
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgtel() {
		return orgtel;
	}
	public void setOrgtel(String orgtel) {
		this.orgtel = orgtel;
	}
	public String getOrgLegalPerson() {
		return orgLegalPerson;
	}
	public void setOrgLegalPerson(String orgLegalPerson) {
		this.orgLegalPerson = orgLegalPerson;
	}
	public String getOrgAddress() {
		return orgAddress;
	}
	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}
	public String getOrgDes() {
		return orgDes;
	}
	public void setOrgDes(String orgDes) {
		this.orgDes = orgDes;
	}
	public BigDecimal getLat() {
		return lat;
	}
	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}
	public BigDecimal getLng() {
		return lng;
	}
	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}
	public String getLocalId() {
		return localId;
	}
	public void setLocalId(String localId) {
		this.localId = localId;
	}
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	
}
