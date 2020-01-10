package cn.asxuexi.all.dao;

import java.util.List;
import java.util.Map;

public interface ChooseCityDao {

	List<Map<String, Object>> listProvinces();

	List<Map<String, Object>> listCities(String id);


	List<String> listFirstChars();

	List<Map<String, Object>> listTopCities();


	List<Map<String, Object>> getAreasOfJson();


	/**
	 * @author fanjunguo
	 * @description 
	 * @return
	 */
	List<Map<String, Object>> getCitylist();

	/**
	 * @author fanjunguo
	 * @description 安卓请求所有城市数据
	 * @return
	 */
	List<Map<String, Object>> getCitylistForAndroid();

	/**
	 * @author fanjunguo
	 * @description 安卓请求热门城市数据
	 * @return
	 */
	List<Map<String, Object>> listTopCitiesForAndroid();

	/**
	 * @author fanjunguo
	 * @description 
	 * @return
	 */
	List<Map<String, Object>> listTopCitiesForH5();

}