package cn.asxuexi.entity;

import java.util.List;

public class PersonalMenu {
	private int id;
	private String name;
	private String url ;
	private String parent;
	private String icon;
	private List<PersonalMenu> children;
	public List<PersonalMenu> getChildren() {
		return children;
	}
	public void setChildren(List<PersonalMenu> children) {
		this.children = children;
	}
	public PersonalMenu() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PersonalMenu(int id, String name, String url, String parent, String icon) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.parent = parent;
		this.icon = icon;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
