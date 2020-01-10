package cn.asxuexi.orgImg.entity;

import java.time.LocalDateTime;

public class OrgImgEntity {
	private String imgId;
	private String imgName;
	private String orgId;
	private LocalDateTime gmtCreate;
	private Integer status;

	public OrgImgEntity() {
		super();
	}

	public OrgImgEntity(String imgId, String imgName, String orgId, LocalDateTime gmtCreate, Integer status) {
		super();
		this.imgId = imgId;
		this.imgName = imgName;
		this.orgId = orgId;
		this.gmtCreate = gmtCreate;
		this.status = status;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public LocalDateTime getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(LocalDateTime gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
