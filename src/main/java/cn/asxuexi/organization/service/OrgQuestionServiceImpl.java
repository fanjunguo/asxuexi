package cn.asxuexi.organization.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.asxuexi.organization.dao.OrgQuestionDao;
import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.JsonData;

@Service
public class OrgQuestionServiceImpl implements OrgQuestionService {
	@Resource
	private OrgQuestionDao orgQuestionDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public JsonData listOrgCourses() {
		String orgId = GetOrgIdFromRedis.getOrgId();
		JsonData data = JsonData.error();
		try {
			List<Map<String, Object>> listOrgCourses = orgQuestionDao.listOrgCourses(orgId);
			data = JsonData.success(listOrgCourses);
		} catch (Exception e) {
			data = JsonData.exception();
			logger.error("发生异常", e);
		}
		return data;
	}

	@Override
	public JsonData listOrgQuestions(int page, int rows, int status) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		JsonData data = JsonData.error();
		try {
			PageHelper.startPage(page, rows);
			List<Map<String, Object>> listOrgQuestions = orgQuestionDao.listOrgQuestions(orgId, status);
			PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(listOrgQuestions);
			long total = pageInfo.getTotal();
			for (Map<String, Object> question : listOrgQuestions) {
				Timestamp create = (Timestamp) question.get("gmt_create");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String gmtCreate = format.format(create);
				question.put("gmt_create", gmtCreate);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", listOrgQuestions);
			data = JsonData.success(map);
		} catch (Exception e) {
			data = JsonData.exception();
			logger.error("发生异常", e);
		}
		return data;
	}

	@Override
	public JsonData listCourseQuestions(int page, int rows, int status, String courseId) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		JsonData data = JsonData.error();
		try {
			PageHelper.startPage(page, rows);
			List<Map<String, Object>> listCourseQuestions = orgQuestionDao.listCourseQuestions(orgId, status, courseId);
			PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(listCourseQuestions);
			long total = pageInfo.getTotal();
			for (Map<String, Object> question : listCourseQuestions) {
				Timestamp create = (Timestamp) question.get("gmt_create");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String gmtCreate = format.format(create);
				question.put("gmt_create", gmtCreate);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", listCourseQuestions);
			data = JsonData.success(map);
		} catch (Exception e) {
			data = JsonData.exception();
			logger.error("发生异常", e);
		}
		return data;
	}

	@Override
	public JsonData listAllCourseQuestions(int page, int rows, int status) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		JsonData data = JsonData.error();
		try {
			PageHelper.startPage(page, rows);
			List<Map<String, Object>> listCourseQuestions = orgQuestionDao.listAllCourseQuestions(orgId, status);
			PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(listCourseQuestions);
			long total = pageInfo.getTotal();
			for (Map<String, Object> question : listCourseQuestions) {
				Timestamp create = (Timestamp) question.get("gmt_create");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String gmtCreate = format.format(create);
				question.put("gmt_create", gmtCreate);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", listCourseQuestions);
			data = JsonData.success(map);
		} catch (Exception e) {
			data = JsonData.exception();
			logger.error("发生异常", e);
		}
		return data;
	}

	@Override
	public JsonData updateAnswer(String questionId, String type, String answer) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		JsonData data = JsonData.error();
		try {
			int update = 0;
			if ("org".equals(type)) {
				update = orgQuestionDao.updateOrgAnswer(questionId, orgId, answer, LocalDateTime.now());
			}
			if ("course".equals(type)) {
				update = orgQuestionDao.updateCourseAnswer(questionId, orgId, answer, LocalDateTime.now());
			}
			if (update == 1) {
				data = JsonData.success();
			}
		} catch (Exception e) {
			data = JsonData.exception();
			logger.error("发生异常", e);
		}
		return data;
	}

	@Override
	public JsonData updateStick(String questionId, String type, boolean stick) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		JsonData data = JsonData.error();
		try {
			int update = 0;
			if ("org".equals(type)) {
				if (stick) {
					update = orgQuestionDao.updateOrgStick(questionId, orgId, LocalDateTime.now(), LocalDateTime.now());
				} else {
					update = orgQuestionDao.updateOrgStick(questionId, orgId, null, LocalDateTime.now());
				}
			}
			if ("course".equals(type)) {
				if (stick) {
					update = orgQuestionDao.updateCourseStick(questionId, orgId, LocalDateTime.now(),
							LocalDateTime.now());
				} else {
					update = orgQuestionDao.updateCourseStick(questionId, orgId, null, LocalDateTime.now());
				}
			}
			if (update == 1) {
				data = JsonData.success();
			}
		} catch (Exception e) {
			data = JsonData.exception();
			logger.error("发生异常", e);
		}
		return data;
	}

}
