package cn.asxuexi.android.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.android.service.AppLocationService;

/**
 * @author fanjunguo
 * @version 2018年9月20日 下午4:21:11
 * @description 用于App端定位城市后，获取对应的城市id
 */
@Controller
public class AppLocationController {
	@Resource
	AppLocationService service;
	
	@RequestMapping(value="AppLocation/getCityId.do")
	@ResponseBody
	public String getCityId(String cityname) {
		String cityId = service.getCityId(cityname);
		return cityId;
		
	}
}
