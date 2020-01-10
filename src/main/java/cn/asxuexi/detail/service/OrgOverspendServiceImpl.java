package cn.asxuexi.detail.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.asxuexi.dao.PersonalCollectionsDao;
import cn.asxuexi.detail.dao.OrgOverspendDao;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.Token_JWT;

@Service
public class OrgOverspendServiceImpl implements OrgOverspendService {

	@Resource
	private OrgOverspendDao orgOverspendDao;
	@Resource
	private PersonalCollectionsDao collectionDao;

	@Autowired
	private HttpServletRequest request;

	private String asxuexiResource = "/asxuexi_resource/";

	/**
	 * 请求机构详细信息
	 * 
	 * @param orgId
	 *            机构id
	 */
	public Map<String, Object> orgInfo(String orgId) {
		Map<String, Object> orgInfo = null;
		String userId = (String)Token_JWT.verifyToken().get("userId");
		orgInfo = orgOverspendDao.orgInfo(orgId);
		if (orgInfo != null) {
			// 不为空，说明机构存在
			// 格式化地区信息
			String address = (String) orgInfo.get("MergerName");
			address = address.substring(3);
			address = address.replaceAll(",", "");
			orgInfo.put("MergerName", address);
			// 机构是否被浏览者收藏的标识,-1代表没有,1代表被收藏
			orgInfo.put("isCollected", -1);
			int count = collectionDao.countCollectionOrg(orgId, userId);
			if (count != 0) {
				orgInfo.put("isCollected", 1);
			}
			// 获取机构的logo图片
			Map<String, Object> logo = orgOverspendDao.getLogo(orgId);
			if (logo != null) {
				orgInfo.put("logo", logo.get("name"));
			}
			// 判断机构的营业执照是否通过审核,-1代表未通过,1代表通过
			orgInfo.put("licence_status", -1);
			Map<String, Object> validate = orgOverspendDao.getValidate(orgId);
			if ("1".equals(validate.get("status"))) {
				orgInfo.put("licence_status", 1);
			}
			Integer pageView = (Integer)orgInfo.get("page_view");
			pageView++;
			orgInfo.put("page_view", pageView);
			orgOverspendDao.updatePageView(orgId,pageView);
		}
		return orgInfo;
	}

	/**
	 * @作用 添加机构收藏
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 */
	public int insertCollectionOrg(String orgId) {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		int insertCollectionOrgResult = collectionDao.insertCollectionOrg(orgId, userId);
		return insertCollectionOrgResult;
	}

	/**
	 * @作用 删除收藏
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 */
	public Map<String, Object> UpdateCollectionOrg(String orgId) {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		Map<String, Object> result=new HashMap<>();
		int code=400;
		String message="failure";
		//int updateCollectionOrg = 2;
		try {
			if (!("null".equals(userId)) && null != userId) {
				collectionDao.deleteCollectionByOrgId(orgId, userId);
				code=600;
				message="success";
			}
		} catch (Exception e) {
			code=401;
		}
		result.put("code", code);
		result.put("message", message);
		return result;
	}

	/**
	 * @作用 查询出机构下前5的课程
	 * @param orgId
	 *            机构id
	 */
	public List<Map<String, Object>> listTopCourse(String orgId) {
		List<Map<String, Object>> listTopCourse = null;
		// 判断是否通过审核
		long time = System.currentTimeMillis() / 1000;// 用来判断课程结束时间
		listTopCourse = orgOverspendDao.listTopCourse(time, orgId);
		return listTopCourse;
	}

	/**
	 * @作用 插入信息到org_ask_answer数据库
	 * @param question
	 *            问题
	 */
	public int insertQuestion(String question, String orgId) {
		RandomTool orgCourseTool = new RandomTool();
		String id = orgCourseTool.randomId("orgAsk");// 产生id
		String userId = (String)Token_JWT.verifyToken().get("userId");
		LocalDateTime now = LocalDateTime.now();
		int insertQuestion = orgOverspendDao.insertQuestion(id, userId, orgId, question, now);
		return insertQuestion;
	}

	/**
	 * @作用 查询信息到org_ask_answer数据库
	 * @param page
	 *            当前页数
	 * @param rows
	 *            一页几行
	 * @return map 基于jqgrid格式
	 */
	public Map<String, Object> listOrgQuestion(int page, int rows, String orgId) {
		Map<String, Object> fhz = new HashMap<String, Object>(16);
		// 查询总数
		int totalRecords = orgOverspendDao.countQuestion(orgId);
		int totalPages = totalRecords % rows == 0 ? totalRecords / rows : totalRecords / rows + 1;
		if (page > totalPages) {
			page = totalPages;
		}
		if (totalPages == 0) {
			page = 1;//
			totalPages = 1;
		}
		List<Map<String, Object>> listOrgQuestion = orgOverspendDao.listOrgQuestion(orgId, page, rows);
		for (Map<String, Object> map : listOrgQuestion) {
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
		fhz.put("date", listOrgQuestion);
		fhz.put("totalPages", totalPages);
		fhz.put("totalRecords", totalRecords);
		fhz.put("curPage", page);
		return fhz;
	}

}
