package cn.asxuexi.orgCenter.service;

import java.util.Map;


public interface OrgCenterService {
	/**
	 * 注意 list中虽然是  personalMenu 但其中属性与机构一样
	 * @author 张顺
	 * @作用 请求机构菜单数据
	 * */
	Map<String, Object> getOrgMenu();
}
