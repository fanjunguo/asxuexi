package cn.asxuexi.organization.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.asxuexi.organization.service.OrgQuestionService;
import cn.asxuexi.tool.JsonData;

@RestController
public class OrgQuestionController {
	@Resource
	private OrgQuestionService orgQuestionService;

	/**
	 * 获取机构所有课程的ID和名称
	 * 
	 * @return
	 */
	@RequestMapping("orgQuestion/listOrgCourses.action")
	public JsonData listOrgCourses() {
		JsonData data = orgQuestionService.listOrgCourses();
		return data;
	}

	/**
	 * 获取机构问题
	 * 
	 * @param page
	 *            分页参数，当前页码
	 * @param rows
	 *            分页参数，每页行数
	 * @param solve
	 *            字符串，兼容旧请求的参数。"ing"代表没有回答的问题；"ed"代表已回答的问题
	 * @param status
	 *            问题状态。0：全部问题；1：没有回答的问题；2：已回答的问题
	 * @return
	 */
	@RequestMapping(value = { "OrgAskAnswer/listOrgAskAnswer.action", "orgQuestion/listOrgQuestions.action" })
	public JsonData listOrgQuestions(int page, String rows, String solve, String status) {
		if ("ing".equals(solve)) {
			status = "1";
		}
		if ("ed".equals(solve)) {
			status = "2";
		}
		if (rows == null) {
			// 兼容旧请求，rows默认为10
			rows = "10";
		}
		JsonData data = orgQuestionService.listOrgQuestions(page, Integer.parseInt(rows), Integer.parseInt(status));
		return data;
	}

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
	@RequestMapping("orgQuestion/listCourseQuestions.action")
	public JsonData listCourseQuestions(int page, int rows, int status, String courseId) {
		JsonData data = orgQuestionService.listCourseQuestions(page, rows, status, courseId);
		return data;
	}

	/**
	 * 获取所有课程的问题
	 * 
	 * @param page
	 *            分页参数，当前页码
	 * @param rows
	 *            分页参数，每页行数
	 * @param status
	 *            问题状态。0：全部问题；1：没有回答的问题；2：已回答的问题
	 * @return
	 */
	@RequestMapping("orgQuestion/listAllCourseQuestions.action")
	public JsonData listAllCourseQuestions(int page, int rows, int status) {
		JsonData data = orgQuestionService.listAllCourseQuestions(page, rows, status);
		return data;
	}

	/**
	 * 更新问题的回答
	 * 
	 * @param questionId
	 *            问题ID
	 * @param type
	 *            机构问题(org)或课程问题(course)
	 * @param answer
	 *            问题的回答
	 * @return
	 */
	@RequestMapping("orgQuestion/updateAnswer.action")
	public JsonData updateAnswer(String questionId, String type, String answer) {
		JsonData data = orgQuestionService.updateAnswer(questionId, type, answer);
		return data;
	}

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
	@RequestMapping("orgQuestion/updateStick.action")
	public JsonData updateStick(String questionId, String type, boolean stick) {
		JsonData data = orgQuestionService.updateStick(questionId, type, stick);
		return data;
	}

}
