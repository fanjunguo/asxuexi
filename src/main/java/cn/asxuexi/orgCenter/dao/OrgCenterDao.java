package cn.asxuexi.orgCenter.dao;

import java.util.List;
import java.util.Map;

public interface OrgCenterDao {
	/**
	 * @author 张顺
	 * @作用 请求机构菜单数据
	 * */
	  List<Map<String, Object>> getOrgMenu();
}
