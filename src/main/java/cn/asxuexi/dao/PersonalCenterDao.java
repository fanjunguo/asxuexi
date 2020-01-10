package cn.asxuexi.dao;

import java.util.List;
import java.util.Map;

public interface PersonalCenterDao {
	/**
	 * @author 张顺
	 * @作用 请求左菜单数据
	 * */
	List<Map<String, Object>> listPersonalMenu();
	/**
	 * @author 张顺
	 * @param id 用户编号
	 * @return map 单独一条用户信息
	 * */
	Map<String, Object> getUserInfo(String id);
}