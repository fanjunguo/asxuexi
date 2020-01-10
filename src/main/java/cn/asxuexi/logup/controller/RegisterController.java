package cn.asxuexi.logup.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.entity.UserInfo;
import cn.asxuexi.logup.service.RegisterService;
import cn.asxuexi.service.AccountIsExistedService;

@Controller
@RequestMapping("register/")
public class RegisterController {

	@Resource
	private RegisterService registerService;

	@Resource
	private AccountIsExistedService accountIsExistedService;

	// 存入注册信息
	@ResponseBody
	@RequestMapping("register.do")
	public Map<String, Object> logup(UserInfo logupUser,HttpServletResponse response) {
		Map<String, Object> json = registerService.logup(logupUser,response);
		return json;
	}

	// 验证手机号是否已被注册，若已注册返回值为false
	@ResponseBody
	@RequestMapping("telIsExist.do")
	public String telIsRegisted(String tel) {
		String exist = "false";
		String accountIsExisted = accountIsExistedService.accountIsExisted(tel, "tel");
		if (accountIsExisted.equals("0")) {
			exist = "true";
		}
		return exist;
	}

	// 验证手机号是否已被注册
	@ResponseBody
	@RequestMapping("telIsExisted.do")
	public String telIsExisted(String tel) {
		String exist = "false";
		String count = accountIsExistedService.countTelOfUser(tel);
		if (!"0".equals(count)) {
			exist = "true";
		}
		return exist;
	}

	// 确认验证码是否匹配
	@RequestMapping("codeIsMatched.do")
	@ResponseBody
	public String codeIsMatched(String telcode, String tel) {
		if ("".equals(tel)) {
			return "false";
		} else {
			return registerService.codeIsMatched(telcode, tel);
		}
	}
}
