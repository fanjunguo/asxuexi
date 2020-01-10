package cn.asxuexi.wechat.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.asxuexi.tool.JsonData;
import cn.asxuexi.tool.RedisTool;
import cn.asxuexi.wechat.entity.WeixinJSSDKConfig;
import cn.asxuexi.wechat.tool.WeixinOfficialAccountClient;

@Service
public class WeixinJSSDKServiceImpl implements WeixinJSSDKService {
	@Autowired
	private RedisTool redisTool;

	@Override
	public JsonData getWeixinJSSDKConfig(String url) {
		if (url.matches("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]")) {
			// url参数正确，获取服务器存储的ticket票据
			String ticket = redisTool.getString("weixin_jsapi_ticket");
			if (ticket == null) {
				// 若服务器存储的ticket失效，向微信公众平台请求ticket
				ticket = WeixinOfficialAccountClient.getJSApiTicket();
				// 并将该ticket存入缓存
				redisTool.addString("weixin_jsapi_ticket", ticket, 7200L, TimeUnit.SECONDS);
			}
			String nonceStr = UUID.randomUUID().toString();
			String timestamp = Long.toString(System.currentTimeMillis() / 1000);
			String string1 = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", ticket, nonceStr,
					timestamp, url);
			String signature = DigestUtils.sha1Hex(string1);
			WeixinJSSDKConfig config = new WeixinJSSDKConfig();
			config.setAppId(WeixinOfficialAccountClient.getAppId());
			config.setNonceStr(nonceStr);
			config.setSignature(signature);
			config.setTimestamp(timestamp);
			return JsonData.success(config);
		} else {
			return JsonData.error("url错误");
		}
	}

}
