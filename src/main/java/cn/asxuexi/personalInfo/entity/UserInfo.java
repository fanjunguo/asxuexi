package cn.asxuexi.personalInfo.entity;

public class UserInfo {
	private String name;
	private String sex;
	private String year;
	private String month;
	private String day;
	private String address;
	private String birth;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public UserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserInfo(String name, String sex, String year, String month, String day, String address) {
		super();
		this.name = name;
		this.sex = sex;
		this.year = year;
		this.month = month;
		this.day = day;
		this.address = address;
	}
}
