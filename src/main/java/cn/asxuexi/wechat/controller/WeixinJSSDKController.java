package cn.asxuexi.wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.asxuexi.tool.JsonData;
import cn.asxuexi.wechat.service.WeixinJSSDKService;

@RestController
public class WeixinJSSDKController {
	@Autowired
	private WeixinJSSDKService weixinJSSDKService;

	@GetMapping("getWeixinJSSDKConfig.do")
	public JsonData getWeixinJSSDKConfig(String url) {
		return weixinJSSDKService.getWeixinJSSDKConfig(url);
	}
}
