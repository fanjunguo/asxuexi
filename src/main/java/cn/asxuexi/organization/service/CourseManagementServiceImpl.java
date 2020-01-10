package cn.asxuexi.organization.service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.asxuexi.organization.dao.CourseManagementDao;
import cn.asxuexi.organization.entity.CourseDO;
import cn.asxuexi.search.tool.ElasticsearchTool;
import cn.asxuexi.tool.DateTimeTool;
import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.Token_JWT;

@Service
public class CourseManagementServiceImpl implements CourseManagementService {
	@Resource
	private CourseManagementDao courseManagementDao;
	@Autowired
	private ElasticsearchTool elasticsearchTool;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Map<String, Object> listCourses(int page, int rows, String orgId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(orgId)) {
			orgId = GetOrgIdFromRedis.getOrgId();
		}
		try {
			PageHelper.startPage(page, rows);
			List<Map<String, Object>> listCourses = courseManagementDao.listCourses(orgId);
			PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(listCourses);
			long total = pageInfo.getTotal();
			resultMap.put("code", 600);
			resultMap.put("message", "成功");
			dataMap.put("rows", listCourses);
			dataMap.put("total", total);
			resultMap.put("data", dataMap);
		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
			dataMap.put("rows", new ArrayList<Object>());
			dataMap.put("total", 0);
			resultMap.put("data", dataMap);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getCourse(String courseId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String userId = (String) Token_JWT.verifyToken().get("userId");
		CourseDO course = courseManagementDao.getCourse(userId, courseId);
		if (course != null) {
			// 格式化课程安排中的时间字符串
			List<Map<String, Object>> listCourseTimetable = course.getCourseTimetable();
			for (Map<String, Object> courseTimetable : listCourseTimetable) {
				Date chapterDate = (Date) courseTimetable.get("chapter_date");
				String chapterDateStr = DateTimeTool.getDateStr(chapterDate.getTime() / 1000);
				courseTimetable.put("chapter_date", chapterDateStr);
			}
		}
		resultMap.put("code", 600);
		resultMap.put("message", "成功");
		resultMap.put("data", course);
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> listOrgs() {
		String userId = (String) Token_JWT.verifyToken().get("userId");
		return courseManagementDao.listOrgsByUserId(userId);
	}

	@Override
	public Map<String, Object> deleteCourses(String courseIdList) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String userId = (String) Token_JWT.verifyToken().get("userId");
		String orgId = GetOrgIdFromRedis.getOrgId();
		MDC.put("userId", userId);
		try {
			List<String> list = JSON.parseArray(courseIdList, String.class);
			int result = courseManagementDao.updateCourseStatus(list);
			if (result == list.size()) {
				// 删除课程索引文档
				elasticsearchTool.deleteCourseDocumentsById("course", list);
				resultMap.put("code", 600);
				resultMap.put("message", "成功");
				logger.info("机构[ {} ]，更改表[course]的记录[ {} ]的字段[status]为 -1 ", orgId, list);
			} else {
				resultMap.put("code", 400);
				resultMap.put("message", "修改失败");
			}

		} catch (Exception e) {
			resultMap.put("code", 401);
			resultMap.put("message", "发生异常");
			logger.error("发生异常：", e);
		}
		return resultMap;
	}

	@Override
	@Transactional
	public Map<String, Object> addCourse(String basicInfo, String packageArray, String pictureArray, String videoArray,
			String courseTimetable, String orgArray) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> orgs = JSON.parseArray(orgArray, String.class);
		List<String> savedCourseIdList = new ArrayList<String>();
		for (String orgId : orgs) {
			Map<String, Object> basicInfoMap = JSONObject.parseObject(basicInfo, new TypeReference<Map<String, Object>>() {
			});
			List<String> courseSortArray = JSON.parseArray((String) basicInfoMap.get("courseSort"), String.class);
			List<Map<String, Object>> packageList = JSONObject.parseObject(packageArray,
					new TypeReference<List<Map<String, Object>>>() {
					});
			List<Map<String, Object>> pictureList = JSONObject.parseObject(pictureArray,
					new TypeReference<List<Map<String, Object>>>() {
					});
			List<Map<String, Object>> videoList = JSONObject.parseObject(videoArray,
					new TypeReference<List<Map<String, Object>>>() {
					});
			List<Map<String, Object>> courseTimetableList = JSONObject.parseObject(courseTimetable,
					new TypeReference<List<Map<String, Object>>>() {
					});
			LocalDateTime now = LocalDateTime.now();
			String courseId = RandomTool.randomId("course");
			int insertCourse = courseManagementDao.insertCourse(courseId, basicInfoMap, orgId, now);
			int insertCourseSort = courseManagementDao.insertCourseSort(courseId, courseSortArray);
			if (insertCourse == 1 && insertCourseSort == 1) {
				// 基本信息保存完毕，保存套餐，图片，视频信息
				for (Map<String, Object> coursePackage : packageList) {
					// 每个套餐生成id
					coursePackage.put("packageId", RandomTool.randomId("package"));
				}
				int insertCoursePackage = courseManagementDao.insertCoursePackage(courseId, packageList, now);
				for (Map<String, Object> picture : pictureList) {
					// 每个图片生成id
					picture.put("imgId", RandomTool.randomId("img"));
				}
				int insertImg = courseManagementDao.insertImg(courseId, pictureList, now);
				int insertCourseVideo = 1;
				// 视频列表不为空
				if (videoList.size() != 0) {
					for (Map<String, Object> video : videoList) {
						// 每个视频生成id
						video.put("videoId", RandomTool.randomId("video"));
					}
					insertCourseVideo = courseManagementDao.insertCourseVideo(courseId, videoList, now);
				}
				int insertCourseTimetable = 1;
				// 课程安排信息列表不为空
				if (courseTimetableList.size() != 0) {
					for (Map<String, Object> map : courseTimetableList) {
						map.put("chapterId", RandomTool.randomId("chapter"));
					}
					// 保存课程安排信息
					insertCourseTimetable = courseManagementDao.insertCourseTimetable(courseId, courseTimetableList, now);
				}
				if (insertCoursePackage == 1 && insertImg == 1 && insertCourseVideo == 1 && insertCourseTimetable == 1) {
					savedCourseIdList.add(courseId);
				}
			}
		}
		resultMap.put("code", 600);
		resultMap.put("message", "新增成功");
		resultMap.put("data", savedCourseIdList);
		return resultMap;
	}

