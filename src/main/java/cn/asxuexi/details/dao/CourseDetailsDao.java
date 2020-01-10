package cn.asxuexi.details.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.asxuexi.details.entity.CourseDO;

public interface CourseDetailsDao {
	/**
	 * 根据课程Id获得该课程的名称
	 * 
	 * @param courseId
	 *            课程Id
	 * @return
	 */
	Map<String, Object> getCourseSort(@Param("courseId") String courseId);

	/**
	 * 根据课程Id获得该课程的基本信息
	 * 
	 * @param courseId
	 *            课程Id
	 * @return
	 */
	CourseDO getCourse(@Param("courseId") String courseId);

	/**
	 * 根据课程Id获得该课程的套餐信息
	 * 
	 * @param courseId
	 *            课程Id
	 * @return
	 */
	List<Map<String, Object>> listCoursePackages(@Param("courseId") String courseId);

	/**
	 * 根据课程Id获得该课程的图片信息
	 * 
	 * @param courseId
	 *            课程Id
	 * @return
	 */
	List<Map<String, Object>> listImgs(@Param("courseId") String courseId);

	/**
	 * 根据课程Id获得该课程的视频信息
	 * 
	 * @param courseId
	 *            课程Id
	 * @return
	 */
	List<Map<String, Object>> listCourseVideos(@Param("courseId") String courseId);

	/**
	 * 根据课程Id获取课程安排信息
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	List<Map<String, Object>> listCourseTimetable(@Param("courseId") String courseId);

	/**
	 * 根据机构Id获得该机构的基本信息
	 * 
	 * @param orgId
	 *            机构Id
	 * @return
	 */
	Map<String, Object> getOrg(@Param("orgId") String orgId);

	/**
	 * 根据机构Id获得该机构的课程列表。目前是获取机构下前两位的课程，且不包括courseId对应的课程。
	 * 
	 * @param orgId
	 *            机构ID
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	List<Map<String, Object>> listCourses(@Param("orgId") String orgId, @Param("courseId") String courseId);

	/**
	 * 向收藏表中添加新纪录
	 * 
	 * @param userId
	 *            用户Id
	 * @param courseId
	 *            课程Id，添加机构的收藏时，该值为“0”
	 * @param orgId
	 *            机构Id
	 * @return
	 */
	int insertCollection(@Param("userId") String userId, @Param("courseId") String courseId,
			@Param("orgId") String orgId);

	/**
	 * 保存用户对课程的提问
	 * 
	 * @param id
	 *            问题ID
	 * @param userId
	 *            用户ID
	 * @param orgId
	 *            机构ID
	 * @param courseId
	 *            课程ID
	 * @param question
	 *            提问内容
	 * @param now
	 *            创建时间
	 * @return
	 */
	int insertQuestion(@Param("id") String id, @Param("userId") String userId, @Param("orgId") String orgId,
			@Param("courseId") String courseId, @Param("question") String question, @Param("now") LocalDateTime now);

	/**
	 * 获取课程的所有问答
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	List<Map<String, Object>> listQuestions(@Param("courseId") String courseId);

	/**
	 * 修改课程浏览量
	 * 
	 * @param courseId
	 *            课程ID
	 * @param pageView
	 *            浏览量
	 */
	void updatePageView(@Param("courseId") String courseId, @Param("pageView") Integer pageView);

	/**
	 * 获取课程的报名人数
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	Integer countCourseStudent(@Param("courseId") String courseId);
}
