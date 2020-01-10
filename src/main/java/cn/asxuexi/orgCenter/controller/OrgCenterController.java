package cn.asxuexi.orgCenter.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.orgCenter.service.OrgCenterService;
@Controller
public class OrgCenterController {
	@Resource
	private OrgCenterService centerService;
	/**
	 * 注意 list中虽然是  personalMenu 但其中属性与机构一样
	 * @author 张顺
	 * @作用 请求机构菜单数据
	 * */
	@RequestMapping("OrgCenter/getOrgMenu.action")
	@ResponseBody
	public Map<String, Object> getOrgMenu() {
		 Map<String, Object> orgMenu = centerService.getOrgMenu();
		return orgMenu;
	}
}
