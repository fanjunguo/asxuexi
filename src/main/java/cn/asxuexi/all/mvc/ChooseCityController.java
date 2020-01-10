package cn.asxuexi.all.mvc;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.all.service.ChooseCityService;


@Controller
public class ChooseCityController{
	@Resource
	ChooseCityService service;
	//获取省份
	@RequestMapping(value="chooseCity/getProvinces.do")
	@ResponseBody
	public List<Map<String, Object>> getProvinces() {
		 List<Map<String, Object>> list = service.listProvinces();
		return list;
		
	}
	/**
	 * 
	 * @author fanjunguo
	 * @description 获取城市（多级联动选择）
	 */
	@RequestMapping(value="chooseCity/getCities.do")
	@ResponseBody
	public List<Map<String, Object>> getCities(String id) {
		List<Map<String, Object>> citylist = service.listCities(id);
		
		return citylist;
	}
	
	//获取首字母列表
	@RequestMapping(value="chooseCity/getFirstChars.do")
	@ResponseBody
	public List<String> getFirstChars() {
		List<String> firstchar = service.listFirstChars();
		return firstchar;
	}
	
	
	//获取城市列表
	@RequestMapping(value="chooseCity/getCitylist.do")
	@ResponseBody
	public Map<String, List<Map<String, Object>>> getCitylist(){
		Map<String, List<Map<String, Object>>> city = service.getCitylist();
		return city;
	}
	
	//获取城市列表
	@RequestMapping(value="chooseCity/getCitylistForH5.do")
	@ResponseBody
	public Map<String, List<Map<String, Object>>> getCitylistForH5(){
		Map<String, List<Map<String, Object>>> city = service.getCitylistForH5();
		return city;
	}
	
	
	
	/**
	 * @description:安卓获取城市列表
	 * @return:标准json数据
	 */
	@RequestMapping(value="chooseCity/getCitylistForAndroid.do")
	@ResponseBody
	public Map<String, List<Map<String, Object>>> getCitylistForAndroid() {
		Map<String, List<Map<String, Object>>> json = service.getAllCityForAndroid();
		return json;
	}
	/**
	 * 
	 * @author fanjunguo
	 * @return 
	 * @description 安卓获取热门城市
	 * @return 标准json格式数据
	 */
	@RequestMapping(value="chooseCity/getTopCityForAndroid.do")
	@ResponseBody
	public Map<String, List<Map<String, Object>>> getTopCityForAndroid() {
		Map<String, List<Map<String, Object>>> json = service.getTopCityForAndroid();
		return json;
	}
	
	//获取热门城市top10
	@RequestMapping(value="chooseCity/getTopcities.do")
	@ResponseBody
	public List<Map<String, Object>> getTopcities() {
		List<Map<String, Object>> topcity = service.listTopCities();
		return topcity;
	}
	
	//安卓:获取区域3级数据,保存到客户端数据库,用于选择地区时3级联动
	@RequestMapping(value="chooseCity/getAreasOfJson.do")
	@ResponseBody
	public List<Map<String, Object>> getAreasOfJson() {
		List<Map<String, Object>> areasOfJson = service.getAreasOfJson();
		return areasOfJson;
	}
}
