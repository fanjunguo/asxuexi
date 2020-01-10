package cn.asxuexi.all.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.asxuexi.all.dao.NavigationDao;
import cn.asxuexi.all.entity.TopNav;
import cn.asxuexi.android.SortOperate;
import cn.asxuexi.android.SortOperateTree;
import cn.asxuexi.android.TreeSort;

@Service
public class NavigationServiceImpl implements NavigationService {
	@Resource
	private NavigationDao navigationDao;
	@Autowired
	private HttpServletRequest request;

	/**
	 * 请求左菜单
	 * 
	 * @author 张顺
	 * @return Map<String, Object> key: rowcount,sortData
	 */
	@Override
	public Map<String, Object> getLeftNav() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 请求行数
		int count = navigationDao.countLeftNavRows();
		// 请求分类
		List<SortOperate> secend = navigationDao.listSorts();
//		形成树状结构
		List<SortOperateTree> sortOperateTree  = TreeSort.getFatherNode(secend);
		map.put("rowcount", count);
		map.put("sortData", sortOperateTree);
		return map;
	}
	
	
	/**
	 * 请求上菜单
	 * 
	 * @author 张顺
	 * @return Map<String, Object> key:topNavData
	 */
	@Override
	public Map<String, Object> getTopNav(String cityId) {
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+ "/";
		//2019.8.7 为保证各个城市内容和图片都展示,暂时写死,所有都城市都用东营的top-nav
		cityId="370500";
//		if(cityId==null||"".equals(cityId)) {
//			cityId="370500";
//		}
		List<TopNav> nav_top_select = navigationDao.getTopNav(cityId);
		for (TopNav nav_top_test : nav_top_select) {
			//拼接字符串
			String nav_top_href = nav_top_test.getNav_top_href();
			String string = basePath + nav_top_href;
			nav_top_test.setNav_top_href(string);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("topNavData", nav_top_select);
		return map;
	}
}
