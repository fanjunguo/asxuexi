package cn.asxuexi.personalAskAnswer.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.personalAskAnswer.dao.PersonalAskAnswerDao;
import cn.asxuexi.tool.Token_JWT;

@Service
public class PersonalAskAnswerServiceImpl implements PersonalAskAnswerService {

	@Resource
	private PersonalAskAnswerDao personalAskAnswerDao;

	/**
	 * @作用 请求用户提问的课程数据
	 * @param table
	 *            查询0：org_ask_answer表或1: course_ask_answer表
	 * @param page
	 *            查询页数
	 **/
	@Override
	public Map<String, Object> listAnswer(String table, int page,int rows) {
		if (rows<5) {
			rows=5;
		}
		String userId = (String)Token_JWT.verifyToken().get("userId");
		Map<String, Object> fhz = new HashMap<String, Object>(16);
		if ("1".equals(table)) {// 查询机构表
			int countOrgAskAnswering = personalAskAnswerDao.countCourseAskAnswer(userId);
			// 分页
			int totalPages = countOrgAskAnswering % rows == 0 ? countOrgAskAnswering / rows
					: countOrgAskAnswering / rows + 1;
			if (page > totalPages) {
				page = totalPages;
			}
			if (totalPages == 0) {
				page = 1;
				totalPages = 1;
			}
			List<Map<String, Object>> listCourseAskAnswering = personalAskAnswerDao.listCourseAskAnswer(page, rows,
					userId);
			for (Map<String, Object> map : listCourseAskAnswering) {
				// 转换时间
				Timestamp gmt_create = (Timestamp) map.get("gmt_create");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String formatCreate = sdf.format(gmt_create);
				Timestamp gmt_modified = (Timestamp) map.get("gmt_modified");
				if (null == gmt_modified) {
					map.put("gmt_modified", null);
				} else {
					String formatModified = sdf.format(gmt_modified);
					map.put("gmt_modified", formatModified);
				}
				map.put("gmt_create", formatCreate);
			}
			fhz.put("date", listCourseAskAnswering);
			fhz.put("totalPages", totalPages);
			fhz.put("totalRecords", countOrgAskAnswering);
			fhz.put("curPage", page);// ��ǰҳ
		} else {
			int countOrgAskAnswering = personalAskAnswerDao.countOrgAskAnswer(userId);
			// 分页
			int totalPages = countOrgAskAnswering % rows == 0 ? countOrgAskAnswering / rows
					: countOrgAskAnswering / rows + 1;
			if (page > totalPages) {
				page = totalPages;
			}
			if (totalPages == 0) {
				page = 1;
				totalPages = 1;
			}
			List<Map<String, Object>> listCourseAskAnswering = personalAskAnswerDao.listOrgAskAnswer(page, rows,
					userId);
			for (Map<String, Object> map : listCourseAskAnswering) {
				// 转换时间
				Timestamp gmt_create = (Timestamp) map.get("gmt_create");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String formatCreate = sdf.format(gmt_create);
				Timestamp gmt_modified = (Timestamp) map.get("gmt_modified");
				if (null == gmt_modified) {
					map.put("gmt_modified", null);
				} else {
					String formatModified = sdf.format(gmt_modified);
					map.put("gmt_modified", formatModified);
				}
				map.put("gmt_create", formatCreate);
			}
			fhz.put("date", listCourseAskAnswering);
			fhz.put("totalPages", totalPages);
			fhz.put("totalRecords", countOrgAskAnswering);
			fhz.put("curPage", page);
		}
		return fhz;
	}
}
