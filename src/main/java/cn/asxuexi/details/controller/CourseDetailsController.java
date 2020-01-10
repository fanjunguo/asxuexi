package cn.asxuexi.details.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.details.service.CourseDetailService;

@Controller
public class CourseDetailsController {
	@Resource
	private CourseDetailService courseDetailService;

	@RequestMapping("courseDetails.do")
	public String getCourseDetailsPage(String courseId, Model model) {
		Map<String, Object> courseName = courseDetailService.getCourseSort(courseId);
		if (courseName == null) {
			return "wrong_pages/courseNotFound";
		}
		model.addAllAttributes(courseName);
		model.addAttribute("courseId", courseId);
		return "details/courseDetails";
	}

	@RequestMapping("courseDetails/getCourse.do")
	@ResponseBody
	public Map<String, Object> getCourse(String courseId) {
		Map<String, Object> course = courseDetailService.getCourse(courseId);
		return course;
	}

	@RequestMapping("courseDetails/getOrg.do")
	@ResponseBody
	public Map<String, Object> getOrg(String orgId, String courseId) {
		Map<String, Object> org = courseDetailService.getOrg(orgId, courseId);
		return org;
	}

	@RequestMapping("courseDetails/storeCourse.action")
	@ResponseBody
	public Map<String, Object> storeCourse(String courseId, String orgId) {
		Map<String, Object> map = courseDetailService.storeCourse(courseId, orgId);
		return map;
	}

	@RequestMapping("courseDetails/storeOrg.action")
	@ResponseBody
	public Map<String, Object> storeOrg(String orgId) {
		Map<String, Object> map = courseDetailService.storeOrg(orgId);
		return map;
	}

	@RequestMapping("courseDetails/askQuestion.action")
	@ResponseBody
	public Map<String, Object> askQuestion(String orgId, String courseId, String question) {
		Map<String, Object> map = courseDetailService.askQuestion(orgId, courseId, question);
		return map;
	}

	@RequestMapping("courseDetails/listQuestions.do")
	@ResponseBody
	public Map<String, Object> listQuestions(int page, int rows, String courseId) {
		Map<String, Object> map = courseDetailService.listQuestions(page, rows, courseId);
		return map;
	}

}
