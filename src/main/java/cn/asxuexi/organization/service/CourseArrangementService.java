package cn.asxuexi.organization.service;

import java.util.Map;

public interface CourseArrangementService {
	/**
	 * 获取某个课程的班级信息
	 * 
	 * @param courseId
	 *            课程id
	 * @return
	 */
	Map<String, Object> listClasses(String courseId);

	/**
	 * 获取某个课程的报名学生
	 * 
	 * @param rows
	 *            分页参数，每页行数
	 * @param page
	 *            分页参数，当前页数
	 * 
	 * @param courseId
	 *            课程
	 * @param keyword
	 *            检索学生的关键字，学生名称或手机号
	 * @return
	 */
	Map<String, Object> listStudents(int page, int rows, String courseId, String keyword);

	/**
	 * 获取某个课程的未分班学生
	 * 
	 * @param courseId
	 *            课程
	 * @return
	 */
	Map<String, Object> listUndividedStudents(String courseId);

	/**
	 * 为某课程添加新的班级
	 * 
	 * @param courseId
	 *            课程id
	 * @param className
	 *            班级名称
	 * @param classhourEachtime
	 *            每次上课课时数
	 * @param studentIds
	 *            JSON字符串，学生id列表 ["","",""]
	 * @return
	 */
	Map<String, Object> addClass(String courseId, String className, String classhourEachtime, String studentIds);

	/**
	 * 删除某课程的某个班级
	 * 
	 * @param courseId
	 *            课程ID
	 * @param classId
	 *            班级ID
	 * @param studentIds
	 *            JSON字符串，学生id列表 ["","",""]
	 * @return
	 */
	Map<String, Object> deleteClass(String courseId, String classId, String studentIds);

	/**
	 * 更新学生的班级信息
	 * 
	 * @param classId
	 *            班级ID
	 * @param className
	 *            班级名称
	 * @param classhourEachtime
	 *            每次上课课时数
	 * @param studentIds
	 *            JSON字符串，学生id列表 ["","",""]
	 * @param status
	 *            学生状态
	 * @return
	 */
	Map<String, Object> updateCourseStudentClass(String classId, String className, String classhourEachtime,
			String studentIds, String status);

	/**
	 * 删除学生
	 * 
	 * @param students
	 *            JSON字符串，学生列表 [{course_id:"课程ID",student_id:"学生ID"},{}]
	 * @return
	 */
	Map<String, Object> deleteStudent(String students);

	/**
	 * 根据手机号查询用户信息
	 * 
	 * @param q
	 *            用户输入的手机号
	 * @return
	 */
	Map<String, Object> listUsers(String tel);

	/**
	 * 增加学生
	 * 
	 * @param courseId
	 *            课程ID
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
	 * @return
	 */
	Map<String, Object> addStudent(String courseId, String studentName, String classId, String className,
			String classhourEachtime, String userTel);

}
