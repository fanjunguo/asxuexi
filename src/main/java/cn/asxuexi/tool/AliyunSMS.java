package cn.asxuexi.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class AliyunSMS {
	private final static String ALIYUN_SMS_ACCESS_KEY_ID = "LTAIOkwgK9qAKuOy"; // 阿里云 accessKeyId
	private final static String ALIYUN_SMS_ACCESS_KEY_SECRET = "O8frhuGj4QTCPoJW3FgoYtQoPSVgdA"; // 阿里云 accessKeySecret
	private final static String ALIYUN_SMS_PRODUCT = "Dysmsapi"; // 短信API产品名称（短信产品名固定，无需修改）
	private final static String ALIYUN_SMS_DOMAIN = "dysmsapi.aliyuncs.com"; // 阿里云调用短信服务平台接口域名（接口地址固定，无需修改）
	private final static String ALIYUN_SMS_REGION_ID = "cn-hangzhou"; // 阿里云regionId，暂时不支持多region（请勿修改）
	private final static String ALIYUN_SMS_END_POINT_NAME = "cn-hangzhou"; // 阿里云end_point_name（请勿修改）
	private final static Logger LOGGER = LoggerFactory.getLogger(AliyunSMS.class);// log4j，打印错误
	
	public class BusinessLimitControlException extends Exception {

		private static final long serialVersionUID = 1L;
		BusinessLimitControlException(String message) {
			super(message);
		}
	}
	public boolean sendVerificationCode(String tel, String code) throws BusinessLimitControlException {
		boolean success = false;
		/*
		 * 设置超时时间 此处使用httpURLConnection对外发出请求，httpURLConnetcion
		 * 的运行原理是通过底层socket通信实现的，则必须设置超时时间（timeout），防止 在网络异常情况下，程序僵死而不往下执行；
		 */
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化ascClient,暂时不支持多region（请勿修改）
		try {
			IClientProfile profile = DefaultProfile.getProfile(ALIYUN_SMS_REGION_ID, ALIYUN_SMS_ACCESS_KEY_ID,
					ALIYUN_SMS_ACCESS_KEY_SECRET);
			DefaultProfile.addEndpoint(ALIYUN_SMS_END_POINT_NAME, ALIYUN_SMS_REGION_ID, ALIYUN_SMS_PRODUCT,
					ALIYUN_SMS_DOMAIN);
			IAcsClient acsClient = new DefaultAcsClient(profile);
			// 组装请求对象
			SendSmsRequest request = new SendSmsRequest();
			// 设置请求方法（POST）
			request.setMethod(MethodType.POST);
			// 填写送验证码的手机
			request.setPhoneNumbers(tel);
			// 短信签名，在阿里云控制台--短信服务上设置
			// TODO 发布时改为“爱上学发习网”
			request.setSignName("爱上学习网");
			// 短信模板，在阿里云控制台上设置，即为发送的短信内容模板（仅验证码为变量）
			request.setTemplateCode("SMS_140650104");
			// 将生成的code赋值给模板中的${code}
			request.setTemplateParam("{\"code\":" + code + "}");
			// 如下为验证短信发送是否成功
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			String responseCode = sendSmsResponse.getCode();
			if (null != responseCode && "OK".equals(responseCode)) {
				success = true;
			} else if ("isv.BUSINESS_LIMIT_CONTROL".equals(responseCode)) {
				//默认流控：短信验证码 ：使用同一个签名，对同一个手机号码发送短信验证码，支持1条/分钟，5条/小时 ，累计10条/天。可修改
				throw new BusinessLimitControlException("主动抛出异常，短信发送数量超过限制");
			} else {
				LOGGER.info("短信发送失败，阿里云短信接口调用错误码为：" + responseCode);
			}
		} catch (ServerException e) {
			LOGGER.error("阿里云服务端错误", e);
		} catch (ClientException e) {
			LOGGER.error("爱上学习网客户端错误", e);
		} catch (BusinessLimitControlException e) {
			throw e;
		}
		return success;
	}
}
