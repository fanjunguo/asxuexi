package cn.asxuexi.smsverification.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.smsverification.service.SMSVerificationService;

@Controller
@RequestMapping("SMSVerificationCode/")
public class SMSVerificationController {

	@Resource
	private SMSVerificationService sMSVerificationCodeServiceImpl;
	// 发送手机验证码功能
	@RequestMapping("sendSMSVerificationCode.do")
	@ResponseBody
	public int getSMSVerificationCode(String tel) {
		int result = sMSVerificationCodeServiceImpl.getSMSVerificationCode(tel);
		return result;
	}
	
}
