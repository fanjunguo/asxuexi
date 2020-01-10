package cn.asxuexi.organization.service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.asxuexi.OrgEnroll.dao.OrgEnrollDao;
import cn.asxuexi.orgInfo.dao.OrgInfoDao;
import cn.asxuexi.orgInfo.entity.OrgInfo;
import cn.asxuexi.organization.dao.BranchSchoolManagementDao;
import cn.asxuexi.organization.dao.OrgAccountDao;
import cn.asxuexi.tool.JsonData;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.Token_JWT;

/**
 * 分校管理-service层
 *
 * @author fanjunguo
 * @version 2019年9月24日 下午4:32:03
 */

@Service
public class BranchSchoolManagementServiceImpl implements BranchSchoolManagementService {

	private Logger logger=LoggerFactory.getLogger(this.getClass());
	@Resource
	private BranchSchoolManagementDao branchSchoolManagementDao;
	@Resource
	private OrgEnrollDao orgEnrollDao;
	@Resource
	private OrgAccountDao orgAccountDao;
	@Resource 
	private OrgInfoDao orgInfoDao;
	
	/**
	 * 创建分校
	 */
	@Override
	@Transactional
	public JsonData createBranchSchool(OrgInfo branchSchoolInfo) throws Exception{
		JsonData json;
		/* 新建机构需要更新的内容
		 * 1.新建机构
		 * 2.维护用户-机构关系表
		 * 3.新增账户
		 * 4.机构头像logo表
		 * */
		String userId = (String)Token_JWT.verifyToken().get("userId");
		String orgId =RandomTool.randomId("org");
		MDC.put("userId", userId);
		branchSchoolInfo.setOrgId(orgId);
		int resultOfCreateBranchSchool = branchSchoolManagementDao.createBranchSchool(branchSchoolInfo);
		logger.info("创建分校结果:"+resultOfCreateBranchSchool);
		int resultOfCreateNewRelationship = orgEnrollDao.insertUserOrgRelationship(userId, orgId, false,LocalDateTime.now());
		logger.info("更新用户机构关系表结果:"+resultOfCreateNewRelationship);
		// 创建机构账户信息
		int resultOfCreateOrgAccount = orgAccountDao.createOrgAccount(orgId);
		logger.info("创建机构账户信息结果:"+resultOfCreateOrgAccount);
		// 在logo表中创建该机构记录
		String logoId = RandomTool.randomId("logo");
		int resultOfInsertOrgLogo = orgInfoDao.insertLogo(orgId, logoId, branchSchoolInfo.getLogoUrl(), LocalDateTime.now());
		logger.info("更新logo表结果:"+resultOfInsertOrgLogo);
		json=JsonData.success();
		
		return json;
	}
	
	
	/**
	 * 查询所有学校名单
	 */
	@Override
	public JsonData getBranchSchoolList() {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		List<Map<String, String>> branchSchoolList = branchSchoolManagementDao.getBranchSchoolList(userId);
		return JsonData.success(branchSchoolList);
	}
	
	/**
	 * 更新机构信息
	 */
	@Override
	public JsonData updateSchoolInfo(OrgInfo orgInfo) {
		if (this.verifyIdentity(orgInfo.getOrgId())) {
			int updateResult = branchSchoolManagementDao.updateSchoolInfo(orgInfo);
			int updateLogoResult = orgInfoDao.updateLogo(orgInfo.getLogoUrl(), orgInfo.getOrgId(), LocalDateTime.now());
			if (1==updateResult&& 1==updateLogoResult) {
				return JsonData.success();
			} else {
				return JsonData.error("机构信息修改失败");
			}
		}else {
			return JsonData.error("身份验证失败");
		}
	}
	
	/**
	 * 更改机构状态
	 * 
	 * @param status
	 * @return
	 */
	@Override
	public JsonData updateSchoolStatus(boolean status,String orgId) {
		JsonData json;
		if (this.verifyIdentity(orgId)) {
			int updateSchoolStatus = branchSchoolManagementDao.updateSchoolStatus(status,orgId);
			if (updateSchoolStatus==1) {
				json=JsonData.success();
			} else {
				json=JsonData.error("更改机构状态时失败");
			}
		}else {
			json=JsonData.error("身份验证失败");
		}
		return json;
	}
	
	
	
	/**
	 * 私有方法:验证登录身份
	 * 
	 * @param orgId 要修改的机构id
	 * @return 验证通过返回true,不通过false
	 */
	private boolean verifyIdentity(String orgId) {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		List<Map<String, String>> schoolList = branchSchoolManagementDao.getBranchSchoolList(userId);
		Iterator<Map<String, String>> iterator = schoolList.iterator();
		while (iterator.hasNext()) {
			boolean containsValue = iterator.next().containsValue(orgId);
			if (containsValue) {
				return containsValue;
			}
		}
		return false;
	}
	
	
}
