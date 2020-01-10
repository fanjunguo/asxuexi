package cn.asxuexi.dao;

import java.util.List;
import java.util.Map;

public interface PersonalCollectionsDao {

	/**
	 * @author fanjunguo
	 * @description 查询收藏的课程
	 * @param user_id
	 * @param currentTime
	 * @return
	 */
	List<Map<String, Object>> getCourseCollections(String user_id, String currentTime);

	/**
	 * 
	 * @author fanjunguo
	 * @description 查询收藏的机构
	 * @param user_id
	 * @param currentTime
	 * @return
	 */
	List<List<Map<String, Object>>> getSchoolCollections(String user_id, String currentTime);

	/**
	 * 
	 * @author fanjunguo
	 * @description 删除课程收藏
	 * @param courseId
	 *            课程ID
	 * @param userId
	 *            用户ID
	 * @return
	 */
	int deleteCollectionByCourseId(String courseId, String userId);

	/**
	 * @author fanjunguo
	 * @description 添加机构收藏
	 * @param orgId
	 * @param userId
	 * @return
	 */
	int insertCollectionOrg(String orgId, String userId);

	/**
	 * @作用 删除机构收藏
	 * @param orgId
	 *            机构id
	 * @param userId
	 *            用户id
	 */
	int deleteCollectionByOrgId(String orgId, String userId);

	/**
	 * @作用 查询是否收藏机构
	 * @param courseId
	 *            课程id
	 * @param userId
	 *            用户id
	 */
	int countCollectionOrg(String orgId, String userId);

}