package cn.asxuexi.details.service;

import java.util.Map;

public interface CourseDetailService {
	/**
	 * 根据课程Id获得该课程名称
	 * 
	 * @param courseId
	 *            课程Id
	 * @return
	 */
	Map<String, Object> getCourseSort(String courseId);

	/**
	 * 根据课程Id获得该课程的所有信息
	 * 
	 * @param courseId
	 *            课程Id
	 * @return
	 */
	Map<String, Object> getCourse(String courseId);

	/**
	 * 根据机构Id获得该机构的基本信息
	 * 
	 * @param orgId
	 *            机构Id
	 * @param courseId
	 *            课程Id
	 * @return
	 */
	Map<String, Object> getOrg(String orgId, String courseId);

	/**
	 * 根据课程Id收藏该课程
	 * 
	 * @param courseId
	 *            课程Id
	 * @param orgId
	 *            机构Id
	 * @return
	 */
	Map<String, Object> storeCourse(String courseId, String orgId);

	/**
	 * 根据机构Id收藏该课程
	 * 
	 * @param orgId
	 *            机构Id
	 * @return
	 */
	Map<String, Object> storeOrg(String orgId);

	/**
	 * 保存用户对课程的提问
	 * 
	 * @param orgId
	 *            机构ID
	 * @param courseId
	 *            课程ID
	 * @param question
	 *            提问内容
	 * @return
	 */
	Map<String, Object> askQuestion(String orgId, String courseId, String question);

	/**
	 * 获取课程的所有问答
	 * 
	 * @param page
	 *            分页参数，当前页码
	 * @param rows
	 *            分页参数，每页行数
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	Map<String, Object> listQuestions(int page, int rows, String courseId);

}
