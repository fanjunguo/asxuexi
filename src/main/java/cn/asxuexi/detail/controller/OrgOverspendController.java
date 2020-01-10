package cn.asxuexi.detail.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.detail.service.OrgOverspendService;

@Controller
public class OrgOverspendController {

	@Resource
	private OrgOverspendService orgOverspendService;

	/**
	 * 请求机构详细信息
	 * @param orgId 机构id
	 */
	@RequestMapping("orgOverspend/orgInfo.do")
	@ResponseBody
	public Map<String, Object> orgInfo(String orgId) {
		Map<String, Object> orgInfo = orgOverspendService.orgInfo(orgId);
		return orgInfo;
	}

	/**
	 * @作用 添加收藏机构
	 * @param orgId 机构id
	 */
	@ResponseBody
	@RequestMapping("orgOverspend/insertCollectionOrg.action")
	public int insertCollectionOrg(String orgId) {
		int insertCollectionOrg = orgOverspendService.insertCollectionOrg(orgId);
		return insertCollectionOrg;
	}

	/**
	 * @作用 删除收藏
	 * @param orgId
	 *            机构id
	 */
	@ResponseBody
	@RequestMapping("orgOverspend/UpdateCollectionOrg.action")
	public Map<String, Object> UpdateCollectionOrg(String orgId) {
		Map<String, Object> result = orgOverspendService.UpdateCollectionOrg(orgId);
		return result;
	}

	/**
	 * @作用 请求机构下前5门课程
	 * @param 机构的id
	 */
	@RequestMapping("orgOverspend/listTopCourse.do")
	@ResponseBody
	public List<Map<String, Object>> listTopCourse(String orgId) {
		List<Map<String, Object>> listTopCourse = orgOverspendService.listTopCourse(orgId);
		return listTopCourse;
	}

	/**
	 * @作用 插入问题
	 * @param question 提出的问题
	 * @param orgId 机构id
	 */
	@RequestMapping("orgOverspend/insertQuestion.action")
	@ResponseBody
	public int insertQuestion(String question, String orgId) {
		int insertQuestion = orgOverspendService.insertQuestion(question, orgId);
		return insertQuestion;
	}

	/**
	 * @作用 查询信息到org_ask_answer数据库
	 * @param page
	 *            当前页数
	 * @return map 基于jqgrid格式
	 */
	@ResponseBody
	@RequestMapping("orgOverspend/listQuestion.do")
	public Map<String, Object> listQuestion(int page,int rows, String orgId) {
		Map<String, Object> listOrgQuestion = orgOverspendService.listOrgQuestion(page, rows, orgId);
		return listOrgQuestion;
	}

}
