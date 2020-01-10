package cn.asxuexi.smsverification.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.smsverification.dao.SMSVerificationDao;
import cn.asxuexi.tool.AliyunSMS;
import cn.asxuexi.tool.AliyunSMS.BusinessLimitControlException;
import cn.asxuexi.tool.GetRandomNumber;

@Service
public class SMSVerificationServiceImpl implements SMSVerificationService {

	@Resource
	private SMSVerificationDao sMSVerificationCodeDaoImpl;
	@Override
	public int getSMSVerificationCode(String tel) {
		AliyunSMS sender = new AliyunSMS();
		boolean isSent = false;
		int back = 0;//默认返回值为0，代表失败
		LocalDateTime now = LocalDateTime.now();//获取当前时间
		// 从数据表中获取对应手机号验证码和发送时间
		List<Map<String, Object>> list = sMSVerificationCodeDaoImpl.getDateTimeAndCode(tel);
		// 表中存在数据，说明给该手机号发送过消息
		if (1 == list.size()) {
			String code = (String) list.get(0).get("telcode");
			// 计算当前时刻和上次发送验证码时间的时间差（分钟为单位）
			Timestamp timestamp = (Timestamp) list.get(0).get("gmt_create");
			LocalDateTime lastTime = timestamp.toLocalDateTime();
			long minutesDifference = Duration.between(lastTime, now).toMinutes();
			Object obj = list.get(0).get("gmt_modified");
			if (null != obj) {
				timestamp = (Timestamp) obj;
				lastTime = timestamp.toLocalDateTime();
				minutesDifference = Duration.between(lastTime, now).toMinutes();
			}
			if (minutesDifference > 5) {
				// 时间差大于5分钟，生成新的验证码，存入数据库
				code = Integer.toString(GetRandomNumber.getRandomNumber(100000));
				boolean isUpdated = sMSVerificationCodeDaoImpl.updateSMSVerificationcode(tel, code, now);
				if (isUpdated) {
					// 保存成功后，发送该验证码
					try {
						isSent = sender.sendVerificationCode(tel, code);
						if (isSent) {
							// 发送成功，返回1
							back = 1;
						}
					} catch (BusinessLimitControlException e) {
						// 次数超过限制，返回2
						e.printStackTrace();
						back = 2;
					}
				}
			} else {
				// 时间差小于5分钟，发送原本的验证码
				try {
					isSent = sender.sendVerificationCode(tel, code);
					if (isSent) {
						// 发送成功，返回1
						back = 1;
					}
				} catch (BusinessLimitControlException e) {
					// 次数超过限制，返回2
					e.printStackTrace();
					back = 2;
				}
			}
		}
		// 表中未存在数据，说明没有给该手机号发送过消息
		if (0 == list.size()) {
			String code = Integer.toString(GetRandomNumber.getRandomNumber(100000));
			boolean isUpdated = sMSVerificationCodeDaoImpl.insertSMSVerificationcode(tel, code, now);
			if (isUpdated) {
				try {
					isSent = sender.sendVerificationCode(tel, code);
					if (isSent) {
						// 发送成功，返回1
						back = 1;
					}
				} catch (BusinessLimitControlException e) {
					// 次数超过限制，返回2
					e.printStackTrace();
					back = 2;
				}
			}
		}
		return back;
	}
}
