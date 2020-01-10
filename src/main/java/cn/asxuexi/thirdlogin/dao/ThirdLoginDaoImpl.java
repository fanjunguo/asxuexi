package cn.asxuexi.thirdlogin.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ThirdLoginDaoImpl implements ThirdLoginDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public String insertThirdId(String userId, String thirdId) {
		LocalDateTime now = LocalDateTime.now();
		String sql = "insert into dbo.[third](id ,user_id,gmt_create) values(?,?,?) ";
		int update = jdbcTemplate.update(sql, new Object[] { thirdId, userId, now });
		return Integer.toString(update);
	}

	@Override
	public String updateThirdId(String userId, String thirdId) {
		LocalDateTime now = LocalDateTime.now();
		String sql = "update dbo.[third] set id=?,gmt_modified=? where uesr_id=? and id like ? ";
		String thirdIdType = thirdId.substring(0, thirdId.indexOf("_"));
		int update = jdbcTemplate.update(sql, new Object[] { thirdId, now, userId, thirdIdType });
		return Integer.toString(update);
	}

	@Override
	public List<Map<String, Object>> getUserIdOfThird(String thirdId) {
		String sql = "select user_id from [third] where id=?";
		List<Map<String, Object>> uesrIdList = jdbcTemplate.queryForList(sql, thirdId);
		return uesrIdList;
	}

	@Override
	public String countThirdIdOfThird(String userId, String thirdId) {
		String sql = "SELECT COUNT(id) FROM [third] WHERE user_id=? and id like ?";
		String thirdIdType = thirdId.substring(0, thirdId.indexOf("_"));
		String count = jdbcTemplate.queryForObject(sql, String.class, new Object[] { userId, thirdIdType + "%" });
		return count;
	}
}
