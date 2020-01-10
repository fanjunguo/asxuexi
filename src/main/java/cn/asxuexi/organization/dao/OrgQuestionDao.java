package cn.asxuexi.organization.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OrgQuestionDao {
	/**
	 * 获取机构所有课程的ID和名称
	 * 
	 * @param orgId
	 *            机构ID
	 * @return
	 */
	List<Map<String, Object>> listOrgCourses(@Param("orgId") String orgId);

	/**
	 * 获取机构问题
	 * 
	 * @param orgId
	 *            机构ID
	 * @param status
	 *            问题状态。0：全部问题；1：没有回答的问题；2：已回答的问题
	 * @return
	 */
	List<Map<String, Object>> listOrgQuestions(@Param("orgId") String orgId, @Param("status") int status);

	/**
	 * 获取课程问题
	 * 
	 * @param orgId
	 *            机构ID
	 * @param status
	 *            问题状态。0：全部问题；1：没有回答的问题；2：已回答的问题
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	List<Map<String, Object>> listCourseQuestions(@Param("orgId") String orgId, @Param("status") int status,
			@Param("courseId") String courseId);

	/**
	 * 获取所有课程问题
	 * 
	 * @param orgId
	 *            机构ID
	 * @param status
	 *            问题状态。0：全部问题；1：没有回答的问题；2：已回答的问题
	 * @return
	 */
	List<Map<String, Object>> listAllCourseQuestions(@Param("orgId") String orgId, @Param("status") int status);

	/**
	 * 更新机构问题的回答
	 * 
	 * @param questionId
	 *            问题ID
	 * @param orgId
	 *            机构ID
	 * @param answer
	 *            问题回答
	 * @param now
	 *            更新时间
	 * @return
	 */
	int updateOrgAnswer(@Param("questionId") String questionId, @Param("orgId") String orgId,
			@Param("answer") String answer, @Param("now") LocalDateTime now);

	/**
	 * 更新课程问题的回答
	 * 
	 * @param questionId
	 *            问题ID
	 * @param orgId
	 *            机构ID
	 * @param answer
	 *            问题回答
	 * @param now
	 *            更新时间
	 * @return
	 */
	int updateCourseAnswer(@Param("questionId") String questionId, @Param("orgId") String orgId,
			@Param("answer") String answer, @Param("now") LocalDateTime now);

	/**
	 * 修改机构问题的置顶字段
	 * 
	 * @param questionId
	 *            问题ID
	 * @param orgId
	 *            机构ID
	 * @param stick
	 *            置顶时间
	 * @param now
	 *            修改时间
	 * @return
	 */
	int updateOrgStick(@Param("questionId") String questionId, @Param("orgId") String orgId,
			@Param("stick") LocalDateTime stick, @Param("now") LocalDateTime now);

	/**
	 * 修改课程问题的置顶字段
	 * 
	 * @param questionId
	 *            问题ID
	 * @param orgId
	 *            机构ID
	 * @param stick
	 *            置顶时间
	 * @param now
	 *            修改时间
	 * @return
	 */
	int updateCourseStick(@Param("questionId") String questionId, @Param("orgId") String orgId,
			@Param("stick") LocalDateTime stick, @Param("now") LocalDateTime now);

}
