package cn.asxuexi.android.service;

import java.util.Map;

/**
 * @author fanjunguo
 * @version 2018年9月20日 下午4:40:48
 * @description 
 */
public interface AppLocationService {

	String getCityId(String cityname);

	/**
	 * @author fanjunguo
	 * @description 根据城市名称,请求数据库中对应的城市id和城市短名称
	 * @param cityname
	 * @return map
	 */
	Map<String, Object> getCityInfo(String cityname);

}