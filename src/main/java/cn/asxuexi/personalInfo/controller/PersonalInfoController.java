package cn.asxuexi.personalInfo.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.asxuexi.personalInfo.entity.UserInfo;
import cn.asxuexi.personalInfo.service.PersonalInfoService;

@Controller
public class PersonalInfoController {

	@Resource
	private PersonalInfoService personalInfoService;

	/**
	 * @作用 获取用户信息
	 */
	@ResponseBody
	@RequestMapping("PersonalInfo/getPersonalInfo.action")
	public Map<String, Object> getPersonalInfo(HttpServletRequest req) {
		Map<String, Object> personalInfo = personalInfoService.getPersonalInfo();
		return personalInfo;
	}

	/**
	 * @作用 修改用户信息(包含头像)
	 * @param file 图片文件
	 * @param userName 用户名
	 * @param sex 性别
	 * @param year 年份
	 * @param month 月份
	 * @param day 天 出生年月日
	 * @param address 县级地区
	 */
	@ResponseBody
	@RequestMapping("PersonalInfo/updateUserInfoFile.action")
	public int updateUserInfoFile(@RequestParam("file") MultipartFile file, String userName, String sex, String year,
			String month, String day, String address) {
		UserInfo userInfo = new UserInfo(userName, sex, year, month, day, address);
		int updateUserInfoFile = personalInfoService.updateUserInfoFile(file, userInfo);
		return updateUserInfoFile;
	}
	
	/**
	 * 
	 * @author fanjunguo
	 * @description 单独更新头像
	 * @param file
	 * @return int 1表示更新成功 0表示更新失败
	 */
	@RequestMapping("personalInfo/updatePhoto.action")
	@ResponseBody
	public int updatePhoto(@RequestParam("file") MultipartFile file) {
		int updatePhoto = personalInfoService.updatePhoto(file);
		return updatePhoto;
	}
	
	
	/**
	 * @作用 修改用户信息(不包含头像)
	 * @param userName  用户名
	 * @param sex 性别
	 * @param year 年份
	 * @param month 月份
	 * @param day 天 出生年月日
	 * @param address 县级地区
	 */
	@ResponseBody
	@RequestMapping("PersonalInfo/updateUserInfo.action")
	public int updateUserInfo(String userName, String sex, String year, String month, String day, String address) {
		UserInfo userInfo = new UserInfo(userName, sex, year, month, day, address);
		int updateUserInfo = personalInfoService.updateUserInfo(userInfo);
		return updateUserInfo;
	}

	/**
	 * @author fanjunguo
	 * @description 单独修改昵称
	 * @return int 修改成功返回1,不成功返回0
	 */
	@RequestMapping("PersonalInfo/updateName.action")
	@ResponseBody
	public int updateName(String userName) {
		int result = personalInfoService.updateName(userName);
		return result;
	}
	
	
	
	/**
	 * @作用 修改密码
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 */
	@ResponseBody
	@RequestMapping("PersonalInfo/updatePassword.action")
	public int updatePassword(String oldPassword, String newPassword) {
		int updatePassword = personalInfoService.updatePassword(oldPassword, newPassword);
		return updatePassword;
	}

	//验证当前密码是否正确
	@RequestMapping("personalInfo/validatePassword.action")
	@ResponseBody
	public String validatePassword(String password){
		String result = personalInfoService.validatePassword(password);		
		return result;
	}
	
	
	/**
	 * @作用 修改手机号
	 * @param password
	 *            密码
	 * @param newPhone
	 *            新密码
	 * @param telCode
	 *            验证码
	 */
	@ResponseBody
	@RequestMapping("PersonalInfo/updatePhone.action")
	public int updatePhone(String password, String newPhone, String telCode) {
		int updatePhone = personalInfoService.updatePhone(password, newPhone, telCode);
		return updatePhone;
	}

	/**
	 * @作用 验证邮箱
	 * @param newEmail 新邮箱
	 * @param password 密码
	 */
	@ResponseBody
	@RequestMapping("personalInfo/emailValidate.action")
	public int emailValidate(String newEmail, String password) {
		int emailValidate = personalInfoService.emailValidate(newEmail, password);
		return emailValidate;
	}

	/**
	 * @作用 保存邮箱信息
	 * @param code 验证码
	 * @param newEmail 新邮箱
	 **/
	@ResponseBody
	@RequestMapping("personalInfo/updateEmail.action")
	public int updateEmail(String code, String newEmail) {
		int updateEmail = personalInfoService.updateEmail(code, newEmail);
		return updateEmail;
	}

	/**
	 * @作用 查询第三方状态
	 */
	@ResponseBody
	@RequestMapping("PersonalInfo/queryState.action")
	public Map<String, Object> queryState() {
		Map<String, Object> queryState = personalInfoService.queryState();
		return queryState;
	}

	/**
	 * @作用 解绑第三方信息
	 * @param state
	 *            支付宝或微信
	 */
	@ResponseBody
	@RequestMapping("PersonalInfo/delThridId.action")
	public int delThridId(String state) {
		int delThridId = personalInfoService.delThridId(state);
		return delThridId;
	}

}
