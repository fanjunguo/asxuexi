package cn.asxuexi.detail.service;

import java.util.Map;

/**
* @author 张顺
* @作用 主要针对课程的详细页面 包含一些问答模块
*/
public interface CourseOverspendService {

	/**
	 * @作用 请求课程的详细信息
	 * @author 张顺
	 * @param courseId 
	 * @return 课程详细信息
	 */
	Map<String, Object> courseInfo(String courseId);
	/**
	 * @作用 添加收藏
	 * @param courseId 课程id
	 * @param orgId 机构id
	 * */
	int insertCollectionCourse(String courseId,String orgId);
	/**
	 * @作用 删除收藏
	 * @param courseId 课程id
	 * @param orgId 机构id
	 * */
	int updateCollectionCourse(String courseId,String orgId);
	/**
	 * @作用 插入问题
	 * @param orgId 机构id
	 * @param question 问题内容
	 * @param courseId 
	 * @return {@link Integer} 0或1
	 * */	
	
	int insertQuestion(String orgId,String question, String courseId);
	/**
	 * @作用 获取课程问题及答案
	 * @param page
	 *            当前页数
	 * @param rows
	 *            一页几个
	 * @param courseId 
	 */
	 Map<String, Object> listCourseQuestion(int page, int rows, String courseId);
}
