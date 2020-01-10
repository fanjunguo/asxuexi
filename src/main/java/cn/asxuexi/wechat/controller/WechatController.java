package cn.asxuexi.wechat.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 *
 *
 * @author fanjunguo
 * @version 2019年5月7日 下午11:15:15
 */
@Controller
public class WechatController {

	@RequestMapping("wechat/getQrcode.do")
	@ResponseBody
	public String getQrcode(HttpServletResponse resp) {
		long timestamp=System.currentTimeMillis();
		String uuid=null;
		String urlStr="https://login.weixin.qq.com/jslogin?appid=wx782c26e4c19acffb&fun=new&lang=zh_CN&_="+timestamp;
		String returnStr = getUrlResp(urlStr); //返回格式:window.QRLogin.code = 200; window.QRLogin.uuid = "AbS8RE4Zsw==";
		if (returnStr.contains("200")) {
			uuid=returnStr.substring(returnStr.indexOf("\"")+1, returnStr.lastIndexOf("\""));
		}
		return uuid;
	}
	
	
	private void isLogin(String uuid) {
		long timestamp=System.currentTimeMillis();
		int tip=1;
		String url="https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login?uuid=XXXXXX&tip="+tip+"&_="+timestamp;
		String result = getUrlResp(url);
		System.out.println(result);
	}
	
	
	
	//解析微信接口,接受返回内容:字符串,不是json格式
	private String getUrlResp(String urlStr) {
		String returnStr=null;
		try {
			URL url=new URL(urlStr);
			URLConnection connection = url.openConnection();
			BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));  //为什么要用这个reader呢?不同的流有什么区别呢?需要研究一下
			returnStr=in.readLine();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}
}
