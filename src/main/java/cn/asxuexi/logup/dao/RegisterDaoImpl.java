package cn.asxuexi.logup.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.asxuexi.entity.UserInfo;

@Repository
public class RegisterDaoImpl implements RegisterDao {


	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public String insertUserInfo(UserInfo login_as) {
		String back = "0";
		LocalDateTime now = LocalDateTime.now();
		String sql = "insert into [user] (id,name,tel,password,email,gmt_create) values (?,?,?,?,?,?)";
		int update = jdbcTemplate.update(sql,
				new Object[] { login_as.getId(),login_as.getName(), login_as.getTel(), login_as.getPassword(), login_as.getEmail(), now });
		if (1 == update) {
			back = "1";
		}
		return back;
	}
	@Override
	public List<Map<String, Object>> getDateTimeAndCode(String tel) {
		String sql = "select telcode,gmt_create,gmt_modified from [smsverificationcode] where tel=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, tel);
		return list;
	}
}
