package cn.asxuexi.person.entity;
/**
 * 上课人实体类
 *
 * @author fanjunguo
 * @version 2019年6月3日 下午5:36:44
 */

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.NotEmpty;


public class StudentEntity {

	private String id; //上课人id
	private String userId; 
	@javax.validation.constraints.NotNull @NotEmpty
	private String name;
	@javax.validation.constraints.NotNull @NotEmpty
	private String tel;
	private LocalDateTime gmt_created;
	private LocalDateTime gmt_modefied;


	public StudentEntity() {
		super();
	}

	public StudentEntity(String id, String name, String tel, String userId, LocalDateTime gmt_created,
			LocalDateTime gmt_modefied) {
		super();
		this.id = id;
		this.name = name;
		this.tel = tel;
		this.userId = userId;
		this.gmt_created = gmt_created;
		this.gmt_modefied = gmt_modefied;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public LocalDateTime getGmt_created() {
		return gmt_created;
	}

	public void setGmt_created(LocalDateTime gmt_created) {
		this.gmt_created = gmt_created;
	}

	public LocalDateTime getGmt_modefied() {
		return gmt_modefied;
	}

	public void setGmt_modefied(LocalDateTime gmt_modefied) {
		this.gmt_modefied = gmt_modefied;
	}
	
}
