package cn.asxuexi.wechat.tool;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class WeixinOfficialAccountClient {
	private final static String APP_ID = "wx6de372d97ccf6fb8";
	private final static String APP_SECRET = "31e7ecbe767271b0b8b1475ee1f754a7";
	private final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	private final static String JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
	private final static Logger LOG = LoggerFactory.getLogger(WeixinOfficialAccountClient.class);
	private static OkHttpClient okHttpClient;

	@Autowired(required = true)
	public void setOkHttpClient(OkHttpClient okHttpClient) {
		WeixinOfficialAccountClient.okHttpClient = okHttpClient;
	}

	public static String getAccessToken() {
		String accessToken = null;
		String url = String.format("%s?grant_type=%s&appid=%s&secret=%s", TOKEN_URL, "client_credential", APP_ID,
				APP_SECRET);
		Request request = new Request.Builder().url(url).get().build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			JSONObject responseBody = JSON.parseObject(response.body().string());
			accessToken = responseBody.getString("access_token");
			LOG.debug("accessToken: {}",responseBody.toJSONString());
		} catch (IOException e) {
			LOG.error("获取微信access_token时发生异常", e);
		}
		return accessToken;
	}

	public static String getJSApiTicket() {
		String ticket = null;
		String accessToken = getAccessToken();
		String url = String.format("%s?access_token=%s&type=jsapi", JS_API_TICKET_URL, accessToken);
		Request request = new Request.Builder().url(url).get().build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			JSONObject responseBody = JSON.parseObject(response.body().string());
			LOG.debug("JSApiTicket: {}",responseBody.toJSONString());
			ticket = responseBody.getString("ticket");
		} catch (IOException e) {
			LOG.error("获取微信jsapi_ticket时发生异常", e);
		}
		return ticket;
	}

	public static String getAppId() {
		return APP_ID;
	}
}
