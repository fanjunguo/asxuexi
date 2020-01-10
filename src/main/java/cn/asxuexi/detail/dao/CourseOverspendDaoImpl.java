package cn.asxuexi.detail.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author 张顺
 * @作用 主要针对课程的详细页面 包含一些问答模块
 */
@Repository
public class CourseOverspendDaoImpl implements CourseOverspendDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * @作用 请求课程的详细信息
	 * @author 张顺
	 * @return 课程详细信息
	 */
	@Override
	public List<Map<String, Object>> courseInfo(String courseId) {
		String sql = "SELECT dbo.course.org_id, dbo.course.courseid, dbo.course.sort_id, dbo.org.orgname, dbo.org.tel, dbo.org.url, dbo.org.address, dbo.org.status AS orgStatus, dbo.areas.MergerName,dbo.org.[lng],dbo.org.[lat], dbo.course.coursename,  dbo.course.showingprice, dbo.course.oldprice,dbo.pricetype.pricetypeid, dbo.pricetype.typename, dbo.course.course_begin, dbo.course.course_end, dbo.course.teacher, dbo.course.des,dbo.course.status as courseStatus, dbo.img.img_name, dbo.img.status, dbo.sort.sort_grade,dbo.sort.sort_name\r\n"
				+ "FROM  dbo.course INNER JOIN\r\n"
				+ "dbo.org ON dbo.course.org_id = dbo.org.orgid INNER JOIN dbo.img ON dbo.course.courseid = dbo.img.course_id INNER JOIN   dbo.areas ON dbo.org.localid = dbo.areas.ID INNER JOIN    dbo.pricetype ON dbo.course.pricetypeid = dbo.pricetype.pricetypeid INNER JOIN    dbo.sort ON dbo.course.sort_id = dbo.sort.sort_id where dbo.course.courseid=? order by dbo.img.status";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, new Object[] { courseId });
		return queryForList;
	}

	/**
	 * 获取父级的信息
	 * 
	 * @author 张顺
	 * @param sortId
	 *            分类编号
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getParentSort(String sortId) {
		Map<String, Object> queryForMap = new HashMap<String, Object>(16);
		String sql = "select sort_id,sort_name,sort_grade from sort where sort_id =(select sort_parentid from sort where sort_id=?)";
		try {
			queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { sortId });
		} catch (Exception e) {
			queryForMap = null;
		}
		return queryForMap;
	}

	/**
	 * 获取课程视频
	 * 
	 * @author 张顺
	 * @param courseId
	 *            课程编号
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getCourseVideo(String courseId) {
		Map<String, Object> queryForMap = null;
		String sql = "select course_id,video_name from course_video where course_id =?";
		try {
			queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { courseId });
		} catch (Exception e) {
			queryForMap = null;
		}
		return queryForMap;
	}

	/**
	 * @作用 查询是否收藏课程
	 * @param courseId
	 *            课程id
	 * @param userId
	 *            用户id
	 */
	@Override
	public int countCollectionCourse(String courseId, String userId) {
		String sql = "select count(*) from collection where [course_id]=? and [user_id]=?";
		Integer queryForObject = jdbcTemplate.queryForObject(sql, new Object[] { courseId, userId }, Integer.class);
		return queryForObject;
	}

	/**
	 * @作用 添加收藏
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 * @param userId
	 *            用户id
	 */
	@Override
	public int insertCollectionCourse(String courseId, String orgId, String userId) {
		String sql = "insert into [collection]([user_id],[course_id] ,[org_id]) values(?,?,?)";
		int update = jdbcTemplate.update(sql, new Object[] { userId, courseId, orgId });
		return update;
	}

	/**
	 * @作用 删除收藏
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 * @param userId
	 *            用户id
	 */
	@Override
	public int UpdateCollectionCourse(String courseId, String orgId, String userId) {
		String sql = "delete [collection] where [user_id]=? and [course_id]=? and [org_id]=? ";
		int update = jdbcTemplate.update(sql, new Object[] { userId, courseId, orgId });
		return update;
	}

	/**
	 * @作用 插入问题
	 * @param id
	 *            问题id
	 * @param userId
	 *            用户id
	 * @param courseId
	 *            课程id
	 * @param orgId
	 *            机构id
	 * @param question
	 *            问题内容
	 * @param gmt_create
	 *            创建时间
	 * @return {@link Integer} 0或1
	 */
	@Override
	public int insertQuestion(String id, String userId, String courseId, String orgId, String question,
			LocalDateTime gmt_create) {
		String sql = "insert into course_ask_answer(id,user_id,course_id,org_id,question,gmt_create) values(?,?,?,?,?,?)";
		int update = jdbcTemplate.update(sql, new Object[] { id, userId, courseId, orgId, question, gmt_create });
		return update;
	}

	/**
	 * @作用 查询当前课程总数
	 * @param courseId
	 *            课程id
	 */
	@Override
	public int countQuestion(String courseId) {
		String sql = "select count(*) from dbo.course_ask_answer where  dbo.course_ask_answer.course_id=?";
		Integer queryForObject = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { courseId });
		return queryForObject;
	}

	/**
	 * @作用 获取课程问题及答案
	 * @param courseId
	 *            课程id
	 * @param page
	 *            当前页数
	 * @param rows
	 *            一页几个
	 */
	@Override
	public List<Map<String, Object>> listCourseQuestion(String courseId, int page, int rows) {

		int toprow = (page - 1) * rows;
		String sql = "SELECT top " + rows
				+ " dbo.course_ask_answer.id, dbo.course_ask_answer.question, dbo.course_ask_answer.answer,dbo.course_ask_answer.gmt_create, dbo.[user].name, dbo.[user].photo,dbo.[user].id as userId FROM  dbo.course_ask_answer INNER JOIN   dbo.[user] ON dbo.course_ask_answer.user_id = dbo.[user].id  where ( dbo.course_ask_answer.id not in (\r\n"
				+ " select top " + toprow
				+ " dbo.course_ask_answer.id from  dbo.course_ask_answer INNER JOIN   dbo.[user] ON dbo.course_ask_answer.user_id = dbo.[user].id where   dbo.course_ask_answer.course_id\r\n"
				+ "=?  order by  dbo.course_ask_answer.stick DESC,dbo.course_ask_answer.gmt_modified DESC, dbo.course_ask_answer.gmt_create DESC))  and dbo.course_ask_answer.course_id =?  order by  dbo.course_ask_answer.stick DESC,dbo.course_ask_answer.gmt_modified DESC, dbo.course_ask_answer.gmt_create DESC";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, new Object[] { courseId, courseId });
		return queryForList;
	}
}
