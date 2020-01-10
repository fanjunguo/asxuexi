package cn.asxuexi.personalInfo.service;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.asxuexi.personalInfo.entity.UserInfo;

public interface PersonalInfoService {

	/**
	 * @作用 获取用户信息
	 */
	Map<String, Object> getPersonalInfo();

	/**
	 * 修改用户图片和基本信息
	 */
	public int updateUserInfoFile(@RequestParam("file") MultipartFile file, UserInfo userInfo);

	/**
	 * 修改用户图片和基本信息
	 * 
	 * @param userInfo
	 *            用户信息表
	 */
	int updateUserInfo(UserInfo userInfo);

	/**
	 * @作用 修改密码
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return int 1:修改成功 2.原密码不正确  3.密码更新失败
	 */
	int updatePassword(String oldPassword, String newPassword);

	/**
	 * @作用 修改手机号
	 * @param password 密码
	 * @param newPhone  新手机号
	 * @param telCode 用户输入的验证码
	 * @return int 1:更改成功; 2:密码错误 3.验证码错误或失效 4.出现异常
	 */
	int updatePhone(String password, String newPhone, String telCode);

	/**
	 * @作用 验证邮箱
	 * @param newEmail
	 *            新邮箱
	 * @param password
	 *            密码
	 * @return
	 */
	int emailValidate(String newEmail, String password);

	/**
	 * @作用 保存邮箱信息
	 * @param code
	 *            验证码
	 * @param newEmail
	 *            新邮箱
	 **/
	int updateEmail(String code, String newEmail);
	 /**
		 * @作用 查询绑定状态
		 * 
		 * */
	 Map<String, Object> queryState();
	 /**
		 * @作用 解绑第三方信息
		 *  @param state 支付宝或微信
		 */
		public int delThridId(String state);

	
	int updateName(String userName);

	/**
	 * @author fanjunguo
	 * @description 
	 * @param file
	 * @return 
	 */
	int updatePhoto(MultipartFile file);

	/**
	 * @author fanjunguo
	 * @description 验证用户输入的密码是否正确
	 * @param password 用户输入的密码
	 * @return map json格式的结果
	 */
	String validatePassword(String password);
}
