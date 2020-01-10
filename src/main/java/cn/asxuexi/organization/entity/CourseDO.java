package cn.asxuexi.organization.entity;

import java.util.List;
import java.util.Map;

public class CourseDO {
	private String course_id;
	private String course_name;
	private String course_teacher;
	private String course_description;
	private String course_charging;
	private String org_id;
	private String org_name;
	private List<String> sortIdList;
	private List<Map<String, Object>> packageList;
	private List<Map<String, Object>> pictureList;
	private List<Map<String, Object>> videoList;
	private List<Map<String, Object>> courseTimetable;

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public String getCourse_teacher() {
		return course_teacher;
	}

	public void setCourse_teacher(String course_teacher) {
		this.course_teacher = course_teacher;
	}

	public String getCourse_description() {
		return course_description;
	}

	public void setCourse_description(String course_description) {
		this.course_description = course_description;
	}

	public String getCourse_charging() {
		return course_charging;
	}

	public void setCourse_charging(String course_charging) {
		this.course_charging = course_charging;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public List<String> getSortIdList() {
		return sortIdList;
	}

	public void setSortIdList(List<String> sortIdList) {
		this.sortIdList = sortIdList;
	}

	public List<Map<String, Object>> getPackageList() {
		return packageList;
	}

	public void setPackageList(List<Map<String, Object>> packageList) {
		this.packageList = packageList;
	}

	public List<Map<String, Object>> getPictureList() {
		return pictureList;
	}

	public void setPictureList(List<Map<String, Object>> pictureList) {
		this.pictureList = pictureList;
	}

	public List<Map<String, Object>> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<Map<String, Object>> videoList) {
		this.videoList = videoList;
	}

	public List<Map<String, Object>> getCourseTimetable() {
		return courseTimetable;
	}

	public void setCourseTimetable(List<Map<String, Object>> courseTimetable) {
		this.courseTimetable = courseTimetable;
	}

}
