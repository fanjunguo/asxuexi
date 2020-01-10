package cn.asxuexi.organization.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.asxuexi.organization.dao.CourseArrangementDao;
import cn.asxuexi.tool.RandomTool;

@Service
public class CourseArrangementServiceImpl implements CourseArrangementService {
	@Resource
	private CourseArrangementDao courseArrangementDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Map<String, Object> listClasses(String courseId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> listClasses = courseArrangementDao.listClasses(courseId);
			List<Map<String, Object>> listClassStudents = courseArrangementDao.listCourseStudents(courseId);
			for (Map<String, Object> map : listClasses) {
				List<Map<String, Object>> students = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> student : listClassStudents) {
					if (map.get("class_id").equals(student.get("class_id"))) {
						students.add(student);
					}
				}
				map.put("studentList", students);
			}
			resultMap.put("code", 600);
			resultMap.put("message", "成功");
			resultMap.put("data", listClasses);
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> listStudents(int page, int rows, String courseId, String keyword) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> listCourseStudents = courseArrangementDao.listCourseStudents(courseId);
			List<Map<String, Object>> listOrderStudents = courseArrangementDao.listOrderStudents(courseId);
			List<Map<String, Object>> updateStudentList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> tempStudentList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> orderStudent : listOrderStudents) {
				for (Map<String, Object> courseStudent : listCourseStudents) {
					if (orderStudent.get("student_id").equals(courseStudent.get("student_id"))) {
						tempStudentList.add(orderStudent);
					}
					if (14 == (Integer) orderStudent.get("order_status")
							|| -1 == (Integer) orderStudent.get("order_status")) {
						tempStudentList.add(orderStudent);
						if (!updateStudentList.contains(orderStudent)) {
							updateStudentList.add(orderStudent);
						}
					}
				}
			}
			listOrderStudents.removeAll(tempStudentList);
			for (Map<String, Object> student : listOrderStudents) {
				student.put("status", "0");
			}
			courseArrangementDao.insertCourseStudent(listOrderStudents, LocalDateTime.now());
			courseArrangementDao.updateCourseStudentStatus(updateStudentList, LocalDateTime.now());
			PageHelper.startPage(page, rows);
			List<Map<String, Object>> listStudents = courseArrangementDao.listStudents(courseId, keyword);
			PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(listStudents);
			for (Map<String, Object> student : listStudents) {
				Map<String, Object> packageInfo = new HashMap<String, Object>();
				packageInfo.put("package_id", student.get("package_id"));
				packageInfo.put("package_name", student.get("package_name"));
				packageInfo.put("package_price", student.get("package_price"));
				packageInfo.put("course_length", student.get("course_length"));
				student.put("package_info", packageInfo);
				student.remove("package_id");
				student.remove("package_name");
				student.remove("package_price");
				student.remove("course_length");
			}
			resultMap.put("code", 600);
			resultMap.put("message", "成功");
			dataMap.put("rows", listStudents);
			dataMap.put("total", pageInfo.getTotal());
			resultMap.put("data", dataMap);
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> listUndividedStudents(String courseId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> listUndividedStudents = courseArrangementDao.listUndividedStudents(courseId);
			resultMap.put("code", 600);
			resultMap.put("message", "成功");
			resultMap.put("data", listUndividedStudents);
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> addClass(String courseId, String className, String classhourEachtime,
			String studentIds) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<String> studentIdList = JSON.parseArray(studentIds, String.class);
			String classId = "class" + courseId.substring(courseId.indexOf("_")) + "_" + RandomTool.getRandomInt(3);
			int insertCourseClass = courseArrangementDao.insertCourseClass(courseId, classId, className,
					classhourEachtime, LocalDateTime.now());
			if (insertCourseClass == 1) {
				if (studentIdList.size() != 0) {
					courseArrangementDao.updateCourseStudentClass(classId, className, classhourEachtime, "1",
							LocalDateTime.now(), studentIdList);
				}
				resultMap.put("code", 600);
				resultMap.put("message", "成功");
			} else {
				resultMap.put("code", 400);
				resultMap.put("message", "失败");
			}
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> deleteClass(String courseId, String classId, String studentIds) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<String> studentIdList = JSON.parseArray(studentIds, String.class);
			int deleteCourseClass = courseArrangementDao.deleteCourseClass(courseId, classId);
			if (deleteCourseClass == 1) {
				if (studentIdList.size() != 0) {
					courseArrangementDao.updateCourseStudentClass(null, null, null, "0", LocalDateTime.now(),
							studentIdList);
				}
				resultMap.put("code", 600);
				resultMap.put("message", "成功");
			}
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> updateCourseStudentClass(String classId, String className, String classhourEachtime,
			String studentIds, String status) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<String> studentIdList = JSON.parseArray(studentIds, String.class);
			courseArrangementDao.updateCourseStudentClass(classId, className, classhourEachtime, status,
					LocalDateTime.now(), studentIdList);
			resultMap.put("code", 600);
			resultMap.put("message", "成功");
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> deleteStudent(String students) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> updateStudentList = JSONObject.parseObject(students,
					new TypeReference<List<Map<String, Object>>>() {
					});
			courseArrangementDao.updateCourseStudentStatus(updateStudentList, LocalDateTime.now());
			resultMap.put("code", 600);
			resultMap.put("message", "成功");
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> listUsers(String q) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> listUsers = courseArrangementDao.listUsers(q);
			resultMap.put("code", 600);
			resultMap.put("message", "成功");
			resultMap.put("data", listUsers);
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> addStudent(String courseId, String studentName, String classId, String className,
			String classhourEachtime, String userTel) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//根据手机号查询userId:以后可以考虑优化,在上游查询出来
			String userId = courseArrangementDao.getUserId(userTel);
			String studentId = RandomTool.randomId("student");
			courseArrangementDao.insertStudent(courseId, studentId, studentName, classId, className, classhourEachtime,
					userTel, "1", LocalDateTime.now(),userId);
			resultMap.put("code", 600);
			resultMap.put("message", "成功");
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

}
