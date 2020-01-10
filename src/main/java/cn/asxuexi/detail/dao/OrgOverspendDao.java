package cn.asxuexi.detail.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrgOverspendDao {
	/**
	 * @作用 请求机构信息
	 * @param orgId
	 *            机构id
	 */
	public Map<String, Object> orgInfo(String orgId);

	/**
	 * @作用 查询机构的营业执照是否通过认证
	 * @param orgId
	 *            机构id
	 */
	public Map<String, Object> getValidate(String orgId);

	/**
	 * 查询logo
	 * 
	 * @param orgId
	 *            {@link String} 机构id
	 * @return Map<String, Object>
	 */
	Map<String, Object> getLogo(String orgId);

	/**
	 * @作用 查询出机构下前4的课程
	 * @param time
	 *            现在时间戳
	 * @param orgId
	 *            机构id
	 */
	public List<Map<String, Object>> listTopCourse(long time, String orgId);

	/**
	 * @作用 插入信息到org_ask_answer数据库
	 * @param id
	 *            问题id
	 * @param userId
	 *            用户id
	 * @param orgId
	 *            机构Id
	 * @param question
	 *            问题
	 * @param now
	 *            现在时间
	 */
	public int insertQuestion(String id, String userId, String orgId, String question, LocalDateTime now);

	/**
	 * @作用 查询信息到org_ask_answer数据库
	 * @param orgId
	 *            机构Id
	 * @param page
	 *            当前页数
	 * @param rows
	 *            一页几行
	 */
	public List<Map<String, Object>> listOrgQuestion(String orgId, int page, int rows);

	/**
	 * @作用 查询当前机构总数
	 * @param courseId
	 *            课程id
	 */
	public int countQuestion(String orgId);

	/**
	 * 修改机构浏览量
	 * 
	 * @param orgId
	 *            机构ID
	 * @param pageView
	 *            浏览量
	 */
	public void updatePageView(String orgId, Integer pageView);
}
