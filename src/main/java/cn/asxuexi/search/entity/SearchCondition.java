package cn.asxuexi.search.entity;

import com.alibaba.fastjson.JSON;

public class SearchCondition {
	private String type = "1";
	private String keyword = "";
	private String sortId = "";
	private String sortLevel = "";
	private String cityId = "";
	private String pageNum = "1";
	private String pageRows = "10";
	private String order = "default";
	private String latitude = "";
	private String longitude = "";
	private String distance = "";

	public SearchCondition() {
		super();
	}

	public SearchCondition(String type, String keyword, String sortId, String cityId, String pageNum, String pageRows,
			String order, String latitude, String longitude, String distance, String sortLevel) {
		super();
		this.type = type;
		this.keyword = keyword;
		this.sortId = sortId;
		this.sortLevel = sortLevel;
		this.cityId = cityId;
		this.pageNum = pageNum;
		this.pageRows = pageRows;
		this.order = order;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getPageRows() {
		return pageRows;
	}

	public void setPageRows(String pageRows) {
		this.pageRows = pageRows;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getSortLevel() {
		return sortLevel;
	}

	public void setSortLevel(String sortLevel) {
		this.sortLevel = sortLevel;
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}
