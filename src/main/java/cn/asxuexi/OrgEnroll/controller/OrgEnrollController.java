package cn.asxuexi.OrgEnroll.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.asxuexi.OrgEnroll.service.OrgEnrollService;

@Controller
public class OrgEnrollController {

	@Resource
	private OrgEnrollService orgEnrollService;

	/**
	 * @作用 获取所有省份 、当前城市的省份下的城市，当前城市下的县区
	 */
	@ResponseBody
	@RequestMapping("OrgEnroll/address.action")
	public Map<String, Object> address() {
		Map<String, Object> address = orgEnrollService.address();
		return address;
	}

	/***
	 * @param parentId
	 *            父级id
	 * @作用 获取下一级城市
	 */
	@ResponseBody
	@RequestMapping("OrgEnroll/listAddress.do")
	public List<Map<String, Object>> listAddress(String parentId) {
		List<Map<String, Object>> listAddress = orgEnrollService.listAddress(parentId);
		return listAddress;
	}
/**
 * @param orgName 机构名称
 * @param orgHead 机构负责人
 * @param orgTel 机构电话
 * @param loaction 机构所在县区id
 * @return 
 * @作用 保存信息
 * */
	@ResponseBody
	@RequestMapping("OrgEnroll/insertOrgInfo.action")
	public int insertOrgInfo(String orgName,String orgHead,String orgTel ,String location) {
		int insertOrgInfo = orgEnrollService.insertOrgInfo(orgName, orgHead, orgTel, location);
		return insertOrgInfo;
	}
}
