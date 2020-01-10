package cn.asxuexi.organization.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.organization.service.CourseArrangementService;

@Controller
public class CourseArrangementController {
	@Resource
	private CourseArrangementService courseArrangementService;

	/**
	 * 获取某个课程的班级信息
	 * 
	 * @param courseId
	 *            课程id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("courseArrangement/listClasses.action")
	public Map<String, Object> listClasses(String courseId) {
		Map<String, Object> map = courseArrangementService.listClasses(courseId);
		return map;
	}

	/**
	 * 获取某个课程的报名学生
	 * 
	 * @param courseId
	 *            课程
	 * @param keyword
	 *            检索学生的关键字，学生名称或手机号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("courseArrangement/listStudents.action")
	public Map<String, Object> listStudents(int page, int rows, String courseId, String keyword) {
		Map<String, Object> map = courseArrangementService.listStudents(page, rows, courseId, keyword);
		return map;
	}

	/**
	 * 获取某个课程的未分班学生
	 * 
	 * @param courseId
	 *            课程
	 * @return
	 */
	@ResponseBody
	@RequestMapping("courseArrangement/listUndividedStudents.action")
	public Map<String, Object> listUndividedStudents(String courseId) {
		Map<String, Object> map = courseArrangementService.listUndividedStudents(courseId);
		return map;
	}

	/**
	 * 根据手机号查询用户信息
	 * 
	 * @param q
	 *            用户输入的手机号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("courseArrangement/listUsers.action")
	public Map<String, Object> listUsers(String q) {
		Map<String, Object> map = courseArrangementService.listUsers(q);
		return map;
	}

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
	@ResponseBody
	@RequestMapping("courseArrangement/addClass.action")
	public Map<String, Object> addClass(String courseId, String className, String classhourEachtime,
			String studentIds) {
		Map<String, Object> map = courseArrangementService.addClass(courseId, className, classhourEachtime, studentIds);
		return map;
	}

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
	@ResponseBody
	@RequestMapping("courseArrangement/deleteClass.action")
	public Map<String, Object> deleteClass(String courseId, String classId, String studentIds) {
		Map<String, Object> map = courseArrangementService.deleteClass(courseId, classId, studentIds);
		return map;
	}

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
	@ResponseBody
	@RequestMapping("courseArrangement/updateCourseStudentClass.action")
	public Map<String, Object> updateCourseStudentClass(String classId, String className, String classhourEachtime,
			String studentIds, String status) {
		Map<String, Object> map = courseArrangementService.updateCourseStudentClass(classId, className,
				classhourEachtime, studentIds, status);
		return map;
	}

	/**
	 * 删除学生
	 * 
	 * @param students
	 *            JSON字符串，学生列表 [{course_id:"课程ID",student_id:"学生ID"},{}]
	 * @return
	 */
	@ResponseBody
	@RequestMapping("courseArrangement/deleteStudent.action")
	public Map<String, Object> deleteStudent(String students) {
		Map<String, Object> map = courseArrangementService.deleteStudent(students);
		return map;
	}

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
	@ResponseBody
	@RequestMapping("courseArrangement/addStudent.action")
	public Map<String, Object> addStudent(String courseId, String studentName, String classId, String className,
			String classhourEachtime, String userTel) {
		Map<String, Object> map = courseArrangementService.addStudent(courseId, studentName, classId, className,
				classhourEachtime, userTel);
		return map;
	}

}
