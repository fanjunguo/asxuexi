package cn.asxuexi.all.entity;

public class Item_tab_course_entity {
	private String ownid;
	private String parentid;
	private String ownname;
	private short leveltype;
	private String courseid;
	
	public Item_tab_course_entity() {
		super();
	}
	
	public Item_tab_course_entity(String ownid, String parentid, String ownname, short leveltype, String courseid) {
		super();
		this.ownid = ownid;
		this.parentid = parentid;
		this.ownname = ownname;
		this.leveltype = leveltype;
		this.courseid = courseid;
	}

	public String getOwnid() {
		return ownid;
	}
	public void setOwnid(String ownid) {
		this.ownid = ownid;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getOwnname() {
		return ownname;
	}
	public void setOwnname(String ownname) {
		this.ownname = ownname;
	}
	public short getLeveltype() {
		return leveltype;
	}
	public void setLeveltype(short leveltype) {
		this.leveltype = leveltype;
	}
	public String getCourseid() {
		return courseid;
	}
	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	
}
