package cn.asxuexi.logup.dao;

import java.util.List;
import java.util.Map;

import cn.asxuexi.entity.UserInfo;

public interface RegisterDao {

	String insertUserInfo(UserInfo login_as);

	List<Map<String, Object>> getDateTimeAndCode(String tel);
}