package cn.asxuexi.organization.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CourseArrangementDao {
	/**
	 * 获取某个课程的所有班级信息
	 * 
	 * @param courseId
	 *            课程id
	 * @return
	 */
	List<Map<String, Object>> listClasses(@Param("courseId") String courseId);

	/**
	 * 获取某个课程下所有班级的学生信息
	 * 
	 * @param courseId
	 *            课程id
	 * @return
	 */
	List<Map<String, Object>> listCourseStudents(@Param("courseId") String courseId);

	/**
	 * 获取order订单表中某个课程的学生（order_status为11，12，14，20，-1的记录）
	 * 
	 * @param courseId
	 * @return
	 */
	List<Map<String, Object>> listOrderStudents(@Param("courseId") String courseId);

	/**
	 * 获取course_class_student表中某个课程的报名学生
	 * 
	 * @param courseId
	 *            课程
	 * @param keyword
	 *            检索学生的关键字，学生名称或手机号
	 * @return
	 */
	List<Map<String, Object>> listStudents(@Param("courseId") String courseId, @Param("keyword") String keyword);

	/**
	 * 向course_class_student表添加新学生的记录
	 * 
	 * @param insertStudentList
	 *            学生记录列表
	 * @param now
	 *            记录创建时间
	 * @return
	 */
	int insertCourseStudent(@Param("insertStudentList") List<Map<String, Object>> insertStudentList,
			@Param("now") LocalDateTime now);

	/**
	 * 修改course_class_student表中某些学生的状态字段（status）为-1
	 * 
	 * @param now
	 *            记录修改时间
	 * 
	 * @param deleteStudentList
	 *            要更改的记录列表
	 * @return
	 */
	int updateCourseStudentStatus(@Param("updateStudentList") List<Map<String, Object>> updateStudentList,
			@Param("now") LocalDateTime now);

	/**
	 * 获取某个课程的未分班学生
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	List<Map<String, Object>> listUndividedStudents(@Param("courseId") String courseId);

	/**
	 * 向course_class表添加新的记录
	 * 
	 * @param courseId
	 *            课程ID
	 * @param classId
	 *            班级ID
	 * @param className
	 *            班级名称
	 * @param classhourEachtime
	 *            每次上课课时数
	 * @param now
	 *            记录创建时间
	 * @return
	 */
	int insertCourseClass(@Param("courseId") String courseId, @Param("classId") String classId,
			@Param("className") String className, @Param("classhourEachtime") String classhourEachtime,
			@Param("now") LocalDateTime now);

	/**
	 * 根据学生ID修改course_class_student表中记录的
	 * class_id,class_name,classhour_eachtime,status,gmt_modified字段
	 * 
	 * @param classId
	 *            班级ID
	 * @param className
	 *            班级名称
	 * @param classhourEachtime
	 *            每次上课课时数
	 * @param now
	 *            记录修改时间
	 * @param studentIdList
	 *            学生ID列表
	 * @return
	 */
	int updateCourseStudentClass(@Param("classId") String classId, @Param("className") String className,
			@Param("classhourEachtime") String classhourEachtime, @Param("status") String status,
			@Param("now") LocalDateTime now, @Param("studentIdList") List<String> studentIdList);

	/**
	 * 删除某课程下的某个班级
	 * 
	 * @param courseId
	 *            课程ID
	 * @param classId
	 *            班级ID
	 * @return
	 */
	int deleteCourseClass(@Param("courseId") String courseId, @Param("classId") String classId);

	/**
	 * 根据手机号查询用户信息
	 * 
	 * @param q
	 *            用户输入的手机号
	 * @return
	 */
	List<Map<String, Object>> listUsers(@Param("tel") String tel);

	/**
	 * 向course_class_student表添加新学生的记录
	 * 
	 * @param courseId
	 *            课程ID
	 * @param studentId
	 *            学生ID
	 * @param studentName
	 *            学生名称
	 * @param classId
	 *            班级ID
	 * @param className
	 *            班级名称
	 * @param classhourEachtime
	 *            每次上课课时数
	 * @param userTel
	 *            用户手机号
	 * @param status
	 *            学生状态
	 * @param now
	 *            记录创建时间
	 * @param userId 用户userId
	 * @return
	 */
	int insertStudent(@Param("courseId") String courseId, @Param("studentId") String studentId,
			@Param("studentName") String studentName, @Param("classId") String classId,
			@Param("className") String className, @Param("classhourEachtime") String classhourEachtime,
			@Param("userTel") String userTel, @Param("status") String status, @Param("now") LocalDateTime now, @Param("userId")String userId);

	/**
	 * 根据手机好,查询用户userId
	 * 
	 * @author fanjunguo
	 * @param userTel 手机号
	 */
	String getUserId(String userTel);
}
