package cn.asxuexi.smsverification.service;

/**
 * @author Jhon
 * @version v1
 */
public interface SMSVerificationService {
	/**
	 * @param 需要发送验证码的手机号
	 * @return 发送成功返回值为1，发送失败为0
	 */
	int getSMSVerificationCode(String tel);
}
