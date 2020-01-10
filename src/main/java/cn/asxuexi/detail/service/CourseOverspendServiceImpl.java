package cn.asxuexi.detail.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.asxuexi.detail.dao.CourseOverspendDao;
import cn.asxuexi.tool.DateTimeTool;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.Token_JWT;

/**
 * @author 张顺
 * @作用 主要针对课程的详细页面 包含一些问答模块
 */
@Service
public class CourseOverspendServiceImpl implements CourseOverspendService {

	@Resource
	private CourseOverspendDao courseOverspendDao;

	@Autowired
	private HttpServletRequest request;

	private String asxuexiResource = "/asxuexi_resource/";

	/**
	 * @作用 请求课程的详细信息
	 * @author 张顺
	 * @return 课程详细信息
	 */
	@Override
	public Map<String, Object> courseInfo(String courseId) {
		Map<String, Object> hashMap = null;
		List<Map<String, Object>> courseInfo = courseOverspendDao.courseInfo(courseId);
		if (courseInfo.size()!=0) {
			hashMap = new HashMap<String, Object>();
			// 课程的在线，下线状态
			hashMap.put("courseStatus", courseInfo.get(0).get("courseStatus"));
			// 机构的在线，下线状态
			hashMap.put("orgStatus", courseInfo.get(0).get("status"));
			List<Map<String, Object>> listparent = new ArrayList<Map<String, Object>>(10);
			String sortGrade = (String) courseInfo.get(0).get("sort_grade");
			String sortId = (String) courseInfo.get(0).get("sort_id");
			String sortName = (String) courseInfo.get(0).get("sort_name");
			if ("3".equals(sortGrade)) {
				Map<String, Object> parentSort = courseOverspendDao.getParentSort(sortId);
				Map<String, Object> parentSort2 = courseOverspendDao
						.getParentSort(parentSort.get("sort_id").toString());
				if (parentSort.size() > 0 && parentSort2.size() > 0) {
					listparent.add(parentSort);
					listparent.add(parentSort2);
				}
			} else if ("2".equals(sortGrade)) {
				Map<String, Object> parentSort = courseOverspendDao.getParentSort(sortId);
				if (parentSort.size() > 0) {
					listparent.add(parentSort);
				}
			} else {
				listparent = null;
			}
			String pricetypeId = (String) courseInfo.get(0).get("pricetypeid");
			if ("06".equals(pricetypeId) || "07".equals(pricetypeId)) {
				hashMap.put("oldprice", " ");
				hashMap.put("showingprice", " ");
			} else {
				hashMap.put("oldprice", "￥" + courseInfo.get(0).get("oldprice"));
				hashMap.put("showingprice", "￥" + courseInfo.get(0).get("showingprice"));
			}
			List<String> courseImg = new ArrayList<String>(10);
			for (Map<String, Object> map : courseInfo) {
				String imgNames = (String) map.get("img_name");
				courseImg.add(imgNames);
			}
			// 转化时间
			DateTimeTool timeAndDate = new DateTimeTool();
			String courseBegin = timeAndDate.transForDate((Long) courseInfo.get(0).get("course_begin"));
			String courseEnd = timeAndDate.transForDate((Long) courseInfo.get(0).get("course_end"));
			Map<String, Object> courseVideo = courseOverspendDao.getCourseVideo(courseId);
			if (courseVideo != null) {
				hashMap.put("video", courseVideo.get("video_name"));
			}
			// 请求本课程是否收藏
			String userId = (String)Token_JWT.verifyToken().get("userId");
			int countCollectionCourse = 0;
			if (null != userId) {
				countCollectionCourse = courseOverspendDao.countCollectionCourse(courseId, userId);
			}
			hashMap.put("orgName", courseInfo.get(0).get("orgName"));
			hashMap.put("orgTel", courseInfo.get(0).get("tel"));
			hashMap.put("orgAddress", courseInfo.get(0).get("address"));
			// 处理下地址格式
			String address = (String) courseInfo.get(0).get("MergerName");
			address = address.substring(3);
			address = address.replaceAll(",", "");
			hashMap.put("orgMergerName", address);

			hashMap.put("courseName", courseInfo.get(0).get("coursename"));
			hashMap.put("priceTypeName", courseInfo.get(0).get("typename"));
			hashMap.put("teacher", courseInfo.get(0).get("teacher"));
			hashMap.put("Des", courseInfo.get(0).get("des"));
			hashMap.put("orgUrl", courseInfo.get(0).get("url"));
			// 父级
			hashMap.put("sort", listparent);
			hashMap.put("sortGrade", sortGrade);
			hashMap.put("sortName", sortName);
			hashMap.put("sortId", sortId);
			hashMap.put("courseImg", courseImg);
			hashMap.put("courseBegin", courseBegin);
			hashMap.put("courseEnd", courseEnd);
			hashMap.put("orgId", courseInfo.get(0).get("org_id"));
			hashMap.put("courseId", courseInfo.get(0).get("courseid"));
			hashMap.put("collectionCourse", countCollectionCourse);
			hashMap.put("lat", courseInfo.get(0).get("lat"));
			hashMap.put("lng", courseInfo.get(0).get("lng"));
		}

		// 获取分类信息
		return hashMap;
	}

