package cn.asxuexi.all.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HomepageBannerDaoImpl implements HomepageBannerDao {
	@Resource
	private JdbcTemplate jdbcTemplate;
/**
 * 执行[存储过程]topbanner  获取首页轮播图
 * topbanner 有两个参数  必须传递
 * @author 张顺
 * @param cityId 城市id
 * @param time 时间
 * */
	@Override
	public List<Map<String, Object>> getImg(String cityId,long time) {
		String sql="exec topbanner ?,?";  //这个是执行数据库的[存储过程]
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,new Object[] {cityId,time});
		return list;
	}

}
