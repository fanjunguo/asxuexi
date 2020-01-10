package cn.asxuexi.organization.service;

import cn.asxuexi.orgInfo.entity.OrgInfo;
import cn.asxuexi.tool.JsonData;

/**
 *
 *
 * @author fanjunguo
 * @version 2019年9月24日 下午4:34:52
 */
public interface BranchSchoolManagementService {

	JsonData createBranchSchool(OrgInfo branchSchoolInfo) throws Exception;

	/**
	 * 查询所有的分校名单
	 */
	JsonData getBranchSchoolList();

	/**
	 * 更新机构信息
	 * 
	 * @author fanjunguo
	 * @param orgInfo
	 */
	JsonData updateSchoolInfo(OrgInfo orgInfo);

	/**
	 * 更改机构状态
	 * 
	 * @param status 上线true,下线false
	 * @return
	 */
	JsonData updateSchoolStatus(boolean status,String orgId);

}