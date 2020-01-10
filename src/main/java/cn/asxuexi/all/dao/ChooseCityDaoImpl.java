package cn.asxuexi.all.dao;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChooseCityDaoImpl implements ChooseCityDao {

	@Resource
	JdbcTemplate template;
	@Override
	public List<Map<String, Object>> listProvinces() {
		String sql="select id,shortname from areas where parentid='100000' order by id";
		List<Map<String, Object>> list = template.queryForList(sql);
		
		return list;
	}
	@Override
	public List<Map<String, Object>>  listCities(String id) {
		String sql="select shortname,id from areas where parentid=?";
		List<Map<String, Object>> citylist = template.queryForList(sql, new Object[] {id});
		return citylist;
	}
	@Override
	//获取城市首字母
	public List<String> listFirstChars() {
		String sqlForFirstchar="select distinct firstchar from areas order by firstchar";
		List<String> firstchar = template.queryForList(sqlForFirstchar, String.class);
		return firstchar;
	}
	
	//按字母获取城市
	@Override
	public List<Map<String, Object>> getCitylist(){
		String sqlForCitylist="select id,shortname as name,firstchar from areas where leveltype=2 order by firstchar";
		List<Map<String, Object>> citylist = template.queryForList(sqlForCitylist);
		return citylist;
	}
	
	@Override
	public List<Map<String, Object>> getCitylistForAndroid(){
		String sqlForCitylist="select id, name,firstchar from areas where leveltype=2 order by firstchar";
		List<Map<String, Object>> citylist = template.queryForList(sqlForCitylist);
		return citylist;
	}
	
	
	//获取热门城市top
	@Override
	public List<Map<String, Object>> listTopCities() {
		String sql="select top 10 cityid,cityname from top_city";		
		List<Map<String, Object>> topcity = template.queryForList(sql);
		return topcity;
	}
	
	/**
	 * 安卓获取热门城市数据
	 */
	@Override
	public List<Map<String, Object>> listTopCitiesForAndroid() {
		String sql="select top 10 cityid,name as cityname from top_city";		
		List<Map<String, Object>> topcity = template.queryForList(sql);
		return topcity;
	}
	
	
	@Override
	public List<Map<String, Object>> listTopCitiesForH5() {
		String sql="select top 10 cityid as id,name from top_city";		
		List<Map<String, Object>> topcity = template.queryForList(sql);
		return topcity;
	}
	
	//查询areas表中,所有的数据
	@Override
	public List<Map<String, Object>> getAreasOfJson() {
		String sql="select id,parentid,leveltype,name from areas where leveltype>0";
		List<Map<String, Object>> areas = template.queryForList(sql);
		return areas;
	}

}

