package cn.asxuexi.android.dao;

import java.util.Map;

/**
 * @author fanjunguo
 * @version 2018年9月20日 下午4:43:06
 * @description 
 */
public interface AppLocationDao {

	String getCityId(String cityname);
	
	Map<String, Object> getCityInfo(String cityname);

}