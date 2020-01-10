package cn.asxuexi.organization.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.asxuexi.orgInfo.entity.OrgInfo;
import cn.asxuexi.orgInfo.service.OrgInfoService;
import cn.asxuexi.organization.entity.CreateGroup;
import cn.asxuexi.organization.entity.UpdateGroup;
import cn.asxuexi.organization.service.BranchSchoolManagementService;
import cn.asxuexi.tool.JsonData;

/**
 * 机构分校管理:
 * 	1.创建机构分校
 * 	2.查询分校列表
 *
 * @author fanjunguo
 * @version 2019年9月24日 下午2:47:20
 */
@RestController
public class BranchSchoolManagementController {

	private Logger logger=LoggerFactory.getLogger(this.getClass());

	@Resource
	private BranchSchoolManagementService branchSchoolManagementService;
	
	@Resource
	private OrgInfoService orgInfoService;
	
	/**
	 * 创建分校
	 * 
	 * @author fanjunguo
	 * 
	 * @param String logoUrl 头像url
	 * @param String orgName 机构name
	 * @param String orgtel 机构电话
	 * @param String orgLegalPerson 机构负责人
	 * @param String orgAddress 详细地址
	 * @param String roomNumber 门牌号(如果详细地址不够详尽,可以通过门牌号补充. app用字段,非必填)
	 * @param String orgDes 机构简介
	 * @param BigDecimal lat 
	 * @param BigDecimal lng 
	 * @param String localId 机构所在县级id
	 * @return
	 */
	@RequestMapping("branchSchoolManagement/createBranchSchool.action")
	public JsonData createBranchSchool(@Validated({CreateGroup.class}) OrgInfo branchSchoolInfo,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			logger.info(bindingResult.getFieldError().getDefaultMessage());
			return JsonData.illegalParam();
		}else {
			try {
				return branchSchoolManagementService.createBranchSchool(branchSchoolInfo);
			} catch (Exception e) {
				logger.error("创建分校时出现异常",e);
				return JsonData.error(e.getLocalizedMessage());
			}
		}
	}
	
	/**
	 * 查询所有学校名单
	 * 
	 * 
	 * @author fanjunguo
	 * @return
	 */
	@RequestMapping("branchSchoolManagement/getBranchSchoolList.action")
	public JsonData getBranchSchoolList() {
		return branchSchoolManagementService.getBranchSchoolList();
	}
	
	
	/**
	 * 更新学校信息
	 * 
	 * 
	 * @author fanjunguo
	 * @param String orgId 新增必填字段.  
	 * 			其他字段同新建分校的字段.用户修改完保存时,将所有的、有填写内容的 字段信息传给后台
	 * @return
	 */
	@RequestMapping("branchSchoolManagement/updateSchoolInfo.action")
	public JsonData updateSchoolInfo(@Validated(UpdateGroup.class)OrgInfo orgInfo) {
		return branchSchoolManagementService.updateSchoolInfo(orgInfo);
	}
	
	/**
	 * 改变机构状态 
	 * 
	 * @author fanjunguo
	 * @param status 机构状态:true-在线 false-下线
	 * @param orgId 操作的机构id
	 * @return
	 */
	@RequestMapping("branchSchoolManagement/updateSchoolStatus.action")
	public JsonData updateSchoolStatus(@RequestParam(required=true)boolean status,@RequestParam(required=true)String orgId) {
		return branchSchoolManagementService.updateSchoolStatus(status,orgId);
	}
	
	/**
	 * 根据id,查询机构信息,用于在修改信息之前,先获取信息
	 * 
	 * @param orgId 机构id
	 */
	@RequestMapping("branchSchoolManagement/getSchoolInfoById.action")
	public JsonData getSchoolInfoById(String orgId) {
		Map<String, Object> orgInformation = orgInfoService.getOrgInformation(orgId);
		return JsonData.success(orgInformation);
	}
}
