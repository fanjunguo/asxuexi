package cn.asxuexi.all.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class GetTopMenuDaoImpl implements GetTopMenuDao {
	@Resource
	private JdbcTemplate template;
	
	@Override
	public List<Map<String, Object>> getmenu(){
		String sql="select name,href from top_menu";
		List<Map<String, Object>> list = template.queryForList(sql);
		return list;
	}
	
	@Override
	public Map<String, Object> getTelAndName(String userid) {
		String sql="select tel,name from [user] where id=?";
		Map<String, Object> map = template.queryForMap(sql, new Object[] {userid});
		return map;
	}
}
