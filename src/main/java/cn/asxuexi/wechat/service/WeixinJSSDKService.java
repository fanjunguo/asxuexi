package cn.asxuexi.wechat.service;

import cn.asxuexi.tool.JsonData;

public interface WeixinJSSDKService {
	/**
	 * 获得微信js_sdk配置信息
	 * 
	 * @param url 调用页面的url，不包含#及其后面部分
	 * @return
	 */
	JsonData getWeixinJSSDKConfig(String url);

}
