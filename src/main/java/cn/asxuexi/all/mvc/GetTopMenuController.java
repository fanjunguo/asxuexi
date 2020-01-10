package cn.asxuexi.all.mvc;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.all.service.GetTopMenuService;
/**
 * 
 * @author fanjunguo
 * @version 2018年9月13日 上午11:16:42
 * @description 加载顶部导航菜单
 */

@Controller
public class GetTopMenuController {

	@Resource
	private GetTopMenuService service;
	
	@RequestMapping(value="getTopMenu/getTopMenu.do")
	@ResponseBody
	public List<Map<String, Object>> getTopMenu(){
		List<Map<String, Object>> list = service.getmenu();
		return list;
	}
}
