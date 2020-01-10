package cn.asxuexi.tool;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpenWechat {
	private static final String WECHAT_SNS_URL = "https://api.weixin.qq.com/sns/";
	private static final String APP_ID = "wxe60b803281a8a13a";
	private static final String APP_SECRET = "4b1e4f4cd8c9a17625e32addd8c4d027";
	private OkHttpClient client = new OkHttpClient();

	/**
	 * 从微信开放接口获取access_token
	 * @param code 授权码
	 * @return null或json对象 
	 * 正确的对象： 
	 * {"access_token":"ACCESS_TOKEN", 
	 * "expires_in":7200,
	 * "refresh_token":"REFRESH_TOKEN", 
	 * "openid":"OPENID", 
	 * "scope":"SCOPE"}
	 * 错误的对象： 
	 * {"errcode":40029, "errmsg":"invalid code"}
	 */
	public JSONObject getAccessToken(String code) {
		JSONObject parseObject = null;
		StringBuffer baseUrl = new StringBuffer(WECHAT_SNS_URL);
		String getAccessTokenUrl = baseUrl.append("oauth2/access_token?appid=").append(APP_ID).append("&secret=")
				.append(APP_SECRET).append("&code=").append(code).append("&grant_type=authorization_code").toString();
		Request request = new Request.Builder().url(getAccessTokenUrl).build();
		try {
			Response response = client.newCall(request).execute();
			parseObject = JSON.parseObject(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parseObject;
	}
	/**
	 * 从微信开放接口获取userinfo
	 * @param code 授权码
	 * @return null或json对象 
	 * 正确的对象： 
	 * {"openid":"OPENID",
	 * "nickname":"NICKNAME",
	 * "sex":1,
	 * "province":"PROVINCE",
	 * "city":"CITY",
	 * "country":"COUNTRY",
	 * "headimgurl": "http://wx.qlogo.cn/mmopen/g3M",
	 * "privilege":["PRIVILEGE1","PRIVILEGE2"],
	 * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"}
	 * 错误的对象： 
	 * {"errcode":40003,"errmsg":"invalid openid"}
	 */
	public JSONObject getUserInfo(String code) {
		JSONObject parseObject = null;
		JSONObject accessTokenObj = getAccessToken(code);
		if(null!=accessTokenObj){
			if (null!=accessTokenObj.get("access_token")) {
				String accessToken=accessTokenObj.getString("access_token");
				String openId=accessTokenObj.getString("openid");
				StringBuffer baseUrl = new StringBuffer(WECHAT_SNS_URL);
				String getUserInfoUrl = baseUrl.append("userinfo?access_token=").append(accessToken)
						.append("&openid=").append(openId).toString();
				Request request = new Request.Builder().url(getUserInfoUrl).build();
				try {
					Response response = client.newCall(request).execute();
					parseObject = JSON.parseObject(response.body().string());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return parseObject;
	}
}
