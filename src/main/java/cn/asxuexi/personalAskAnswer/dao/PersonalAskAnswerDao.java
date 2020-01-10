package cn.asxuexi.personalAskAnswer.dao;

import java.util.List;
import java.util.Map;

public interface PersonalAskAnswerDao {
	/**
	 * @作用 获取课程问题的总数
	 * @param userId 用户id
	 */
	int countCourseAskAnswer(String userId);
	/**
	 * @param page
	 *            当前页数
	 * @param rows
	 *            显示个数
	 * @param userId
	 *            用户id
	 * @作用 获取 课程 本页的信息(课程)
	 */
	List<Map<String, Object>> listCourseAskAnswer(int page, int rows, String userId);
	/**
	 * @作用 获取机构问题的总数
	 * @param userId 用户id
	 */
	int countOrgAskAnswer(String userId);
	/**
	 * @param page
	 *            当前页数
	 * @param rows
	 *            显示个数
	 * @param userId
	 *            用户id
	 * @作用 获取 机构 本页的信息(机构)
	 */
	public List<Map<String, Object>> listOrgAskAnswer(int page, int rows, String userId);
}
