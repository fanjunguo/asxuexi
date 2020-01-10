package cn.asxuexi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.dao.PersonalCollectionsDao;
import cn.asxuexi.tool.Token_JWT;

@Service
public class PersonalCollectionsServiceImpl implements PersonalCollectionsService {
	@Resource
	PersonalCollectionsDao dao;
	
	@Override
	public Map<String, Object> getCourseCollections(String currentTime) {

		String userId = (String)Token_JWT.verifyToken().get("userId");
		List<Map<String, Object>> course = dao.getCourseCollections(userId,currentTime);
		long systemTime = System.currentTimeMillis()/1000;
		Map<String , Object> json=new HashMap<>();
		json.put("data", course);
		json.put("systemTime", systemTime);
		return json;
	}
	
	@Override
	public int deleteCollection(String courseId,String orgId) {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		int result;
		//如果课程id不为空,则根据课程id删除收藏;否则根据机构id删除收藏
		if (null!=courseId||!"".equals(courseId)) {
			result=dao.deleteCollectionByCourseId(courseId, userId);
		} else {
			result=dao.deleteCollectionByOrgId(orgId, userId);
		}
		return result;
	}
	
	
	@Override
	public List<List<Map<String, Object>>> getSchoolCollections(String currentTime){
		String userId = (String)Token_JWT.verifyToken().get("userId");
		List<List<Map<String, Object>>> school = dao.getSchoolCollections(userId,currentTime);
		return school;
	}
}
