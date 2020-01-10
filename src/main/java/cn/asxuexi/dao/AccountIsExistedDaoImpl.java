package cn.asxuexi.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountIsExistedDaoImpl implements AccountIsExistedDao {
	
	
	private String back;
	private String sql;
	private String updatea;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public String countUser(String parameter,String flag) {
		if("tel".equals(flag)) {
		    sql = "select count(*) from [user] where tel="+parameter+"";
		}else {
			sql = "select user_id from [third] where id='"+parameter+"'";
			}
		try {
		updatea = jdbcTemplate.queryForObject(sql, String.class);
		}catch(Exception e) {
			updatea = "0";
		}
		back =updatea;
		return back;
	}

	@Override
	public String countTelOfUser(String tel) {
		String sql="SELECT COUNT(tel) FROM [user] WHERE tel=?";
		String count = jdbcTemplate.queryForObject(sql, String.class, new Object[]{tel});
		return count;
	}

	@Override
	public List<Map<String, Object>> getUserIdOfThird(String thirdId) {
		String sql="select user_id from [third] where id=?";
		List<Map<String, Object>> uesrIdList = jdbcTemplate.queryForList(sql, thirdId);
		return uesrIdList;
	}
	
	@Override
	public String countThirdIdOfThird(String userId,String thirdId) {
		String sql="SELECT COUNT(id) FROM [third] WHERE user_id=? and id like ?";
		String thirdIdType=thirdId.substring(0, thirdId.indexOf("_"));
		String count = jdbcTemplate.queryForObject(sql, String.class, new Object[]{userId,thirdIdType+"%"});
		return count;
	}
}
