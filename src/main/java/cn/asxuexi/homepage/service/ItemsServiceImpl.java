package cn.asxuexi.homepage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.asxuexi.homepage.dao.ItemsDao;

@Service
public class ItemsServiceImpl implements ItemsService {
	@Resource
	private ItemsDao itemsDao;
	private Logger logger=LoggerFactory.getLogger(this.getClass());

	
	// forAndroid by:junguo.fan
	private Map<String, List<Map<String, Object>>> resultForAndroid = new HashMap<String, List<Map<String, Object>>>();

	@Autowired
	private HttpServletRequest request;

	@Override
	public Map<String, Object> getItems(String contentType, String cityId) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("cityid".equals(cookie.getName())) {
					cityId = cookie.getValue();
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>(16);
		List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>(10);
		List<Map<String, Object>> data = null;
		List<Map<String, Object>> content = null;
		if ("org".equals(contentType)) {
			data = itemsDao.listDataOfOrg(cityId);
			content = itemsDao.listOrgs(cityId);
			// 判断机构是否认证了机构资质
			List<String> orgIdList = new ArrayList<String>(10);
			for (Map<String, Object> org : content) {
				org.put("org_validation", false);
				orgIdList.add((String) org.get("orgid"));
			}
			List<String> validatedOrgIdList = itemsDao.listValidatedOrgId(orgIdList);
			for (Map<String, Object> org : content) {
				for (String validatedOrgId : validatedOrgIdList) {
					if (validatedOrgId.equals(org.get("orgid").toString())) {
						org.put("org_validation", true);
					}
				}
			}
		}
		if ("course".equals(contentType)) {
			data = itemsDao.listDataOfCourse(cityId);
			content = itemsDao.listCourses(cityId);
			List<String> courseIdList = new ArrayList<String>(10);
			for (Map<String, Object> course : content) {
				courseIdList.add((String) course.get("courseid"));
			}
			List<Map<String, Object>> listPackages = itemsDao.listPackages(courseIdList);
			for (Map<String, Object> course : content) {
				List<Map<String, Object>> packageList = new ArrayList<Map<String, Object>>(10);
				for (Map<String, Object> coursePackage : listPackages) {
					if (course.get("courseid").equals(coursePackage.get("course_id"))) {
						packageList.add(coursePackage);
					}
				}
				course.put("packageList", packageList);
			}
		}
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> rowa = data.get(i);
			if ("1".equals(rowa.get("level_type").toString())) {
				itemsList.add(rowa);
			}
		}
		// android>>>>>>
		resultForAndroid.put("firstLevel", itemsList);
		List<Map<String, Object>> secondLevelList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> thirdLevelList = new ArrayList<Map<String, Object>>();
		// <<<<<<<<< android
		map.put("100000", itemsList);
		for (int j = 0; j < itemsList.size(); j++) {
			List<Map<String, Object>> tabsList = new ArrayList<Map<String, Object>>(10);
			String ownId = itemsList.get(j).get("own_id").toString();
			for (int k = 0; k < data.size(); k++) {
				Map<String, Object> rowb = data.get(k);
				String parentId = rowb.get("parent_id").toString();
				if (parentId.equals(ownId)) {
					tabsList.add(rowb);
					// android
					secondLevelList.add(rowb);
				}
			}
			for (int m = 0; m < tabsList.size(); m++) {
				List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>(10);
				String ownid = tabsList.get(m).get("own_id").toString();
				for (int n = 0; n < content.size(); n++) {
					Map<String, Object> rowc = content.get(n);
					String parentid = rowc.get("parent_id").toString();
					if (parentid.equals(ownid)) {
						contentList.add(rowc);
						// android
						thirdLevelList.add(rowc);
					}
				}
				map.put(ownid, contentList);

			}
			map.put(ownId, tabsList);

		}
		resultForAndroid.put("secondLevel", secondLevelList);
		resultForAndroid.put("thirdLevel", thirdLevelList);
		return map;
	}

	/**
	 * @author fanjunguo
	 * @return 我在类中建了个全局变量resultForAndroid,用来给安卓返回标准的json格式数据.当getItems()方法执行时,resultForAndroid会被赋值.
	 */
	@Override
	public Map<String, List<Map<String, Object>>> getItemsForAndroid() {
		return resultForAndroid;
	}

	/**
	 * 获取首页推荐课程
	 * @param orderRule 排序规则:1-按时间倒序;2-按时间顺序;3-按价格排序
	 * @param cityId 城市ID
	 * 
	 * 2019.8.29 逻辑调整:
	 * 按时间倒序:取最新的课程,并且不能是同一个机构的
	 * 其他情况:随机取8个,不足8个则有多少取多少
	 */
	@Override
	public Map<String, Object> getCourseOfRecommended(int orderRule,String cityId) {
		Map<String, Object> json= new HashMap<>();
		int code=600;
		String message="success";
		List<Map<String, Object>> courseList=itemsDao.getCourseOfRecommended(orderRule,cityId);
		//如果是取最新课程
		if (orderRule==1) {
			List<String> orgIdList=new ArrayList<>();
			List<Map<String, Object>> targetDataList=new ArrayList<>();
			Iterator<Map<String, Object>> iterator = courseList.iterator();
			while (iterator.hasNext()) {
				Map<String, Object> next = iterator.next();
				String orgId = (String) next.get("orgId");
				if (!orgIdList.contains(orgId)) {
					orgIdList.add(orgId);
					targetDataList.add(next);
				}
				if (orgIdList.size()>=8) {
					courseList=targetDataList;
					break;
				}
			}
			
		} 
		
		json.put("data", courseList);
		json.put("code", code);
		json.put("message", message);
		return json;
	}

	//获取首页推荐机构
	@Override
	public Map<String, Object> getOrgOfRecommended(int orderRule, String cityId) {
		
		Map<String, Object> json= new HashMap<>();
		int code=600;
		String message="success";
		try {
			List<Map<String, Object>> orgList = itemsDao.getOrgOfRecommended(orderRule,cityId);
			int length=orgList.size();
			if (length>8) {
				int restNum=length%8;
				List<Map<String, Object>> subList = orgList.subList(0, 8-restNum);
				orgList.addAll(subList);
			} 	
			json.put("data", orgList);
		} catch (Exception e) {
			logger.error("查询首页课程异常",e);
			code=401;
			message="exception";
		}
		json.put("code", code);
		json.put("message", message);
		return json;
	}
	
	

}