	/**
	 * @作用 添加收藏
	 * @param courseId 课程id
	 * @param orgId 机构id
	 */
	@Override
	public int insertCollectionCourse(String courseId, String orgId) {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		int collectionCourse = -1;
		if (null != userId) {
			collectionCourse = courseOverspendDao.insertCollectionCourse(courseId, orgId, userId);
		}
		return collectionCourse;
	}

	/**
	 * @作用 删除收藏
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 */
	@Override
	public int updateCollectionCourse(String courseId, String orgId) {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		int collectionCourse = -1;
		if (null != userId) {
			collectionCourse = courseOverspendDao.UpdateCollectionCourse(courseId, orgId, userId);
		}
		return collectionCourse;
	}

	/**
	 * @作用 插入问题
	 * @param orgId
	 *            机构id
	 * @param question
	 *            问题内容
	 * @return {@link Integer} 0或1
	 */
	@Override
	public int insertQuestion(String orgId, String question, String courseId) {
		int insertQuestion = 2;
		// 生成随机id
		String userId = (String)Token_JWT.verifyToken().get("userId");
		if (!("null".equals(userId)) && null != userId) {
			RandomTool orgCourseTool = new RandomTool();
			String randomId = orgCourseTool.randomId("ask");
			LocalDateTime now = LocalDateTime.now();
			insertQuestion = courseOverspendDao.insertQuestion(randomId, userId, courseId, orgId, question, now);
		}
		return insertQuestion;
	}

	/**
	 * @作用 获取课程问题及答案
	 * @param page
	 *            当前页数
	 * @param rows
	 *            一页几个
	 */
	public Map<String, Object> listCourseQuestion(int page, int rows, String courseId) {
		Map<String, Object> fhz = new HashMap<String, Object>(16);
		// 查询总数
		int totalRecords = courseOverspendDao.countQuestion(courseId);
		int totalPages = totalRecords % rows == 0 ? totalRecords / rows : totalRecords / rows + 1;
		if (page > totalPages) {
			page = totalPages;
		}
		if (totalPages == 0) {
			page = 1;
			totalPages = 1;
		}
		List<Map<String, Object>> listCourseQuestion = courseOverspendDao.listCourseQuestion(courseId, page, rows);
		for (Map<String, Object> map : listCourseQuestion) {
			Timestamp time = (Timestamp) map.get("gmt_create");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String format = sdf.format(time);
			map.put("gmt_create", format);
			String photo = (String) map.get("photo");
			if ("".equals(photo) || photo == null) {

			} else {
				String scheme = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ asxuexiResource + "user/" + map.get("userId") + "/" + photo;
				map.put("photo", scheme);
			}
		}
		fhz.put("date", listCourseQuestion);
		fhz.put("totalPages", totalPages);
		fhz.put("totalRecords", totalRecords);
		fhz.put("curPage", page);
		return fhz;
	}
}
