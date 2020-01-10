package cn.asxuexi.personalInfo.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.asxuexi.logup.service.RegisterService;
import cn.asxuexi.personalInfo.dao.PersonalInfoDao;
import cn.asxuexi.personalInfo.entity.UserInfo;
import cn.asxuexi.tool.MD5Tool;
import cn.asxuexi.tool.MailUtil;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.RedisTool;
import cn.asxuexi.tool.DateTimeTool;
import cn.asxuexi.tool.Token_JWT;
import cn.asxuexi.tool.Upbaidutool;

@Service
public class PersonalInfoServiceImpl implements PersonalInfoService {

	@Resource
	private PersonalInfoDao dao;
	@Resource 
	private RegisterService registerService;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private RedisTool redis;
	private String asxuexi_resource = "/../asxuexi_resource/";
	private String asxuexiResource = "/asxuexi_resource/";

	
	/**
	 * @作用 获取用户信息
	 */
	public Map<String, Object> getPersonalInfo() {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		Map<String, Object> personalInfo = dao.getPersonalInfo(userId);
		// 转化时间
		DateTimeTool timeAndDate = new DateTimeTool();
		if (null != personalInfo) {
			if (personalInfo.get("birthday") != null) {
				if ((Long) personalInfo.get("birthday") != 0) {
					String transForDate = timeAndDate.transForDate((Long) personalInfo.get("birthday"));
					personalInfo.put("birthday", transForDate);// 前端判断是否为0
					String year = transForDate.substring(0, transForDate.indexOf("-"));
					String month = transForDate.substring(transForDate.indexOf("-") + 1,
							transForDate.indexOf("-", transForDate.indexOf("-") + 1));
					String day = transForDate.substring(transForDate.lastIndexOf("-") + 1);
					
					personalInfo.put("year", year);
					personalInfo.put("month", month.replaceAll("^(0+)", ""));
					personalInfo.put("day", day.replaceAll("^(0+)", ""));
				}else {
					personalInfo.put("birthday", null);
				}

			}
			// 地区转化
			String areasId = (String) personalInfo.get("areas_id");
			if (areasId == null || "".equals(areasId)) {
				personalInfo.put("areas_id", 0);
			} else {
				List<Map<String, Object>> listAddress = dao.listAddress(areasId);
				List<Map<String, Object>> listAddresss = dao.listAddresss(areasId);
				personalInfo.put("listAddress", listAddress);
				personalInfo.put("allListAddress", listAddresss);
			}
			//判断用户是否上传头像
			String photo = (String) personalInfo.get("photo");
			if (null==photo){
				//设置默认头像
				photo="img/personal/default.jgp";
			}
			String scheme =request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+asxuexiResource + "user/" + userId + "/" + photo;
			personalInfo.put("photo", scheme);
		}
		return personalInfo;
	}
	
