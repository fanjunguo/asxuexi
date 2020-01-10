package cn.asxuexi.OrgEnroll.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrgEnrollDao {
	/**
	 * @ 作用 查询全国省份和城市
	 **/
	List<Map<String, Object>> listCity(String cityId);

	/**
	 * @param cityId
	 *            城市id
	 * @作用 获取城市所在的省份id
	 */
	Map<String, Object> getProvinceId(String cityId);

	/**
	 * @param cityId
	 *            城市id
	 * @作用 获取id下的城区
	 */
	List<Map<String, Object>> listAddress(String cityId);

	/**
	 * @param orgId 机构id
	 * @param orgName 机构名称
	 * @param orgHead 机构负责人
	 * @param orgTel 机构电话
	 * @param loacltion 机构所在县区
	 * @param userId 用户id
	 * @param now 现在的时间
	 * @作用 保存信息
	 * */
	int insertOrgInfo(String orgId, String orgName, String orgHead, String orgTel, String loacltion,String userId,LocalDateTime now);

	int insertOrgLogo(String logoId, String orgId, String logoName, LocalDateTime now);
	
	/**
	 * 维护用户-机构关系表(新增关系)
	 * 
	 * @author fanjunguo
	 * @param userId 用户ID
	 * @param orgId 新增的机构id
	 * @param isMainSchool 是否是主校
	 * @param now 时间
	 * @return
	 */
	int insertUserOrgRelationship(String userId,String orgId,boolean isMainSchool,LocalDateTime now);
}
