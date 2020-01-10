package cn.asxuexi.all.mvc;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.all.service.HomepageBannerService;

@Controller
public class HomepageBannerController {
	@Resource
	private HomepageBannerService service;
	
	/**
	 * @author fanjunguo
	 * @description 获取轮播图数据
	 * @param cityId 城市id,不能为空
	 * @return
	 */
	@RequestMapping(value="homepageBanner/getImg.do")
	@ResponseBody
	public List<Map<String, Object>> getImg(String cityId){
		List<Map<String, Object>> imgs = service.getImg(cityId);
		return imgs;
		
	}

}
