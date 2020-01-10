package cn.asxuexi.android;
/**
 * @author 张顺
 * 
 * */
public class SortOperate {
	private String sortId;
	private String sortParentid;
	private String sortName;
	private String sortGrade;
	
	public SortOperate() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SortOperate(String sortId, String sortParentid, String sortName, String sortGrade) {
		super();
		this.sortId = sortId;
		this.sortParentid = sortParentid;
		this.sortName = sortName;
		this.sortGrade = sortGrade;
		
	}
	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	public String getSortParentid() {
		return sortParentid;
	}
	public void setSortParentid(String sortParentid) {
		this.sortParentid = sortParentid;
	}
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public String getSortGrade() {
		return sortGrade;
	}
	public void setSortGrade(String sortGrade) {
		this.sortGrade = sortGrade;
	}
	
}
