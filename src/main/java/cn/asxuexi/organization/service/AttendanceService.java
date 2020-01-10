package cn.asxuexi.organization.service;

import java.util.Map;

/**
 * @author fanjunguo
 * @version 2019年3月12日 下午9:23:05
 * @description 
 */
public interface AttendanceService {

	/**
	 * @author fanjunguo
	 * @description 请求所有的课程列表
	 * @param page
	 * @param rows
	 * @return
	 */
	Map<String, Object> getClassList(int page, int rows);

	/**
	 * @author fanjunguo
	 * @description 请求班级的所有学生名单
	 * @param page 页数
	 * @param rows 每页行数
	 * @param classId
	 * @return
	 */
	Map<String, Object> getStuents(int page, int rows, String classId);

	/**
	 * @author fanjunguo
	 * @description 保存考勤情况
	 * @return
	 */
	Map<String, Object> saveAttendance(String attendance,String classId);

	/**
	 * @author fanjunguo
	 * @description 获取每个班级里面的学生考勤汇总
	 * @param classId
	 * @return
	 */
	Map<String, Object> getAttendance(String classId);

	/**
	 * @author fanjunguo
	 * @description 根据学生id,获取此学生的考勤明细
	 * @param studentId
	 * @param classId
	 * @param billPeriod 账单的开始日期和结束日期,需要处理一下,转成LocalDate类型
	 * @return 所有的考勤日期以及出勤情况
	 */
	Map<String, Object> getAttendanceDetail(String studentId, String classId, String billPeriod);

}