package cn.asxuexi.all.service;

import java.util.List;
import java.util.Map;

public interface ChooseCityService {

	List<Map<String, Object>> listProvinces();

	List<Map<String, Object>> listCities(String id);

	List<String> listFirstChars();

	List<Map<String, Object>> listTopCities();

	/**
	 * @author fanjunguo
	 * @description 
	 * @return
	 */
	Map<String, List<Map<String, Object>>> getAllCityForAndroid();

	/**
	 * @author fanjunguo
	 * @return 
	 * @description 安卓获取热门城市
	 */
	Map<String, List<Map<String, Object>>> getTopCityForAndroid();

	/**
	 * @author fanjunguo
	 * @description 
	 * @return
	 */
	List<Map<String, Object>> getAreasOfJson();

	/**
	 * @author fanjunguo
	 * @description 
	 * @return
	 */
	Map<String, List<Map<String, Object>>> getCitylist();

	/**
	 * @author fanjunguo
	 * @description 
	 * @return
	 */
	Map<String, List<Map<String, Object>>> getCitylistForH5();


}