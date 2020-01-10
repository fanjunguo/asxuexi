package cn.asxuexi.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class PersonalCenterDaoImpl implements PersonalCenterDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * @author 张顺
	 * @作用 请求左菜单数据
	 * */
	@Override
	public List<Map<String, Object>> listPersonalMenu() {
		String sql="SELECT  id, name, url, parent FROM  personal_menu ORDER BY id";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
		return queryForList;
	}
	/**
	 * @author 张顺
	 * @param id 用户编号
	 * @return map 单独一条用户信息
	 * */
	@Override
	public Map<String, Object> getUserInfo(String id) {
		String sql="SELECT name, photo  FROM  dbo.[user] WHERE (id = ?)";
		Map<String, Object> queryForMap = null;
		try {
			queryForMap=jdbcTemplate.queryForMap(sql,new Object[] {id});
		} catch (Exception e) {
			
		}
		
		return queryForMap;
	}
}
