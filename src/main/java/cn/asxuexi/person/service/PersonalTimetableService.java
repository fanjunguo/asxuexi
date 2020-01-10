package cn.asxuexi.person.service;

import cn.asxuexi.tool.JsonData;

public interface PersonalTimetableService {
	/**
	 * 获取用户某段时间内的课程安排
	 * 
	 * @param begin
	 *            开始日期(yyyy-mm-dd)
	 * @param end
	 *            结束日期(yyyy-mm-dd)
	 * @return
	 */
	JsonData listPersonalTimetables(String begin, String end);

}
