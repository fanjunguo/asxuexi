package cn.asxuexi.organization.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @author fanjunguo
 * @version 2019年3月12日 下午9:56:01
 * @description 
 */
public interface AttendanceDao {

	/**
	 * @author fanjunguo
	 * @description 请求所有的班级列表
	 * @param orgId 机构id
	 * @return
	 */
	List<Map<String, Object>> getClassList(String orgId);

	/**
	 * @author fanjunguo
	 * @description 请求班级的所有学生名单
	 * @param page 页数
	 * @param rows 每页行数
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getStuents(@Param("classId")String classId);
	
	/**
	 * @author fanjunguo
	 * @description 获取当前日期下,所有已考勤的班级名单
	 * @param classId 班级id
	 * @param now 当天的日期
	 */
	List<Map<String, Object>> isCheckedAttendance(@Param("now")LocalDate now);
	

	/**
	 * @author fanjunguo
	 * @description 
	 * @param parseArray
	 * @param classId
	 * @param today
	 * @param now
	 */
	@SuppressWarnings("rawtypes")
	int saveAttendance(@Param("parseArray")List<HashMap> parseArray, @Param("classId")String classId, @Param("today")LocalDate today, @Param("now")LocalDateTime now);
	
	

	/**
	 * @author fanjunguo
	 * @description 查询班级上一次结算的日期
	 * @param classId 班级id
	 * @return Date 上一次结算的日期
	 */
	public LocalDate getLastBillDate(String classId);
	
	/**
	 * @author fanjunguo
	 * @description 根据课程id,获取此课程下所有学生的考勤总揽
	 * @param classId 班级id
	 * @param lastBillDate 上次结算日期
	 * @return
	 */
	public List<Map<String, Object>> getTotalOfAttendanceDay(@Param("classId")String classId,@Param("startDate")java.time.LocalDate startDate,@Param("endDate")LocalDate endDate);
	
	/**
	 * @author fanjunguo
	 * @description 在统计账单日期时,如果是第一期账单,没有上次结算日期,所以需要将第一次考勤时间作为第一期账单的开始
	 * @param classId
	 * @return Date 第一次考勤的日期
	 */
	public LocalDate getStarTimeOfClass(String classId);
	
	public List<Map<String, Object>> getAttendanceDetail(@Param("studentId")String studentId,@Param("classId")String classId,@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate);
	
	/**
	 * @description 获取最新的一次考勤日期
	 */
	public LocalDate getTheLastAttendanceDay(String classId);
}
