package cn.asxuexi.login.service;

import java.util.Map;

import cn.asxuexi.entity.UserInfo;

public interface LogInService {


	String setNewPassword(UserInfo login_as);

	Map<String, Object> login(String tel, String password);

}