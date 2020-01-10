package cn.asxuexi.all.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.asxuexi.all.entity.TopNav;
import cn.asxuexi.android.SortOperate;

@Repository
public class NavigationDaoImpl implements NavigationDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	

	/**
	 * 获取分类
	 * 
	 * @author 张顺
	 * @return List<Map<String, Object>>
	 */
	@Override
	public List<SortOperate> listSorts() {
		String sql = "SELECT sort_id, sort_parentid, sort_name, sort_grade FROM  dbo.sort where sort_grade<4 order by rows ";
		List<SortOperate> query = jdbcTemplate.query(sql, new Object[] {},
				new BeanPropertyRowMapper<SortOperate>(SortOperate.class));
		return query;
	}

	/**
	 * 获取左菜单行数
	 * 
	 * @author 张顺
	 * @return int
	 */
	@Override
	public int countLeftNavRows() {
		String sql = "select count (*) from sort where sort_grade=1";
		int queryForInt = jdbcTemplate.queryForObject(sql, Integer.class).intValue();
		return queryForInt;

	}

	/**
	 * 请求上侧导航数据
	 * 
	 * @author 张顺
	 * @param cityId {@link String} 城市id
	 * @return List<TopNav>
	 */
	@Override
	public List<TopNav> getTopNav(String cityId) {
		String sql = "SELECT TOP 10 nav_top_id,nav_top_name,nav_top_href, img"
				+ " FROM top_nav where city_id=? and nav_top_row!=0 order by nav_top_row";
		List<TopNav> query = jdbcTemplate.query(sql, new Object[] {cityId}, new BeanPropertyRowMapper<TopNav>(TopNav.class));
		return query;
	}

	@Override
	public List<Map<String, Object>> getChildrenSort(String pid) {
		String sql="select sort_id as sortId, sort_name as sortName from sort where sort_parentid=?";
		return jdbcTemplate.queryForList(sql, pid);
	}
}
