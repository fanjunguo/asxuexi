package cn.asxuexi.all.dao;

import java.util.List;
import java.util.Map;

public interface HomepageBannerDao {
/**
 *获取首页轮播图
 * @param cityId 城市id
 * @param time 时间 
 * **/
	List<Map<String, Object>> getImg(String cityId,long time);

}
