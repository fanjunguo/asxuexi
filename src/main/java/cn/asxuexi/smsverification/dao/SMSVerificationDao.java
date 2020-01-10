package cn.asxuexi.smsverification.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

public interface SMSVerificationDao {

	List<Map<String, Object>> getDateTimeAndCode(String tel);

	boolean insertSMSVerificationcode(String tel, String code, LocalDateTime now);

	boolean updateSMSVerificationcode(String tel, String code, LocalDateTime now);
}
