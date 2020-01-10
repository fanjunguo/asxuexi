package cn.asxuexi.personalAskAnswer.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PersonalAskAnswerDaoImpl  implements PersonalAskAnswerDao{
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**
	 * @作用 获取课程问题的总数
	 * @param userId 用户id
	 */
	public int countCourseAskAnswer(String userId) {
		String sql = "select count(*) from course_ask_answer where user_id=? ";
		Integer queryForObject = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { userId });
		return queryForObject;
	}
	/**
	 * @param page
	 *            当前页数
	 * @param rows
	 *            显示个数
	 * @param userId
	 *            用户id
	 * @作用 获取 课程 本页的信息(课程)
	 */
	public List<Map<String, Object>> listCourseAskAnswer(int page, int rows, String userId) {
		int toprow = (page - 1) * rows;
		String sql = "SELECT TOP "+rows+" dbo.course.coursename,dbo.[user].name, dbo.course_ask_answer.id, dbo.course_ask_answer.user_id, dbo.course_ask_answer.course_id, dbo.course_ask_answer.org_id,  dbo.course_ask_answer.question, dbo.course_ask_answer.answer, dbo.course_ask_answer.stick, dbo.course_ask_answer.gmt_create,  dbo.course_ask_answer.gmt_modified FROM dbo.[user] INNER JOIN     dbo.course_ask_answer ON dbo.[user].id = dbo.course_ask_answer.user_id INNER JOIN   dbo.course ON dbo.course_ask_answer.course_id = dbo.course.courseid		where (dbo.course_ask_answer.id not in (select top "+
		toprow+" dbo.course_ask_answer.id from dbo.[user] INNER JOIN     dbo.course_ask_answer ON dbo.[user].id = dbo.course_ask_answer.user_id INNER JOIN   dbo.course ON dbo.course_ask_answer.course_id = dbo.course.courseid	 where dbo.course_ask_answer.user_id =?  order by  dbo.course_ask_answer.gmt_create DESC)) and dbo.course_ask_answer.user_id=?  order by  dbo.course_ask_answer.gmt_create DESC";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, new Object[] { userId, userId });
		return queryForList;
	}
	/**
	 * @作用 获取机构问题的总数
	 * @param userId 用户id
	 */
	public int countOrgAskAnswer(String userId) {
		String sql = "select count(*) from org_ask_answer where user_id=? ";
		Integer queryForObject = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { userId });
		return queryForObject;
	}
	/**
	 * @param page
	 *            当前页数
	 * @param rows
	 *            显示个数
	 * @param userId
	 *            用户id
	 * @作用 获取 机构 本页的信息(机构)
	 */
	public List<Map<String, Object>> listOrgAskAnswer(int page, int rows, String userId) {
		int toprow = (page - 1) * rows;
		String sql = "SELECT top "+rows+" dbo.org_ask_answer.id, dbo.org_ask_answer.org_id, dbo.org_ask_answer.question, dbo.org_ask_answer.answer, dbo.org_ask_answer.gmt_create,   dbo.org_ask_answer.gmt_modified, dbo.org.orgid, dbo.org.orgname\r\n" + 
				"FROM  dbo.org INNER JOIN      dbo.org_ask_answer ON dbo.org.orgid = dbo.org_ask_answer.org_id where ( dbo.org_ask_answer.id  not in(select top "+toprow+"  dbo.org_ask_answer.id from dbo.org_ask_answer INNER JOIN      dbo.[user] ON dbo.org_ask_answer.user_id = dbo.[user].id where  dbo.org_ask_answer.user_id=?  order by dbo.org_ask_answer.gmt_create DESC)) and dbo.org_ask_answer.user_id=?  order by dbo.org_ask_answer.gmt_create DESC";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, new Object[] { userId, userId });
		return queryForList;
	}
}
