package cn.asxuexi.OrgEnroll.service;

import java.util.List;
import java.util.Map;

public interface OrgEnrollService {
	/**
	 * @作用 获取所有省份 、当前城市的省份下的城市，当前城市下的县区
	 */
	Map<String, Object> address();

	/***
	 * @param parentId
	 *            父级id
	 * @作用 获取下一级城市
	 */
	List<Map<String, Object>> listAddress(String parentId);
	/**
	 * @param orgName
	 *            机构名称
	 * @param orgHead
	 *            机构负责人
	 * @param orgTel
	 *            机构电话
	 * @param loaction
	 *            机构所在县区
	 * @作用 保存信息
	 */
	 int insertOrgInfo( String orgName, String orgHead, String orgTel, String loacltion);
}
