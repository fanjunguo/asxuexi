package cn.asxuexi.all.entity;

import java.util.List;
import java.util.Map;

public class LeftNav {
	private String sort_id;
	private String sort_name;
	private String rows;
	private String alias;
	private List<Map<String, Object>> two;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<Map<String, Object>> getTwo() {
		return two;
	}

	public void setTwo(List<Map<String, Object>> two) {
		this.two = two;
	}

	public String getSort_id() {
		return sort_id;
	}

	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
	}

	public String getSort_name() {
		return sort_name;
	}

	public void setSort_name(String sort_name) {
		this.sort_name = sort_name;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public LeftNav(String sort_id, String sort_name, String rows) {
		super();
		this.sort_id = sort_id;
		this.sort_name = sort_name;
		this.rows = rows;
	}

	public LeftNav(String sort_id, String sort_name, String rows, String alias, List<Map<String, Object>> two) {
		super();
		this.sort_id = sort_id;
		this.sort_name = sort_name;
		this.rows = rows;
		this.alias = alias;
		this.two = two;
	}

	public LeftNav() {
		super();
		// TODO Auto-generated constructor stub
	}

}
