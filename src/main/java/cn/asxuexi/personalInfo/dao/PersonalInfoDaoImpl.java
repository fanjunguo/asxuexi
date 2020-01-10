package cn.asxuexi.personalInfo.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.asxuexi.personalInfo.entity.UserInfo;

@Repository
public class PersonalInfoDaoImpl implements PersonalInfoDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * @作用 获取用户信息
	 * @param userId
	 *            用户id
	 */
	public Map<String, Object> getPersonalInfo(String userId) {
		Map<String, Object> queryForMap = null;
		String sql = "select [id],[name],[birthday] ,[photo] ,[areas_id],[sex] ,email,tel from [user] where  id=?";
		try {
			queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { userId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryForMap;
	}

	/**
	 * @作用 请求县市的父级
	 * @param areasId
	 *            县级id
	 **/
	public List<Map<String, Object>> listAddress(String areasId) {
		String sql = "select id,name ,LevelType from areas where ID=? or Id in (select ParentId from areas where ID=?) or  Id in (select ParentId from areas where ID in (select ParentId from areas where ID=?))";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql,
				new Object[] { areasId, areasId, areasId });
		return queryForList;
	}

	/**
	 * @作用 请求全部的省市县
	 * @param areasId
	 *            县级id
	 **/
	public List<Map<String, Object>> listAddresss(String areasId) {
		String sql = "select id,name ,LevelType from areas where ParentId in (select ParentId from areas where ID=?) or  ParentId in (select ParentId from areas where Id in (select ParentId from areas where ID=?)) or ParentId in (select ParentId from areas where Id in (select ParentId from areas where ID in (select ParentId from areas where ID=?)))";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql,
				new Object[] { areasId, areasId, areasId });
		return queryForList;
	}
	
	@Override
	public String getPhoto(String userId) {
		String sql="select photo from [user] where id=?";
		String photo = jdbcTemplate.queryForObject(sql, String.class, userId);
		return photo;
	}
	
	
	@Override
	public int updatePhoto(String userId,String photo) {
		String sql="update [user] set photo=? where id=?";
		int updateResult = jdbcTemplate.update(sql, new Object[] {photo,userId});
		return updateResult;
		
	}
	
	
	/**
	 * @作用 修改信息存到数据库（图片）
	 * @param userInfo
	 *            user表信息
	 * @param userId
	 *            用户id
	 * @param photo
	 *            用户图片名称
	 * @param now
	 *            现在的时间
	 */
	public int updateUserInfoFile(UserInfo userInfo, String userId, LocalDateTime now) {
		String sql = "update [user] set name=?,birthday=?,areas_id=?,sex=?,gmt_modified=? where id=?";
		int update = jdbcTemplate.update(sql, new Object[] { userInfo.getName(), userInfo.getBirth(),
				userInfo.getAddress(), userInfo.getSex(), now, userId });
		return update;
	}

	/**
	 * @作用 修改信息存到数据库（图片）
	 * @param userInfo
	 *            user表信息
	 * @param userId
	 *            用户id
	 * @param now
	 *            现在的时间
	 */
	public int updateUserInfo(UserInfo userInfo, String userId, LocalDateTime now) {
		String sql = "update [user] set name=?,birthday=?,areas_id=?,sex=?,gmt_modified=? where id=?";
		int update = jdbcTemplate.update(sql, new Object[] { userInfo.getName(), userInfo.getBirth(),
				userInfo.getAddress(), userInfo.getSex(), now, userId });
		return update;
	}

	
	@Override
	public int updateName(String userName, String userId) {
		String sql="update [user] set name=? where id=?";
		int result = jdbcTemplate.update(sql, new Object[] {userName,userId});
		return result;
	}
	
	/***
	 * @作用 查询密码
	 * @param userId 用户id
	 */
	public Map<String, Object> getPassword(String userId) {
		String sql = "SELECT password  FROM [user] where id=?";
		Map<String, Object> queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { userId });
		return queryForMap;
	}

	/***
	 * @作用 修改密码
	 * @param userId 用户id
	 */
	public int updatePassword(String userId, String newPassword) {
		String sql = "update [user] set password=? where id=?";
		int update = jdbcTemplate.update(sql, new Object[] { newPassword, userId });
		return update;
	}

	/**
	 * @作用 查询验证码
	 * @param newTel
	 * @return {@link Map} [tel] 电话 ,[telcode] 验证码,[counts] 次数,[time] 发送时间
	 */
	public Map<String, Object> getTelCode(String newTel) {
		Map<String, Object> queryForMap = null;
		String sql = "SELECT *  FROM dbo.[smsverificationcode]  where tel=? order by gmt_modified DESC";
		try {
			queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { newTel });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryForMap;
	}

	/**
	 * @作用 修改手机号
	 * @param userId  用户id
	 * @param tel 手机号
	 */
	public int updateTel(String userId, String tel) {
		String sql = "update [user] set tel=? where id=?";
		int update = jdbcTemplate.update(sql, new Object[] { tel, userId });
		return update;
	}

	/**
	 * @作用 插入email
	 * @param email
	 *            邮箱
	 * @param code
	 *            验证码
	 * @param now
	 *            现在时间
	 * @param userId
	 *            用户id
	 */
	public int insertEmail(String email, String code, Long now, String userId) {
		String sql = "insert into email(user_id,email,code,gmt_create) values(?,?,?,?)";
		int update = jdbcTemplate.update(sql, new Object[] { userId, email, code, now });
		return update;
	}

	/**
	 * @作用 修改email
	 * @param email
	 *            邮箱
	 * @param code
	 *            验证码
	 * @param now
	 *            现在时间
	 * @param userId
	 *            用户id
	 */
	public int updateEmail(String email, Long now, String randomCode, String userId) {
		String sql = "update email set gmt_create=? ,email=? ,code=? where  user_id=?";
		int update = jdbcTemplate.update(sql, new Object[] { now, email, randomCode, userId });
		return update;
	}

	/**
	 * @作用 查询email
	 * @param email
	 *            邮箱
	 * @param userId
	 *            用户id
	 */
	public Map<String, Object> getEmail(String email, String userId) {
		Map<String, Object> queryForMap = null;
		String sql = "select gmt_create,code from email where  user_id=? and email=?";
		queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { userId, email });
		return queryForMap;
	}

	/**
	 * @作用 查询email
	 * @param email
	 *            邮箱
	 * @param userId
	 *            用户id
	 */
	public Map<String, Object> getEmail(String userId) {
		Map<String, Object> queryForMap = null;
		String sql = "select gmt_create,code from email where  user_id=? ";
		try {
			queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { userId });
		} catch (Exception e) {
			// TODO: handle exception
		}
		return queryForMap;
	}

	/**
	 * @作用 修改email
	 * @param userId
	 *            用户id
	 * @param email
	 *            用户email
	 */
	public int updateEmail(String userId, String email) {
		String sql = "update [user] set email=? where id=?";
		int update = jdbcTemplate.update(sql, new Object[] { email, userId });
		return update;
	}

	/**
	 * @作用 查询第三方绑定信息
	 * @param userId
	 *            用户id
	 */
	public List<Map<String, Object>> getState(String userId) {
		String sql = "SELECT user_id, id,gmt_create FROM  dbo.third where user_id=?";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, new Object[] { userId });
		return queryForList;
	}

	/**
	 * @作用 解绑第三方信息
	 * @param userId
	 *            用户id
	 * @param state
	 *            支付宝或微信
	 */
	public int delThridId(String userId, String state) {
		String sql = "delete from third where [user_id] =? and id like ?";
		int queryForList = jdbcTemplate.update(sql, new Object[] { userId, state + "%" });
		return queryForList;
	}
}
