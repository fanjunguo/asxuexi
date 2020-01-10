package cn.asxuexi.orgInfo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.asxuexi.orgInfo.entity.OrgInfo;
import cn.asxuexi.orgInfo.service.OrgInfoService;
import cn.asxuexi.tool.JsonData;

@Controller
public class OrgInfoController {

	@Resource
	private OrgInfoService infoService;

	/**
	 * 获取请求机构信息
	 */
	@ResponseBody
	@RequestMapping("asxuexi/org/orgInfo.action")
	public Map<String, Object> getorgInformation() {
		Map<String, Object> orgInformation = infoService.getOrgInformation();
		return orgInformation;
	}

	/**
	 * 获取地址的下一级省市
	 * 
	 * @param parentId
	 *            {@link String} id 父id
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping("asxuexi/org/getAreas.do")
	@ResponseBody
	public List<Map<String, Object>> getAreas(String parentId) {
		List<Map<String, Object>> areas = infoService.getAreas(parentId);
		return areas;
	}

	/**
	 * 更改省份
	 * 
	 * @param provinceId
	 *            {@link String} 省份name
	 * @param city
	 *            {@link String} 城市name
	 * @param county
	 *            {@link String} 县城name
	 * @return List<List<Map<String, Object>>>
	 */
	@ResponseBody
	@RequestMapping("asxuexi/org/editProvince.do")
	public List<Object> editProvince(String province, String city, String county) {
		List<Object> editProvince = infoService.editProvince(province, city, county);
		return editProvince;
	}

	/**
	 * 保存更改信息
	 * 
	 * @author zhangshun
	 * @param file {@link String} 头像url
	 * @param orgName {@link String} 机构name
	 * @param orgtel {@link String} 机构电话
	 * @param orgLegalPerson {@link String} 机构负责人
	 * @param orgAddress {@link String} 详细地址
	 * @param roomNumber 门牌号(如果详细地址不够详尽,可以通过门牌号补充. app用字段,非必填)
	 * @param orgDes  {@link String}机构简介
	 * @param lat {@link BigDecimal}
	 * @param lng {@link BigDecimal}
	 * @param localId  {@link String} 机构所在县级id
	 * @param LogoImg  原来的图片
	 */
	@ResponseBody
	@RequestMapping("asxuexi/org/editOrg.action")
	public JsonData editOrg(String file, String orgName, String orgtel, String orgLegalPerson, String orgAddress,
			String orgDes, BigDecimal lat, BigDecimal lng, String localId, String LogoImg,String roomNumber) {
		OrgInfo orgInfo = new OrgInfo(orgName, orgtel, orgLegalPerson, orgAddress, orgDes, lat, lng, localId,roomNumber,file);
		return infoService.editOrg(file, orgInfo, LogoImg);
	}
	
	/**
	 * @author fanjunguo
	 * @description 根据机构id查询机构名称
	 * @param orgId
	 * @return json数据
	 */
	@ResponseBody
	@RequestMapping("orgInfo/getOrgName.do")
	public Map<String, Object> getOrgName(String orgId) {
		Map<String, Object> orgName = infoService.getOrgName(orgId);
		return orgName;
	}

	/**
	 * 获取身份证认证状态
	 * 
	 */
	@ResponseBody
	@RequestMapping("asxuexi/org/getIdentityAuthenticationStatus.action")
	public Map<String, Object> getIdentityAuthenticationStatus() {
		Map<String, Object> status = infoService.getIdentityAuthenticationStatus();
		return status;
	}

	/**
	 * 上传身份证照片,营业执照照片
	 * 
	 * @param file
	 *            图片文件
	 * @param key
	 *            图片文件附件字段，不为null或''则使用此字段作为文件名 TODO zhoubin 于H5、小程序、手机端情况下无法变更上传文件名使用
	 */
	@ResponseBody
	@RequestMapping("asxuexi/org/uploadIdentityPhoto.action")
	public Map<String, Object> uploadIdentityPhoto(@RequestParam("file") MultipartFile file,String key) {
		Map<String, Object> uploaderPersonal = infoService.uploadIdentityPhoto(file,key);
		return uploaderPersonal;
	}

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
	@ResponseBody
	@RequestMapping("asxuexi/org/insertIdentityInfo.action")
	public Map<String, Object> insertIdentityInfo(String name, String idNumber, String expiryDate) {
		Map<String, Object> map = infoService.insertIdentityInfo(name, idNumber, expiryDate);
		return map;
	}

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
	@ResponseBody
	@RequestMapping("asxuexi/org/updateIdentityInfo.action")
	public Map<String, Object> updateIdentityInfo(String name, String idNumber, String expiryDate, String gmtKey,
			String updateKey) {
		Map<String, Object> map = infoService.updateIdentityInfo(name, idNumber, expiryDate, gmtKey, updateKey);
		return map;
	}

	/**
	 * 获取机构认证
	 */
	@ResponseBody
	@RequestMapping("asxuexi/org/getLicenceAuthenticationStatus.action")
	public Map<String, Object> getLicenceAuthenticationStatus() {
		Map<String, Object> status = infoService.getLicenceAuthenticationStatus();
		return status;
	}

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
	@ResponseBody
	@RequestMapping("asxuexi/org/insertLicenceInfo.action")
	public Map<String, Object> insertLicenceInfo(String licenceNumber, String companyName, String companyAddress,
			String legalRepresentative) {
		Map<String, Object> map = infoService.insertLicenceInfo(licenceNumber, companyName, companyAddress,
				legalRepresentative);
		return map;
	}

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
	@ResponseBody
	@RequestMapping("asxuexi/org/updateLicenceInfo.action")
	public Map<String, Object> updateLicenceInfo(String licenceNumber, String companyName, String companyAddress,
			String legalRepresentative, String gmtKey) {
		Map<String, Object> map = infoService.updateLicenceInfo(licenceNumber, companyName, companyAddress,
				legalRepresentative, gmtKey);
		return map;
	}

}