	/**
	 * @author fanjunguo
	 * @description 只更新头像信息
	 * 	修改上传头像的逻辑: 用户头像使用固定的命名规则:"photo_"+userId.   这样用户上传头像时,不再每次都修改数据库
	 * 		
	 */
	@Override
	public int updatePhoto(MultipartFile file) {
		int result=0;
		String userId = (String)Token_JWT.verifyToken().get("userId");
		String rootPath = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + "user/"+ userId;
		File newFile = new File(rootPath);
		//判断是否存在文件夹.如果不存在,创建
		if (!newFile.exists()) {
			newFile.mkdirs();
		}
		//统一规定图片格式png
		String fileType = ".png";
		String photoName=newFile.toString()+File.separator+"photo_"+userId+fileType;
		File storeFile=new File(photoName);
		try {
			file.transferTo(storeFile);
			//照片存储成功之后.判断如果用户没有设置过头像,需要将头像地址存到数据库
			String photo = dao.getPhoto(userId);
			if (null==photo) {
				dao.updatePhoto(userId, "photo_"+userId+fileType);
			}
			result=1;
		} catch (IllegalStateException | IOException e) {
			result=0;
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	
	/**
	 * @version 1.0 zhangshun
	 * 修改用户图片和基本信息
	 * 
	 * @version 2.0 
	 * @author fanjunguo
	 * 		修改上传头像的逻辑: 用户头像使用固定的命名规则:"photo_"+userId.   这样用户上传头像时,不再每次都修改数据库; 
	 * 		这样就相当于,上传头像和更改其他信息分开了.如果一方失败,不影响另一方修改
	 * 	
	 */
	public int updateUserInfoFile(@RequestParam("file") MultipartFile file, UserInfo userInfo) {
		DateTimeTool timeAndDate = new DateTimeTool();
		String userId = (String)Token_JWT.verifyToken().get("userId");
		LocalDateTime now = LocalDateTime.now();
		String rootPath = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + "user/"
				+ userId;
		File newFile = new File(rootPath);
		if (!newFile.exists()) {
			newFile.mkdirs();
		}
		
		//获取文件类型的后缀(包含“.”)
		String fileType = Upbaidutool.getFileType(file);
		String photoName=newFile.toString()+File.separator+"photo_"+userId+fileType;
		File storeFile=new File(photoName);
		boolean symbol;
		try {
			file.transferTo(storeFile);
			//照片存储成功之后.判读如果用户没有设置过头像,需要将头像地址存到数据库
			String photo = dao.getPhoto(userId);
			if (null==photo) {
				dao.updatePhoto(userId, "photo_"+userId+fileType);
			}
			symbol=true;
		} catch (IllegalStateException | IOException e) {
			symbol=false;
			e.printStackTrace();
		}
		
		// 生日转化时间戳
		if(!"0".equals(userInfo.getDay())) {
			String month = userInfo.getMonth();
			String day = userInfo.getDay();
			String date = userInfo.getYear() + "-" + month + "-" + day;
			String dateForTrans = timeAndDate.dateForTrans(date);
			userInfo.setBirth(dateForTrans);
		}
		
		int updateUserInfo = dao.updateUserInfoFile(userInfo, userId, now);
		int returnValue=1;
		if (!symbol||updateUserInfo==0) {
			returnValue=0;
		}
		return returnValue;
	}

	/**
	 * 修改用户图片和基本信息
	 * 
	 * @param userInfo 用户信息表
	 */
	public int updateUserInfo(UserInfo userInfo) {
		DateTimeTool timeAndDate = new DateTimeTool();
		String userId = (String)Token_JWT.verifyToken().get("userId");
		LocalDateTime now = LocalDateTime.now();
		// 时间转化时间戳
		if(!"0".equals(userInfo.getDay())) {
			String month = userInfo.getMonth();
			String day = userInfo.getDay();
			String date = userInfo.getYear() + "-" + month + "-" + day;
			String dateForTrans = timeAndDate.dateForTrans(date);
			
			userInfo.setBirth(dateForTrans);
		}else {
			userInfo.setBirth("0");
		}
		
		int updateUserInfo = dao.updateUserInfo(userInfo, userId, now);
		return updateUserInfo;
	}
	
	@Override
	public int updateName(String userName) {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		int result=0;
		if (null!=userName&&!"".equals(userName)) {
			try {
				result = dao.updateName(userName, userId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @作用 修改密码
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return int 1:修改成功 2.原密码不正确  3.密码更新失败
	 */
	public int updatePassword(String oldPassword, String newPassword) {
		int returnValue = 5;
		String userId = (String)Token_JWT.verifyToken().get("userId");
		// 1、pass 进行mD5加密
		String createMD5Data = MD5Tool.createMD5Data(oldPassword);
		String createMD5Data2 = MD5Tool.createMD5Data(newPassword);

		// 2、查询老密码
		Map<String, Object> password = dao.getPassword(userId);
		String object = (String) password.get("password");
		// 3、进行比对
		if (createMD5Data.equals(object)) {
			int updatePassword = dao.updatePassword(userId, createMD5Data2);
			if (updatePassword == 0) {
				returnValue = 3;
			} else {
				returnValue = 1;
				redis.delToken();
			}
		} else {
			returnValue = 2;
		}
		return returnValue;
	}

	/**
	 * @author fanjunguo
	 * @description 验证用户输入的密码是否正确
	 * @param password 用户输入的密码
	 * @return map json格式的结果
	 */
	@Override
	public String validatePassword(String password){
		String userId = (String)Token_JWT.verifyToken().get("userId");
		String passwordDB = (String) dao.getPassword(userId).get("password");
		String passwordParam = MD5Tool.createMD5Data(password);
		String result;
		if(passwordDB.equals(passwordParam)) {
			result="true";
		}else {
			result="false";
		}
		return result;
	}
	
	
	
	/**
	 * @作用 修改手机号
	 * @param password 密码
	 * @param newPhone  新手机号
	 * @param telCode 用户输入的验证码
	 * @return int  1:更改成功; 2:密码错误 3.验证码错误或失效 4.出现异常
	 */
	public int updatePhone(String password, String newPhone, String telCode) {
		int returnValue;
		String userId = (String)Token_JWT.verifyToken().get("userId");
		//查询老密码
		Map<String, Object> passwordmap = dao.getPassword(userId);
		String passworda = (String) passwordmap.get("password");
		String createMD5Data = MD5Tool.createMD5Data(password);
		//验证密码是否正确
		if (createMD5Data.equals(passworda)) {
			String isMatched = registerService.codeIsMatched(telCode, newPhone);
			if ("true".equals(isMatched)) {
				// 验证码正确,修改手机号
				try {
					returnValue = dao.updateTel(userId, newPhone);
					returnValue=1;
				} catch (Exception e) {
					e.printStackTrace();
					returnValue=4;
				}
			} else {
				//验证码错误或已失效
				returnValue = 3;
			}
		} else {
			// 原密码不一致
			returnValue = 2;
		}
		return returnValue;
	}

	/**
	 * @作用 验证邮箱
	 * @param newEmail  新邮箱
	 * @param password  密码
	 * @return int 结果:0.保存数据库错误; 1发送成功; 2.邮件发送出现问题; 3.密码错误
	 */
	public int emailValidate(String newEmail, String password) {
		int returnValue=0;
		String userId = (String)Token_JWT.verifyToken().get("userId");
		//查询老密码
		Map<String, Object> passwordmap = dao.getPassword(userId);
		String passworda = (String) passwordmap.get("password");
		String createMD5Data = MD5Tool.createMD5Data(password);
		Long nowtime = System.currentTimeMillis() / 1000;
		if (createMD5Data.equals(passworda)) {
			// 查询邮箱信息
			Map<String, Object> email = dao.getEmail(userId);
			String randomCode = "";
			int updateEmail = 0;
			if (email != null) {
				Long create = (Long) email.get("gmt_create");
				if (nowtime - create < 1800) {
					randomCode = (String) email.get("code");
					updateEmail = dao.updateEmail(newEmail, nowtime, randomCode, userId);
				} else {
					randomCode = RandomTool.getRandomCode(6);
					updateEmail = dao.updateEmail(newEmail, nowtime, randomCode, userId);
				}
			} else {
				randomCode = RandomTool.getRandomCode(6);
				updateEmail = dao.insertEmail(newEmail, randomCode, nowtime, userId);
			}
			// 插入数据库
			if (updateEmail == 1) {
				boolean sendMail = new MailUtil().sendMail(newEmail, randomCode);
				if (sendMail) {
					returnValue = 1;
				} else {
					returnValue = 2;
				}
			} else {
				returnValue = 0;
			}
			// 发送验证
		} else {
			returnValue = 3;// 密码错误
		}
		return returnValue;
	}

	/**
	 * @作用 保存邮箱信息
	 * @param code 验证码
	 * @param newEmail 新邮箱
	 */
	public int updateEmail(String code, String newEmail) {
		int returnValue=0;
		String userId = (String)Token_JWT.verifyToken().get("userId");
		try {
			Map<String, Object> email = dao.getEmail(newEmail, userId);
			if (email != null) {
				Long nowtime = System.currentTimeMillis() / 1000;
				Long time = (Long) email.get("gmt_create");
				if (nowtime - time < 1800) {
					if (code.equals(email.get("code"))) {
						returnValue = dao.updateEmail(userId, newEmail);
					} else {
						returnValue = 2;// 验证码不正确
					}
				} else {
					returnValue = 3;// 超时
				}
			} else {
				returnValue = 4;//邮箱错误
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * @作用 查询绑定状态
	 * 
	 */
	public Map<String, Object> queryState() {
		Map<String, Object> map = new HashMap<String, Object>(16);
		String userId = (String)Token_JWT.verifyToken().get("userId");
		List<Map<String, Object>> state = dao.getState(userId);
		if (state.size() == 2) {
			for (Map<String, Object> map2 : state) {
				String state1 = (String) map2.get("id");
				String substring = state1.substring(0, 1);
				if (substring.equals("w")) {
					map.put("wx", state1);
				} else {
					map.put("zhifubao", state1);
				}
			}
		} else if (state.size() == 1) {
			String state1 = (String) state.get(0).get("id");
			String substring = state1.substring(0, 1);
			if (substring.equals("w")) {
				map.put("wx", state1);
				map.put("zhifubao", null);
			} else {
				map.put("zhifubao", state1);
				map.put("wx", null);
			}
		} else {
			map.put("wx", null);
			map.put("zhifubao", null);
		}

		return map;
	}

	/**
	 * @作用 解绑第三方信息
	 * @param state
	 *            支付宝或微信
	 */
	public int delThridId(String state) {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		int delThridId = 0;
		delThridId = dao.delThridId(userId, state);
		return delThridId;
	}
}
