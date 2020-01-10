package cn.asxuexi.search.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SearchDao {
	/**
	 * 获得所有分类
	 * 
	 * @return
	 */
	List<Map<String, Object>> listSorts();

	/**
	 * 获得某个城市下的区县信息
	 * 
	 * @param cityId
	 *            城市ID
	 * @return
	 */
	List<Map<String, Object>> listAreas(@Param("cityId") String cityId);

}
