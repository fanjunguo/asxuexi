package cn.asxuexi.all.dao;

import java.util.List;
import java.util.Map;

import cn.asxuexi.all.entity.TopNav;
import cn.asxuexi.android.SortOperate;

public interface NavigationDao {

	/**
	 * 获取分类
	 * 
	 * @author 张顺
	 * @return List<Map<String, Object>>
	 */
	List<SortOperate> listSorts();

	/**
	 * 获取左菜单行数
	 * 
	 * @author 张顺
	 * @return int
	 */
	int countLeftNavRows();

	/**
	 * 请求上侧导航数据
	 * 
	 * @author 张顺
	 * @param cityId {@link String} 城市id
	 * @return List<TopNav>
	 */
	List<TopNav> getTopNav(String cityId);

	/**
	 * 根据父级分类ID,查询其所有的子类
	 * 
	 */
	List<Map<String, Object>> getChildrenSort(String pid);
}