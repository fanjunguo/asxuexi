package cn.asxuexi.all.service;

import java.util.Map;

public interface NavigationService {
	/**
	 * 请求左菜单
	 * 
	 * @author 张顺
	 * @return Map<String, Object> key: rowcount,sortData
	 */
	Map<String, Object> getLeftNav();

	/**
	 * 请求上菜单
	 * 
	 * @author 张顺
	 * @param cityId 
	 * @return Map<String, Object> key:topNavData
	 */
	Map<String, Object> getTopNav(String cityId);
}