package cn.asxuexi.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PersonalCollectionsDaoImpl implements PersonalCollectionsDao {
	@Resource
	private JdbcTemplate template;

	/**
	 * @author fanjunguo
	 * @description 查询收藏的课程
	 * @param user_id
	 * @param currentTime
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCourseCollections(String user_id, String currentTime) {
		String sql = "";
		if (null == currentTime) {
			sql = "SELECT dbo.collection.course_id, dbo.course.coursename, dbo.course_img.img_name, dbo.course.course_end, dbo.course.course_charging, dbo.org.orgname, \n"
					+ "               dbo.course.status\n" + "FROM  dbo.course INNER JOIN\n"
					+ "               dbo.org ON dbo.course.org_id = dbo.org.orgid INNER JOIN\n"
					+ "               dbo.collection INNER JOIN\n"
					+ "               dbo.course_img ON dbo.collection.course_id = dbo.course_img.course_id ON dbo.course.courseid = dbo.collection.course_id\n"
					+ "where (dbo.collection.user_id =?) and  (dbo.course_img.img_order = '1')";
			// sql="SELECT dbo.collection.course_id, dbo.course_img.coursename,
			// dbo.course_img.img_name, dbo.course_img.showingprice,
			// dbo.course_img.course_end, \n" +
			// " dbo.course_img.typename, dbo.course_img.pricetypeid, dbo.org.orgname\n" +
			// "FROM dbo.collection INNER JOIN\n" +
			// " dbo.course_img ON dbo.collection.course_id = dbo.course_img.course_id INNER
			// JOIN\n" +
			// " dbo.org ON dbo.collection.org_id = dbo.org.orgid\n"+
			// "WHERE (dbo.course_img.img_order = '1') AND (dbo.collection.user_id =?)";
		} else {
			sql = "SELECT dbo.collection.course_id, dbo.course.coursename, dbo.course_img.img_name, dbo.course.course_end, dbo.course.course_charging, dbo.org.orgname, \n"
					+ "               dbo.course.status\n" + "FROM  dbo.course INNER JOIN\n"
					+ "               dbo.org ON dbo.course.org_id = dbo.org.orgid INNER JOIN\n"
					+ "               dbo.collection INNER JOIN\n"
					+ "               dbo.course_img ON dbo.collection.course_id = dbo.course_img.course_id ON dbo.course.courseid = dbo.collection.course_id\n"
					+ "WHERE (dbo.course_img.img_order = '1') AND (dbo.collection.user_id = ?) and dbo.course.course_end<"
					+ currentTime + " ";
		}

		List<Map<String, Object>> course = template.queryForList(sql, new Object[] { user_id });
		return course;
	}

	/**
	 * 
	 * @author fanjunguo
	 * @description 查询收藏的机构
	 * @param user_id
	 * @param currentTime
	 * @return
	 */
	@Override
	public List<List<Map<String, Object>>> getSchoolCollections(String user_id, String currentTime) {
		List<List<Map<String, Object>>> list = new ArrayList<>();
		String getschool_sql = ""
				+ "SELECT dbo.collection.org_id, dbo.logo.name, dbo.org.orgname, dbo.org.address, dbo.org.tel "
				+ "FROM  dbo.collection "
				+ "INNER JOIN dbo.logo ON dbo.collection.org_id = dbo.logo.org_id "
				+ "INNER JOIN dbo.org ON dbo.collection.org_id = dbo.org.orgid "
				+ "WHERE (dbo.collection.user_id = ?) AND (dbo.collection.course_id = '0')";
		List<Map<String, Object>> schoollist = template.queryForList(getschool_sql, new Object[] { user_id });
		list.add(schoollist);
		// 再取出这些机构下的课程,作为推荐课程
		for (Map<String, Object> map : schoollist) {
			String orgid = (String) map.get("org_id");// 收藏的学校的id
			String sql = ""
					+ "SELECT dbo.course.coursename, dbo.course_img.img_name " 
					+ "FROM  dbo.course "
					+ "INNER JOIN dbo.course_img ON dbo.course.courseid = dbo.course_img.course_id "
					+ "WHERE dbo.course.org_id = ? AND dbo.course_img.img_order = 1 "
					+ "ORDER BY dbo.course.gmt_create DESC";
			List<Map<String, Object>> courseOfCollection = template.queryForList(sql, orgid);
			list.add(courseOfCollection);
		}
		return list;
	}

	/**
	 * @作用 添加机构收藏
	 * @param orgId
	 *            机构id
	 * @param userId
	 *            用户id
	 */
	@Override
	public int insertCollectionOrg(String orgId, String userId) {
		String sql = "insert into [collection] ([user_id],[course_id] ,[org_id]) values (?,'0',?)";
		int update = template.update(sql, new Object[] { userId, orgId });
		return update;
	}

	/**
	 * @作用 删除机构收藏
	 * @param orgId
	 *            机构id
	 * @param userId
	 *            用户id
	 */
	@Override
	public int deleteCollectionByOrgId(String orgId, String userId) {
		String sql = "delete [collection] where [user_id]=? and [course_id]=? and [org_id]=? ";
		int update = template.update(sql, new Object[] { userId, "0", orgId });
		return update;
	}

	/**
	 * 
	 * @author fanjunguo
	 * @description 删除课程收藏
	 * @param courseId
	 * @return
	 */
	@Override
	public int deleteCollectionByCourseId(String courseId, String userId) {
		String sql = "delete from collection where course_id=? AND user_id = ?";
		int result = template.update(sql, new Object[] { courseId, userId });
		return result;
	}

	/**
	 * @作用 查询是否收藏机构
	 * @param orgId
	 *            机构id
	 * @param userId
	 *            用户id
	 */
	@Override
	public int countCollectionOrg(String orgId, String userId) {
		String sql = "select count(*) from collection where [course_id] = '0' and org_id=? and [user_id]=?";
		Integer queryForObject = template.queryForObject(sql, new Object[] { orgId, userId }, Integer.class);
		return queryForObject;
	}
}
