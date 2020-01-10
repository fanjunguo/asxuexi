package cn.asxuexi.homepage.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.homepage.service.ItemsService;

/**
 * @author fanjunguo
 * @version 2018年11月7日 下午8:49:06
 * @description 安卓客户端语言类似java,不好处理不规范的json数据.所以为安卓请求item数据按照标准json格式处理下数据
 */
@Controller
public class ItemsForAndroidController {
	@Resource
	private ItemsService service;
	
	@RequestMapping({ "itemsForAndroid/getItemsOfOrg.do" })
	@ResponseBody
	public Map<String, List<Map<String, Object>>> getItemsOfOrg(String cityId) {
		service.getItems("org",cityId);
		return service.getItemsForAndroid();
	}

	@RequestMapping({ "itemsForAndroid/getItemsOfCourse.do" })
	@ResponseBody
	public Map<String, List<Map<String, Object>>> getItemsOfCourse(String cityId) {
		service.getItems("course",cityId);
		return service.getItemsForAndroid();
	}
}
