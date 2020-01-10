package cn.asxuexi.service;

import java.util.List;
import java.util.Map;

public interface PersonalCollectionsService {

	Map<String, Object> getCourseCollections(String user_id);

	List<List<Map<String, Object>>> getSchoolCollections(String user_id);

	int deleteCollection(String courseId, String orgId);

}