package cn.asxuexi.activity.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.activity.service.ActivityService;
import cn.asxuexi.tool.JsonData;

/**
 * 前台:广告/活动/推广
 *
 * @author fanjunguo
 * @version 2019年6月15日 上午10:28:53
 */
@Controller
public class ActivityController {

	@Resource
	private ActivityService service;
	
	/**
	 * 请求活动照片等信息
	 * 
	 * @param id
	 * @return 
	 * 	code: 600
		data: {
			gmt_created: 1560782646257
			id: "ad_1560782646256184"
			imgHeight: 1280
			imgWidth: 720
			name: "爱上学习网招商大会"
			url: "../asxuexi_resource/ad_img/ad_12314.png"
			show_form: true
		}
		message: "success"
	 */
	@RequestMapping("activity/getAdInfo.do")
	@ResponseBody
	public JsonData getAdInfo(@RequestParam(required=true)String id) {
		if (id.contains("ad")) {
			return service.getAdInfo(id);
		}else {
			return JsonData.result(402, "参数错误");
		}
		
	}
	
	/**
	 * 提交报名信息
	 * 
	 * @author fanjunguo
	 * @param name 姓名
	 * @param tel 电话
	 * @return 报名结果
	 */
	@RequestMapping("activity/submit.do")
	@ResponseBody
	public JsonData submit(@RequestParam(required=true)String id,@RequestParam(required=true)String name,@RequestParam(required=true)String tel) {
		
		return service.submit(id,name,tel);
	}
}
