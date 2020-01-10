package cn.asxuexi.android;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 张顺
 * @作用 对取出的数据进行序列化成树状结构
 */
public class SortOperateTree implements Serializable {
	private static final long serialVersionUID = 2604341300672377675L;
	
	private String Id;
	
	private String pid;
	
	private String text;
	
	private String sortGrade;
	
	private String state;

	private String url;

	private Map<String, Object> attributes = new HashMap<String, Object>(); // ��ӵ��ڵ���Զ�������

	private List<SortOperateTree> children; // 孩子节点

	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSortGrade() {
		return sortGrade;
	}

	public void setSortGrade(String sortGrade) {
		this.sortGrade = sortGrade;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<SortOperateTree> getChildren() {
		return children;
	}

	public void setChildren(List<SortOperateTree> children) {
		this.children = children;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
