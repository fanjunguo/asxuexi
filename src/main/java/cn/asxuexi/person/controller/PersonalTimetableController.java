package cn.asxuexi.person.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.asxuexi.person.service.PersonalTimetableService;
import cn.asxuexi.tool.JsonData;

@RestController
public class PersonalTimetableController {
	@Resource
	private PersonalTimetableService personalTimetableService;

	/**
	 * 获取用户某段时间内的课程安排(包含开始日期，不含结束日期)
	 * 
	 * @param begin
	 *            开始日期(yyyy-mm-dd)
	 * @param end
	 *            结束日期(yyyy-mm-dd)
	 * @return
	 */
	@RequestMapping("personalTimetable/listPersonalTimetables.action")
	public JsonData listPersonalTimetables(String begin, String end) {
		JsonData data = personalTimetableService.listPersonalTimetables(begin, end);
		return data;
	}
}
