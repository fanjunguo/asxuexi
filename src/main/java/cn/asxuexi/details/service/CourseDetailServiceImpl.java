package cn.asxuexi.details.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.asxuexi.details.dao.CourseDetailsDao;
import cn.asxuexi.details.entity.CourseDO;
import cn.asxuexi.tool.DateTimeTool;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.Token_JWT;

@Service
public class CourseDetailServiceImpl implements CourseDetailService {
	@Resource
	private CourseDetailsDao courseDetailsDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Map<String, Object> getCourseSort(String courseId) {
		Map<String, Object> courseSort = courseDetailsDao.getCourseSort(courseId);
		return courseSort;
	}

	@Override
	public Map<String, Object> getCourse(String courseId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			CourseDO course = courseDetailsDao.getCourse(courseId);
			Integer studentNumber = courseDetailsDao.countCourseStudent(courseId);
			if (course != null) {
				course.setStudentNumber(studentNumber);
				// 格式化课程安排中的时间字符串
				List<Map<String, Object>> listCourseTimetable = course.getCourseTimetable();
				for (Map<String, Object> courseTimetable : listCourseTimetable) {
					Date chapterDate = (Date) courseTimetable.get("chapter_date");
					String chapterDateStr = DateTimeTool.getDateStr(chapterDate.getTime() / 1000);
					courseTimetable.put("chapter_date", chapterDateStr);
				}
				Integer pageView = course.getPage_view();
				pageView++;
				course.setPage_view(pageView);
				courseDetailsDao.updatePageView(courseId, pageView);
				resultMap.put("code", 600);
				resultMap.put("message", "成功");
				resultMap.put("data", course);
			} else {
				resultMap.put("code", 400);
				resultMap.put("message", "没有该课程的信息");
			}
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getOrg(String orgId, String courseId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> org = courseDetailsDao.getOrg(orgId);
			if (org != null) {
				List<Map<String, Object>> listCourses = courseDetailsDao.listCourses(orgId, courseId);
				org.put("courseList", listCourses);
				resultMap.put("code", 600);
				resultMap.put("message", "成功");
				resultMap.put("data", org);
			} else {
				resultMap.put("code", 400);
				resultMap.put("message", "没有该机构的信息");
			}
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> storeCourse(String courseId, String orgId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String userId = (String) Token_JWT.verifyToken().get("userId");
		try {
			int insertCollection = courseDetailsDao.insertCollection(userId, courseId, orgId);
			if (insertCollection == 1) {
				resultMap.put("code", 600);
				resultMap.put("message", "收藏成功");
			}
		} catch (DuplicateKeyException e) {
			// 存在该条记录，说明已收藏该课程，反馈相应的信息
			resultMap.put("code", 403);
			resultMap.put("message", "已收藏过");
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> storeOrg(String orgId) {
		return storeCourse("0", orgId);
	}

	@Override
	public Map<String, Object> askQuestion(String orgId, String courseId, String question) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userId = (String) Token_JWT.verifyToken().get("userId");
			int insertQuestion = courseDetailsDao.insertQuestion(RandomTool.randomId("question"), userId, orgId,
					courseId, question, LocalDateTime.now());
			if (insertQuestion == 1) {
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
	public Map<String, Object> listQuestions(int page, int rows, String courseId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			PageHelper.startPage(page, rows);
			List<Map<String, Object>> listQuestions = courseDetailsDao.listQuestions(courseId);
			for (Map<String, Object> question : listQuestions) {
				Timestamp create = (Timestamp) question.get("gmt_create");
				Timestamp modified = (Timestamp) question.get("gmt_modified");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String gmtCreate = format.format(create);
				question.put("gmt_create", gmtCreate);
				if (modified != null) {
					String gmtModified = format.format(modified);
					question.put("gmt_modified", gmtModified);
				}
				
			}
			PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(listQuestions);
			long total = pageInfo.getTotal();
			dataMap.put("total", total);
			dataMap.put("rows", listQuestions);
			resultMap.put("code", 600);
			resultMap.put("message", "成功");
			resultMap.put("data", dataMap);
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

}
