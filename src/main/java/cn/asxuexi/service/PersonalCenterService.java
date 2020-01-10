package cn.asxuexi.service;

import java.util.List;
import java.util.Map;

import cn.asxuexi.entity.PersonalMenu;

public interface PersonalCenterService {
	/**
	 * @author 张顺
	 * @作用 请求左菜单数据
	 * */
	List<PersonalMenu> getPersonalMenu();
	/**
	 * @author 张顺
	 * @作用 获取用户信息
	 * */
	Map<String, Object> getUserInfo();
}