package cn.asxuexi.detail.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author 张顺
 * @作用 主要针对课程的详细页面 包含一些问答模块
 */
public interface CourseOverspendDao {

	/**
	 * @作用 请求课程的详细信息
	 * @author 张顺
	 * @param courseId
	 *            课程id
	 * @return 课程详细信息
	 */
	List<Map<String, Object>> courseInfo(String courseId);

	/**
	 * @作用 查询是否收藏课程
	 * @param courseId
	 *            课程id
	 * @param userId
	 *            用户id
	 */
	int countCollectionCourse(String courseId, String userId);

	/**
	 * @作用 添加收藏
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 * @param userId
	 *            用户id
	 */
	int insertCollectionCourse(String courseId, String orgId, String userId);

	/**
	 * @作用 删除收藏
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 * @param userId
	 *            用户id
	 */
	int UpdateCollectionCourse(String courseId, String orgId, String userId);

	/**
	 * 获取父级的信息
	 * 
	 * @author 张顺
	 * @param sortId
	 *            分类编号
	 * @return Map<String, Object>
	 */
	Map<String, Object> getParentSort(String sortId);

	/**
	 * 获取课程视频
	 * 
	 * @author 张顺
	 * @param courseId
	 *            课程编号
	 * @return Map<String, Object>
	 */
	Map<String, Object> getCourseVideo(String courseId);

	/**
	 * @作用 插入问题
	 * @param id
	 *            问题id
	 * @param userId
	 *            用户id
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 * @param question
	 *            问题内容
	 * @param gmt_create
	 *            创建时间
	 * @return {@link Integer} 0或1
	 */
	int insertQuestion(String id, String userId, String courseId, String orgId, String question,
			LocalDateTime gmt_create);

	/**
	 * @作用 查询当前课程总数
	 * @param courseId
	 *            课程id
	 */
	int countQuestion(String courseId);

	/**
	 * @作用 获取课程问题及答案
	 * @param courseId
	 *            课程id
	 * @param page
	 *            当前页数
	 * @param rows
	 *            一页几个
	 */
	List<Map<String, Object>> listCourseQuestion(String courseId, int page, int rows);
}
