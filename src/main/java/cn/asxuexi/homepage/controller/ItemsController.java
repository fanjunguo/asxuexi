package cn.asxuexi.homepage.controller;

import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.homepage.service.ItemsService;

@Controller
@RequestMapping("/items")
public class ItemsController {
	@Resource
	private ItemsService itemsService;

	@RequestMapping({ "/getItemsOfOrg.do" })
	@ResponseBody
	public Map<String, Object> getItemsOfOrg(String cityId) {
		return itemsService.getItems("org",cityId);
	}

	@RequestMapping({ "/getItemsOfCourse.do" })
	@ResponseBody
	public Map<String, Object> getItemsOfCourse(String cityId) {
		return itemsService.getItems("course",cityId);
	}
	
	//暂行性推荐课程和机构的方案⬇
	
	/**
	 * 请求首页推荐课程
	 * 
	 * @author fanjunguo
	 * @param orderRule 排序规则:1-按时间倒序;2-按时间顺序;3-按价格排序
	 * @param cityId 城市ID
	 * @return
	 */
	@RequestMapping(value="/getCourseOfRecommended.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getCourseOfRecommended(int orderRule,String cityId){
		
		return itemsService. getCourseOfRecommended( orderRule,cityId);
	}
	
	/**
	 * 请求首页推荐课程
	 * 
	 * @param orderRule 排序规则:1-按时间倒序;2-按时间顺序
	 * @param cityId 城市ID
	 * @return
	 */
	@RequestMapping(value="/getOrgOfRecommended.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getOrgOfRecommended(int orderRule,String cityId){
		
		return itemsService. getOrgOfRecommended(orderRule,cityId);
	}
	
	
}
