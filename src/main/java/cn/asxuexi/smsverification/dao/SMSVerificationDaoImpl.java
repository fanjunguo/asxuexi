package cn.asxuexi.smsverification.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SMSVerificationDaoImpl implements SMSVerificationDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(SMSVerificationDaoImpl.class);
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getDateTimeAndCode(String tel) {
		String sql = "select telcode,gmt_create,gmt_modified from [smsverificationcode] where tel=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, tel);
		return list;
	}

	@Override
	public boolean insertSMSVerificationcode(String tel, String code, LocalDateTime now) {
		boolean success = false;
		String sql = "insert into [smsverificationcode] (tel, telcode , gmt_create) values (?,?,?)";
		int update = jdbcTemplate.update(sql, new Object[] { tel, code, now });
		if (update == 1) {
			success = true;
		}
		return success;
	}

	@Override
	public boolean updateSMSVerificationcode(String tel, String code, LocalDateTime now) {
		boolean success = false;
		String sql = "UPDATE [smsverificationcode] SET telcode = ? , gmt_modified = ? WHERE tel = ?";
		int update = jdbcTemplate.update(sql, new Object[] { code, now, tel });
		if (update == 1) {
			success = true;
		}
		return success;
	}

}
