package cn.asxuexi.all.entity;

public class TopNav {
	private String nav_top_id;
	private String nav_top_name;
	private String nav_top_href;
	private String mark;
	private String img;



	public TopNav(String nav_top_id, String nav_top_name, String nav_top_href, String mark,String img) {
		super();
		this.nav_top_id = nav_top_id;
		this.nav_top_name = nav_top_name;
		this.nav_top_href = nav_top_href;
		this.mark = mark;
		this.img=img;
	}

	public TopNav() {
	
	}
	public String getImg() {
		return img;
	}
	public String getMark() {
		return mark;
	}
	
	public void setImg(String img) {
		this.img=img;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getNav_top_id() {
		return nav_top_id;
	}

	public void setNav_top_id(String nav_top_id) {
		this.nav_top_id = nav_top_id;
	}

	public String getNav_top_name() {
		return nav_top_name;
	}

	public void setNav_top_name(String nav_top_name) {
		this.nav_top_name = nav_top_name;
	}

	public String getNav_top_href() {
		return nav_top_href;
	}

	public void setNav_top_href(String nav_top_href) {
		this.nav_top_href = nav_top_href;
	}

}
