package cn.asxuexi.all.mvc;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.all.service.NavigationService;

@Controller
public class NavigationController {
	@Resource
	private NavigationService navigationService;

	/**
	 * 请求左菜单
	 * @author 张顺
	 * @return Map<String, Object> key: rowcount,sortData
	 */
	@RequestMapping("Navigation/getLeftNav.do")
	@ResponseBody
	public Map<String, Object> getLeftNav() {
		Map<String, Object> nav_left_selecta = navigationService.getLeftNav();
		return nav_left_selecta;
	}

	/**
	 * 请求上部菜单
	 * 
	 * @author 张顺
	 * @param cityId 不能为空
	 * @return Map<String, Object> key: sortData
	 */
	@RequestMapping("Navigation/getTopNav.do")
	@ResponseBody
	public Map<String, Object> getTopNav(String cityId) {
		Map<String, Object> nav_top_select = navigationService.getTopNav(cityId);
		return nav_top_select;
	}

}
