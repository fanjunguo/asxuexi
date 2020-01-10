package cn.asxuexi.all.entity;

public class Nav_top {
	private String mark;
	private int rows;
	private String sort_id;
	private String text;
	private String sort_name;
	private String nav_id_top;
	
	public Nav_top(String mark, int rows, String sort_id, String text, String sort_name, String nav_id_top) {
		super();
		this.mark = mark;
		this.rows = rows;
		this.sort_id = sort_id;
		this.text = text;
		this.sort_name = sort_name;
		this.nav_id_top = nav_id_top;
	}
	public String getNav_id_top() {
		return nav_id_top;
	}
	public void setNav_id_top(String nav_id_top) {
		this.nav_id_top = nav_id_top;
	}
	public String getSort_name() {
		return sort_name;
	}
	public void setSort_name(String sort_name) {
		this.sort_name = sort_name;
	}
	
	public Nav_top() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSort_id() {
		return sort_id;
	}
	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
