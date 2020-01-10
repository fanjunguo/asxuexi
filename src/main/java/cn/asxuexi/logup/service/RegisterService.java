package cn.asxuexi.logup.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import cn.asxuexi.entity.UserInfo;

public interface RegisterService {

	Map<String, Object> logup(UserInfo logupUser,HttpServletResponse response);

	/**
     * @param telcode 验证码
     * @param tel 手机号
     * @return true 验证码正确  false 验证码错误
     * 
     * */
	String codeIsMatched(String telcode, String tel);



}