	@Override
	@Transactional
	public Map<String, Object> editCourse(String courseId, String basicInfo, String insertPackageArray,
			String deletePackageArray, String updatePackageArray, String pictureArray, String videoArray,
			String insertCourseTimetableArray, String deleteCourseTimetableArray, String updateCourseTimetableArray) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		LocalDateTime now = LocalDateTime.now();
		Map<String, Object> basicInfoMap = JSONObject.parseObject(basicInfo, new TypeReference<Map<String, Object>>() {
		});
		if (basicInfoMap.size() != 0) {
			courseManagementDao.updateCourse(courseId, basicInfoMap, now);
			List<String> courseSortList = JSON.parseArray((String) basicInfoMap.get("courseSort"), String.class);
			courseManagementDao.deleteCourseSort(courseId);
			courseManagementDao.insertCourseSort(courseId, courseSortList);
		}
		List<Map<String, Object>> insertPackageList = JSONObject.parseObject(insertPackageArray,
				new TypeReference<List<Map<String, Object>>>() {
				});
		if (insertPackageList.size() != 0) {
			for (Map<String, Object> coursePackage : insertPackageList) {
				// 每个套餐生成id
				coursePackage.put("packageId", RandomTool.randomId("package"));
			}
			courseManagementDao.insertCoursePackage(courseId, insertPackageList, now);
		}
		List<Map<String, Object>> updatePackageList = JSONObject.parseObject(updatePackageArray,
				new TypeReference<List<Map<String, Object>>>() {
				});
		if (updatePackageList.size() != 0) {
			courseManagementDao.updatePackage(courseId, updatePackageList, now);
		}
		List<String> deletePackageList = JSONObject.parseObject(deletePackageArray, new TypeReference<List<String>>() {
		});
		if (deletePackageList.size() != 0) {
			courseManagementDao.updatePackageStatus(deletePackageList, now);
		}
		List<Map<String, Object>> pictureList = JSONObject.parseObject(pictureArray,
				new TypeReference<List<Map<String, Object>>>() {
				});
		courseManagementDao.deleteImg(courseId);
		if (pictureList.size() != 0) {
			for (Map<String, Object> picture : pictureList) {
				picture.put("imgId", RandomTool.randomId("img"));
			}
			courseManagementDao.insertImg(courseId, pictureList, now);
		}
		List<Map<String, Object>> videoList = JSONObject.parseObject(videoArray,
				new TypeReference<List<Map<String, Object>>>() {
				});
		courseManagementDao.deleteCourseVideo(courseId);
		if (videoList.size() != 0) {
			for (Map<String, Object> video : videoList) {
				video.put("videoId", RandomTool.randomId("video"));
			}
			courseManagementDao.insertCourseVideo(courseId, videoList, now);
		}
		List<Map<String, Object>> insertCourseTimetableList = JSONObject.parseObject(insertCourseTimetableArray,
				new TypeReference<List<Map<String, Object>>>() {
				});
		if (insertCourseTimetableList.size() != 0) {
			for (Map<String, Object> map : insertCourseTimetableList) {
				map.put("chapterId", RandomTool.randomId("chapter"));
			}
			courseManagementDao.insertCourseTimetable(courseId, insertCourseTimetableList, now);
		}
		List<Map<String, Object>> updateCourseTimetableList = JSONObject.parseObject(updateCourseTimetableArray,
				new TypeReference<List<Map<String, Object>>>() {
				});
		if (updateCourseTimetableList.size() != 0) {
			courseManagementDao.updateCourseTimetable(courseId, updateCourseTimetableList, now);
		}
		List<String> deleteCourseTimetableList = JSONObject.parseObject(deleteCourseTimetableArray,
				new TypeReference<List<String>>() {
				});
		if (deleteCourseTimetableList.size() != 0) {
			courseManagementDao.updateCourseTimetableStatus(deleteCourseTimetableList, now);
		}
		resultMap.put("code", 600);
		resultMap.put("message", "修改成功");
		return resultMap;
	}

}
