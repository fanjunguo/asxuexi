package cn.asxuexi.orgCenter.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrgCenterDaoImpl  implements OrgCenterDao{
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**
	 * @author 张顺
	 * @作用 请求左菜单数据
	 * */
	public List<Map<String, Object>> getOrgMenu(){
		String sql="SELECT  id, name, url, parent_id as parent FROM  org_menu ORDER BY id";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
		return queryForList;
	 }
}
