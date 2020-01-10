package cn.asxuexi.homepage.service;

import java.util.List;
import java.util.Map;

public interface ItemsService {

	Map<String, Object> getItems(String contentType,String cityId);

	/**
	 * @author fanjunguo
	 * @description 为安卓请求新增的方法
	 */
	Map<String, List<Map<String, Object>>> getItemsForAndroid();

	/**
	 * 请求首页推荐课程
	 * 
	 * @param orderRule 排序规则
	 * @return
	 */
	Map<String, Object> getCourseOfRecommended(int orderRule,String cityId);

	/**
	 * 请求首页推荐机构
	 * 
	 */
	Map<String, Object> getOrgOfRecommended(int orderRule, String cityId);

}
