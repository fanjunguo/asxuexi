package cn.asxuexi.organization.service;

import cn.asxuexi.tool.JsonData;

public interface OrgQuestionService {
	/**
	 * 获取机构所有课程的ID和名称
	 * 
	 * @return
	 */
	JsonData listOrgCourses();

	/**
	 * 获取机构问题
	 * 
	 * @param page
	 *            分页参数，当前页码
	 * @param rows
	 *            分页参数，每页行数
	 * @param status
	 *            问题状态。0：全部问题；1：没有回答的问题；2：已回答的问题
	 * @return
	 */
	JsonData listOrgQuestions(int page, int rows, int status);

	/**
	 * 获取课程问题
	 * 
	 * @param page
	 *            分页参数，当前页码
	 * @param rows
	 *            分页参数，每页行数
	 * @param status
	 *            问题状态。0：全部问题；1：没有回答的问题；2：已回答的问题
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	JsonData listCourseQuestions(int page, int rows, int status, String courseId);

	/**
	 * 获取所有课程问题
	 * 
	 * @param page
	 *            分页参数，当前页码
	 * @param rows
	 *            分页参数，每页行数
	 * @param status
	 *            问题状态。0：全部问题；1：没有回答的问题；2：已回答的问题
	 * @return
	 */
	JsonData listAllCourseQuestions(int page, int rows, int status);

	/**
	 * 更新问题的回答
	 * 
	 * @param questionId
	 *            问题ID
	 * @param type
	 *            机构问题(org)或课程问题(course)
	 * @param answer
	 *            问题回答
	 * @return
	 */
	JsonData updateAnswer(String questionId, String type, String answer);

	/**
	 * 修改问题的置顶字段
	 * 
	 * @param questionId
	 *            问题ID
	 * @param type
	 *            问题类型，机构问题(org)或课程问题(course)
	 * @param stick
	 *            是否置顶，true:置顶；false:取消置顶
	 * @return
	 */
	JsonData updateStick(String questionId, String type, boolean stick);

}
