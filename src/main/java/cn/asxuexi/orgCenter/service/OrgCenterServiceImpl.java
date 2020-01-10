package cn.asxuexi.orgCenter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.entity.PersonalMenu;
import cn.asxuexi.orgCenter.dao.OrgCenterDao;
import cn.asxuexi.orgInfo.dao.OrgInfoDao;
import cn.asxuexi.tool.PersonalMenuSort;

@Service
public class OrgCenterServiceImpl  implements OrgCenterService{

	@Resource
	private OrgCenterDao centerDao;
	@Resource
	private OrgInfoDao orgInfoDao;
	/**
	 * 注意 list中虽然是  personalMenu 但其中属性与机构一样
	 * @author 张顺\
	 * @return icon 代表是否跳转 0不跳转 1 跳转
	 * @作用 请求左菜单数据
	 * */
	public Map<String, Object> getOrgMenu() {
		PersonalMenuSort personalMenuSort = new PersonalMenuSort();
		List<Map<String, Object>> orgMenu = centerDao.getOrgMenu();
		List<PersonalMenu> fatherNode = personalMenuSort.getFatherNode(orgMenu);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgMenu", fatherNode);
		return map;
	}

}
