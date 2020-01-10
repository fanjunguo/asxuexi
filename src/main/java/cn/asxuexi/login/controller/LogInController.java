package cn.asxuexi.login.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.entity.UserInfo;
import cn.asxuexi.login.service.LogInService;

@Controller
@RequestMapping("log_in/")
public class LogInController {

	@Resource
	private LogInService logInServiceImpl;
	
	//登录
	@RequestMapping("login.do")
	@ResponseBody
	public Map<String, Object> login(String tel, String password) {
		Map<String, Object> json = logInServiceImpl.login(tel, password);
		return json;
	}

	// 手机号码更新密码
	@RequestMapping("setNewPassWord.do")
	@ResponseBody
	public String setNewPassword(UserInfo login_as) {
		String log_in = logInServiceImpl.setNewPassword(login_as);
		return log_in;
	}
	
	/**
	 * @description 判断用户是否登录.请求的主要目的是触发拦截器判断登录.返回结果不代表什么意义
	 */
	@RequestMapping("isLogin.action")
	@ResponseBody
	public void isLogin(){
	}
}
