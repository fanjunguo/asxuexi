package cn.asxuexi.orgInfo.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cn.asxuexi.orgInfo.entity.OrgInfo;
import cn.asxuexi.tool.JsonData;

public interface OrgInfoService {
	/**
	 * 查询机构信息
	 * 
	 * @param orgId
	 *            {@link String} 机构id
	 * @return Map<String, Object>
	 */
	Map<String, Object> getOrgInformation();

	/**
	 * 获取地址的下一级省市
	 * 
	 * @param parentId
	 *            {@link String} id 父id
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> getAreas(String parentId);

	/**
	 * 更改省份
	 * 
	 * @param province
	 *            {@link String} 省份name
	 * @param city
	 *            {@link String} 城市name
	 * @param county
	 *            {@link String} 县城name
	 * @return List<List<Map<String, Object>>>
	 */
	List<Object> editProvince(String province, String city, String county);

	/**
	 * 保存更改信息
	 * 
	 * @param file
	 *            {@link MultipartFile} 图片文件
	 * @param orgInfo
	 *            机构的基本信息
	 * @param LogoImg
	 *            机构logo
	 */
	JsonData editOrg(String file, OrgInfo orgInfo, String LogoImg);

	/**
	 * @author fanjunguo 根据机构id查询机构名称
	 * @param orgId
	 * @return json数据 code表示查询是否成功:200表示成功,其他表示失败
	 */
	Map<String, Object> getOrgName(String orgId);

	/**
	 * 获取营业执照认证状态
	 * 
	 */
	Map<String, Object> getLicenceAuthenticationStatus();

	/**
	 * 获取身份证认证状态
	 * 
	 */
	Map<String, Object> getIdentityAuthenticationStatus();

	/**
	 * 上传身份证照片，营业执照照片
	 * 
	 * @param file
	 *            图片文件
	 */
	Map<String, Object> uploadIdentityPhoto(MultipartFile file,String key);

	/**
	 * 保存身份证信息
	 * 
	 * @param name
	 *            姓名
	 * @param idNumber
	 *            身份证号
	 * @param expiryDate
	 *            有效期截止日期
	 * @return
	 */
	Map<String, Object> insertIdentityInfo(String name, String idNumber, String expiryDate);

	/**
	 * 更新身份证信息
	 * 
	 * @param name
	 *            姓名
	 * @param idNumber
	 *            身份证号
	 * @param expiryDate
	 *            有效期截止日期
	 * @param gmtKey
	 *            需要更新的日期字段，gmt_create或gmt_modified
	 * @param updateKey
	 *            需要更新的状态字段，由front_,back_,person_中任意个组合而成的字符串
	 * @return
	 */
	Map<String, Object> updateIdentityInfo(String name, String idNumber, String expiryDate, String gmtKey,
			String updateKey);

	/**
	 * 保存营业执照信息
	 * 
	 * @param licenceNumber
	 *            营业执照号码
	 * @param companyName
	 *            注册名称
	 * @param companyAddress
	 *            注册地址
	 * @param legalRepresentative
	 *            法定代表人
	 * @return
	 */
	Map<String, Object> insertLicenceInfo(String licenceNumber, String companyName, String companyAddress,
			String legalRepresentative);

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
	 * @return
	 */
	Map<String, Object> updateLicenceInfo(String licenceNumber, String companyName, String companyAddress,
			String legalRepresentative, String gmtKey);

	/**
	 * 根据机构id,查询机构信息
	 * 
	 * @param orgId 机构id
	 * @return 返回map格式的机构信息数据
	 */
	Map<String, Object> getOrgInformation(String orgId);

}
