package cn.asxuexi.detail.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrgOverspendDaoImpl implements OrgOverspendDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * @作用 请求机构信息
	 * @param orgId
	 *            机构id
	 */
	@Override
	public Map<String, Object> orgInfo(String orgId) {
		Map<String, Object> queryForMap = null;
		try {
			String sql = "SELECT dbo.areas.MergerName, dbo.org.orgname, dbo.org.orgid,"
					+ "dbo.org.tel, dbo.org.lat, dbo.org.lng, dbo.org.address, dbo.org.localid, "
					+ "dbo.org.head, dbo.org.des, dbo.org.url, dbo.org.status, dbo.org.page_view,"
					+ "dbo.org.room_number "
					+ "FROM  dbo.areas INNER JOIN dbo.org ON dbo.areas.ID = dbo.org.localid where orgid=?";
			queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { orgId });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return queryForMap;
	}

	@Override
	public Map<String, Object> getValidate(String orgId) {
		Map<String, Object> queryForMap = new HashMap<String, Object>();
		String sql = "SELECT [status]  FROM dbo.[licence_authentication] where org_id=?";
		try {
			queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { orgId });
		} catch (Exception e) {
			// 查不到，说明没有进行营业执照认证
		}
		return queryForMap;
	}

	/**
	 * 查询logo
	 * 
	 * @param orgId
	 *            {@link String} 机构id
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getLogo(String orgId) {
		Map<String, Object> queryForMap = null;
		String sql = "select name ,org_id from Logo where org_id=?";
		try {
			queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { orgId });
		} catch (Exception e) {
		}
		return queryForMap;
	}

	/**
	 * @作用 查询出机构下前5的课程
	 * @param time
	 *            现在时间戳
	 * @param orgId
	 *            机构id
	 */

	@Override
	public List<Map<String, Object>> listTopCourse(long time, String orgId) {
		String sql = " SELECT dbo.course.courseid, dbo.course.coursename, "
				+ " dbo.course_img.img_name "
				+ " FROM dbo.course INNER JOIN dbo.course_img "
				+ " ON dbo.course.courseid = dbo.course_img.course_id "
				+ " WHERE dbo.course.org_id = ? " 
				+ " AND dbo.course.status = 1 "
				+ " AND dbo.course_img.img_order = 1 "
				+ " ORDER BY dbo.course.gmt_create DESC , dbo.course.order_weight DESC ";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, new Object[] { orgId });
		return queryForList;
	}

	/**
	 * @作用 插入信息到org_ask_answer数据库
	 * @param id
	 *            问题id
	 * @param userId
	 *            用户id
	 * @param orgId
	 *            机构Id
	 * @param question
	 *            问题
	 * @param now
	 *            现在时间
	 */
	@Override
	public int insertQuestion(String id, String userId, String orgId, String question, LocalDateTime now) {
		String sql = " insert into org_ask_answer(id,user_id,org_id,question,gmt_create) values(?,?,?,?,?)";
		int update = jdbcTemplate.update(sql, new Object[] { id, userId, orgId, question, now });
		return update;
	}

	/**
	 * @作用 查询当前机构总数
	 * @param courseId
	 *            课程id
	 */
	@Override
	public int countQuestion(String orgId) {
		String sql = "select count(*) from dbo.org_ask_answer where  dbo.org_ask_answer.org_id=?";
		Integer queryForObject = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { orgId });
		return queryForObject;
	}

	/**
	 * @作用 查询信息到org_ask_answer数据库
	 * @param orgId
	 *            机构Id
	 * @param page
	 *            当前页数
	 * @param rows
	 *            一页几行
	 */
	@Override
	public List<Map<String, Object>> listOrgQuestion(String orgId, int page, int rows) {
		int toprow = (page - 1) * rows;
		String sql = "SELECT top " + rows
				+ " dbo.org_ask_answer.id, dbo.org_ask_answer.question, dbo.org_ask_answer.answer,dbo.org_ask_answer.gmt_create, dbo.[user].name, dbo.[user].photo ,dbo.[user].id as userId FROM  dbo.org_ask_answer INNER JOIN   dbo.[user] ON dbo.org_ask_answer.user_id = dbo.[user].id  where ( dbo.org_ask_answer.id not in ("
				+ "	 select top  " + toprow
				+ "  dbo.org_ask_answer.id from  dbo.org_ask_answer INNER JOIN   dbo.[user] ON dbo.org_ask_answer.user_id = dbo.[user].id where   dbo.org_ask_answer.org_id  =?  order by  dbo.org_ask_answer.stick DESC,dbo.org_ask_answer.gmt_modified DESC, dbo.org_ask_answer.gmt_create DESC))  and dbo.org_ask_answer.org_id =?  order by  dbo.org_ask_answer.stick DESC,dbo.org_ask_answer.gmt_modified DESC, dbo.org_ask_answer.gmt_create DESC";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, new Object[] { orgId, orgId });
		return queryForList;
	}

	@Override
	public void updatePageView(String orgId, Integer pageView) {
		String sql = "UPDATE dbo.org SET page_view = ? WHERE dbo.org.orgid = ? ";
		jdbcTemplate.update(sql, pageView, orgId);
	}
}
