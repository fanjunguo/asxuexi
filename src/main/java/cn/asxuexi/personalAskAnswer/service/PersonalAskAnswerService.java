package cn.asxuexi.personalAskAnswer.service;

import java.util.Map;

public interface PersonalAskAnswerService {
	/**
	 * @作用 请求用户提问的课程数据
	 * @param table 查询0：org_ask_answer表或1: course_ask_answer表
	 * @param page 查询页数
	 * @param rows 每页的行数
	 **/
	Map<String, Object> listAnswer(String table, int page, int rows);
}
