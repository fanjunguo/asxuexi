package cn.asxuexi.orgInfo.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import cn.asxuexi.orgInfo.entity.OrgInfo;

public interface OrgInfoDao {
	/**
	 * 查询机构信息
	 * 
	 * @param orgId
	 *            {@link String} 机构id
	 * @return List<Map<String, Object>>
	 */
	Map<String, Object> getOrgInformation(String orgId);

	/**
	 * 查询logo
	 * 
	 * @param orgId
	 *            {@link String} 机构id
	 * @return Map<String, Object>
	 */
	Map<String, Object> getLogo(String orgId);

	/**
	 * 获取地址的下一级省市
	 * 
	 * @param parentname
	 *            {@link String} id 父name
	 * @param level
	 *            父级等级
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> listAreasName(String parentname, String level);

	/**
	 * 获取地址的下一级省市
	 * 
	 * @param parentId
	 *            {@link String} id 父id
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> listAreas(String parentId);

	/**
	 * 增加logo
	 * 
	 * @param orgId
	 *            {@link String} 机构id
	 * @param name
	 *            {@link String} 图片地址
	 * 
	 */
	int insertLogo(String orgId, String id, String name, LocalDateTime gmt_create);

	/**
	 * 更改logo
	 * 
	 * @param name
	 *            {@link String} 图片名称
	 * @param orgId
	 *            {@link String} 机构id
	 * @param now
	 *            {@link LocalDateTime} 现在时间
	 * @return {@link Integer}
	 */
	int updateLogo(String name, String orgId, LocalDateTime now);

	/**
	 * 更改机构信息
	 * 
	 * @param orgId
	 *            机构id
	 * @param orgInfo
	 *            机构信息
	 * @return int
	 */
	int updateOrgInfo(String orgId, OrgInfo orgInfo, LocalDateTime now);

	/**
	 * 
	 * @author fanjunguo
	 * @description 根据机构id查询机构名称
	 * @param orgId
	 * @return json数据
	 */
	Map<String, Object> getOrgName(String orgId);

	/**
	 * 获取身份证认证状态
	 * 
	 * @param orgId
	 *            机构id
	 * @return 机构id对应的整条记录，若没有记录返回空的map
	 */
	Map<String, Object> getIdentityAuthenticationStatus(String orgId);

	/**
	 * 保存身份证信息
	 * 
	 * @param orgId
	 *            机构ID
	 * @param name
	 *            姓名
	 * @param idNumber
	 *            身份证号
	 * @param expiryDateTime
	 *            有效期截止日期
	 * @param gmtCreate
	 *            创建时间
	 * @return
	 */
	int insertIdentityInfo(String orgId, String name, String idNumber, LocalDateTime expiryDateTime,
			LocalDateTime gmtCreate);

	/**
	 * 更新身份证信息
	 * 
	 * @param orgId
	 *            机构ID
	 * @param name
	 *            姓名
	 * @param idNumber
	 *            身份证号
	 * @param expiryDateTime
	 *            有效期截止日期
	 * @param gmtModified
	 *            更改时间
	 * @param gmtKey
	 *            需要更新的日期字段，gmt_create或gmt_modified
	 * @param updateKey
	 *            需要更新的状态字段，由front_,back_,person_中任意个组合而成的字符串
	 * @return
	 */
	int updateIdentityInfo(String orgId, String name, String idNumber, LocalDateTime expiryDateTime,
			LocalDateTime gmtTime, String gmtKey, String updateKey);

	/**
	 * 获取营业执照认证状态
	 * 
	 * @param orgId
	 *            机构id
	 */
	Map<String, Object> getLicenceAuthenticationStatus(String orgId);

	/**
	 * 保存营业执照信息
	 * 
	 * @param orgId
	 *            机构Id
	 * @param licenceNumber
	 *            营业执照号码
	 * @param companyName
	 *            注册名称
	 * @param companyAddress
	 *            注册地址
	 * @param legalRepresentative
	 *            法定代表人
	 * @param now
	 *            当前时间
	 * @return
	 */
	int insertLicenceInfo(String orgId, String licenceNumber, String companyName, String companyAddress,
			String legalRepresentative, LocalDateTime now);

	/**
	 * 修改营业执照信息
	 * 
	 * @param licenceNumber
	 *            营业执照号码
	 * @param companyName
	 *            注册名称
	 * @param companyAddress
	 *            注册地址
	 * @param legalRepresentative
	 *            法定代表人
	 * @param gmtKey
	 *            需要更新的日期字段，gmt_create或gmt_modified
	 * @param now
	 *            当前时间
	 * @param orgId
	 *            机构Id
	 * @return
	 */
	int updateLicenceInfo(String licenceNumber, String companyName, String companyAddress, String legalRepresentative,
			String gmtKey, LocalDateTime now, String orgId);

	/**
	 * 根据菜单ID和机构ID获得对应的后台管理人员ID列表
	 * 
	 * @param orgId
	 *            机构ID
	 * @param menuId
	 *            后台菜单ID
	 * @return
	 */
	List<String> getStaffIdList(String orgId, String menuId);

}
