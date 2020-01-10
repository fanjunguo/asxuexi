package cn.asxuexi.personalInfo.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import cn.asxuexi.personalInfo.entity.UserInfo;

public interface PersonalInfoDao {
	/**
	 * @作用 获取用户信息
	 * @param userId
	 *            用户id
	 */
	Map<String, Object> getPersonalInfo(String userId);

	/**
	 * @作用 请求县de 父级
	 * @param areasId
	 *            县级id
	 **/
	List<Map<String, Object>> listAddress(String areasId);

	/**
	 * @作用 请求全部的省市县
	 * @param areasId
	 *            县级id
	 **/
	List<Map<String, Object>> listAddresss(String areasId);

	/**
	 * @作用 修改信息存到数据库
	 * @param userInfo
	 *            user表信息
	 * @param userId
	 *            用户id
	 * @param now
	 *            现在的时间
	 */
	int updateUserInfoFile(UserInfo userInfo, String userId,LocalDateTime now);

	/**
	 * @作用 修改信息存到数据库（图片）
	 * @param userInfo
	 *            user表信息
	 * @param userId
	 *            用户id
	 * @param now
	 *            现在的时间
	 */
	int updateUserInfo(UserInfo userInfo, String userId, LocalDateTime now);

	/***
	 * @作用 查询密码
	 * @param userId
	 *            用户id
	 */
	Map<String, Object> getPassword(String userId);

	/***
	 * @作用 修改密码
	 * @param userId
	 *            用户id
	 * @param newPassword
	 *            新密码
	 */
	int updatePassword(String userId, String newPassword);

	/**
	 * @作用 查询验证码
	 * @param newTel
	 *            手机号
	 * @return {@link Map} [tel] 电话 ,[telcode] 验证码,[counts] 次数,[time] 发送时间
	 */
	Map<String, Object> getTelCode(String newTel);

	/**
	 * @作用 修改手机号
	 * @param userId
	 *            用户id
	 * @param tel
	 *            手机号
	 */
	int updateTel(String userId, String tel);

	/**
	 * @作用 插入email
	 * @param email
	 *            邮箱
	 * @param code
	 *            验证码
	 * @param now
	 *            现在时间
	 */
	int insertEmail(String email, String code, Long now, String userId);

	/**
	 * @作用 修改email
	 * @param email
	 *            邮箱
	 * @param code
	 *            验证码
	 * @param now
	 *            现在时间
	 */
	int updateEmail(String email, Long now, String randomCode, String userId);

	/**
	 * @作用 查询email
	 * @param email
	 *            邮箱
	 * @param userId
	 *            用户id
	 */
	Map<String, Object> getEmail(String email, String userId);

	/**
	 * @作用 查询email
	 * @param email
	 *            邮箱
	 * @param userId
	 *            用户id
	 */
	Map<String, Object> getEmail(String userId);

	/**
	 * @作用 修改email
	 * @param userId
	 *            用户id
	 * @param email
	 *            用户email
	 */
	int updateEmail(String userId, String email);

	/**
	 * @作用 查询第三方绑定信息
	 * @param userId
	 *            用户id
	 */
	List<Map<String, Object>> getState(String userId);

	/**
	 * @作用 解绑第三方信息
	 * @param userId
	 *            用户id
	 * @param state
	 *            支付宝或微信
	 */
	int delThridId(String userId, String state);
	
	int updateName(String userName, String userId);

	int updatePhoto(String userId, String photo);

	/**
	 * @author fanjunguo
	 * @description  查询照片数据,判断是否已经存入头像
	 * @param userId
	 * @return String 如果已经设置过头像,返回图片名称; 如果没有,返回null
	 */
	String getPhoto(String userId);
}
