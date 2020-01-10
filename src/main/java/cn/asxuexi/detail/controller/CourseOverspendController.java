package cn.asxuexi.detail.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.detail.service.CourseOverspendService;

/**
 * @author 张顺
 * @作用 主要针对课程的详细页面 包含一些问答模块
 */
@Controller
public class CourseOverspendController {

	@Resource
	private CourseOverspendService courseOverspendService;

	/**
	 * @作用 请求课程的详细信息
	 * @author 张顺
	 * @return 课程详细信息
	 */
	@ResponseBody
	@RequestMapping("CourseOverspend/courseInfo.do")
	public Map<String, Object> courseInfo(String courseId) {
		Map<String, Object> courseInfo = courseOverspendService.courseInfo(courseId);
		return courseInfo;
	}

	/**
	 * @作用 添加收藏
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 */
	@ResponseBody
	@RequestMapping("CourseOverspend/insertCollectionCourse.action")
	public int insertCollectionCourse(String courseId, String orgId) {
		int insertCollectionCourse = courseOverspendService.insertCollectionCourse(courseId, orgId);
		return insertCollectionCourse;
	}

	/**
	 * @作用 删除收藏
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 */
	@ResponseBody
	@RequestMapping("CourseOverspend/UpdateCollectionCourse.action")
	public int UpdateCollectionCourse(String courseId, String orgId) {
		int insertCollectionCourse = courseOverspendService.updateCollectionCourse(courseId, orgId);
		return insertCollectionCourse;
	}

	/**
	 * @作用 插入课程问题
	 * @author 张顺
	 * @param orgId
	 *            机构id
	 * @param question
	 *            问题内容
	 * @return {@link Integer} 0、1
	 */
	@ResponseBody
	@RequestMapping("CourseOverspend/insertQuestion.action")
	public int insertQuestion(String orgId, String question, String courseId) {
		int insertQuestion = courseOverspendService.insertQuestion(orgId, question, courseId);
		return insertQuestion;
	}

	/**
	 * @作用 請求课程回答
	 * @param page
	 *            当前页数
	 */
	@ResponseBody
	@RequestMapping("CourseOverspend/listQuestion.do")
	public Map<String, Object> listQuestion(int page, int rows, String courseId) {
		Map<String, Object> listCourseQuestion = courseOverspendService.listCourseQuestion(page, rows, courseId);
		return listCourseQuestion;
	}

}
