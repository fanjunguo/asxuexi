package cn.asxuexi.mvc;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.entity.PersonalMenu;
import cn.asxuexi.service.PersonalCenterService;

@Controller
public class PersonalCenterController {
	@Resource
	private PersonalCenterService personalCenterService;
	/**
	 * @author 张顺
	 * @作用 请求左菜单数据
	 * */
	@RequestMapping("asxuexi/getPersonalMenu.do")
	@ResponseBody
	public List<PersonalMenu> getPersonalMenu() {
		List<PersonalMenu> personal_sort = personalCenterService.getPersonalMenu();
		return personal_sort;
	}
	/**
	 * @author 张顺
	 * @作用 获取用户的图片和用户名
	 * */
	@RequestMapping("asxuexi/getUserInfo.action")
	@ResponseBody
	public Map<String, Object> getUserInfo() {
		Map<String, Object> information = personalCenterService.getUserInfo();
		return information;
	}
}
