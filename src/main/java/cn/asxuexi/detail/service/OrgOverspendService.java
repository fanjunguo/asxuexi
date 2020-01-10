package cn.asxuexi.detail.service;

import java.util.List;
import java.util.Map;

public interface OrgOverspendService {
	/**
	 * 请求机构详细信息
	 * @param orgId 机构id
	 */
	Map<String, Object> orgInfo(String orgId);


	/**
	 * @作用 删除收藏
	 * @param orgId
	 *            机构id
	 */
	Map<String, Object> UpdateCollectionOrg(String orgId);

	/**
	 * @作用 查询出机构下前5的课程
	 * @param orgId
	 *            机构id
	 */
	List<Map<String, Object>> listTopCourse(String orgId);
	/**
	 * @作用 插入信息到org_ask_answer数据库
	 * @param question
	 *            问题
	 * @param orgId 
	 */
	public int insertQuestion( String question, String orgId) ;
	/**
	 * @作用 查询信息到org_ask_answer数据库
	 * @param page
	 *            当前页数
	 * @param rows
	 *            一页几行
	 * @return map 基于jqgrid格式
	 */
	public Map<String, Object> listOrgQuestion(int page, int rows, String orgId);


	/**
	 * @author fanjunguo
	 * @description 添加机构收藏
	 * @param orgId
	 * @return
	 */
	int insertCollectionOrg(String orgId);
}
