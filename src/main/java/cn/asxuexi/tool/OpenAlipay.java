package cn.asxuexi.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;

import cn.asxuexi.entity.AlipayUser;

public class OpenAlipay {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenAlipay.class);
	/**
	 * 
	 * @param authCode
	 * @return
	 */
	public String getUserId(String authCode) {
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.serverUrl, AlipayConfig.appId, AlipayConfig.privateKey, "json",
				"UTF-8", AlipayConfig.PublicKey, "RSA2");
		String userId = null;
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(authCode);
		request.setGrantType("authorization_code");
		try {
			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
			userId = oauthTokenResponse.getUserId();
		} catch (AlipayApiException e) {
			LOGGER.error("阿里API异常", e);
		}
		return userId;
	}

	public AlipayUser getUserInfoByAuthCode(String authCode) {
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.serverUrl, AlipayConfig.appId, AlipayConfig.privateKey, "json",
				"UTF-8", AlipayConfig.PublicKey, "RSA2");
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(authCode);
		request.setGrantType("authorization_code");
		try {
			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
			String accessToken = oauthTokenResponse.getAccessToken();
			AlipayUserInfoShareRequest request1 = new AlipayUserInfoShareRequest();
			AlipayUserInfoShareResponse response = alipayClient.execute(request1, accessToken);
			if (response.isSuccess()) {
				// 封装支付宝对象信息
				AlipayUser alipayUser = new AlipayUser();
				alipayUser.setCity(response.getCity());
				alipayUser.setProvince(response.getProvince());
				alipayUser.setNickName(response.getNickName());
				alipayUser.setUseId(response.getUserId());
				alipayUser.setAvatar(response.getAvatar());
				alipayUser.setGender(response.getGender());
				alipayUser.setIsCertified(response.getIsCertified());
				alipayUser.setIsStudentCertified(response.getIsStudentCertified());
				alipayUser.setUserType(response.getUserType());
				return alipayUser;
			}
		} catch (Exception e) {
			LOGGER.error("获取用户信息抛出异常！", e);
		}
		return null;
	}

}
