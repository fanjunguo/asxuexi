package cn.asxuexi.organization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.asxuexi.orgInfo.entity.OrgInfo;

public interface BranchSchoolManagementDao {
	
	/**
	 * 创建分校
	 */
	int createBranchSchool(OrgInfo branchSchoolInfo);

	/**
	 * 查询所有的分校名单
	 */
	List<Map<String, String>> getBranchSchoolList(String userId);
	
	/**
	 * 更新机构信息
	 * 
	 * @author fanjunguo
	 * @param orgInfo
	 */
	int updateSchoolInfo(OrgInfo orgInfo);

	/**
	 * 更改机构状态
	 */
	int updateSchoolStatus(@Param("status")boolean status,@Param("orgId") String orgId);
}
