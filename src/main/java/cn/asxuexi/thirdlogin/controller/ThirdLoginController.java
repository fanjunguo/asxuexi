package cn.asxuexi.thirdlogin.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.asxuexi.login.dao.LogInDao;
import cn.asxuexi.logup.service.RegisterService;
import cn.asxuexi.thirdlogin.service.ThirdLoginService;
import cn.asxuexi.tool.OpenAlipay;
import cn.asxuexi.tool.OpenWechat;
import cn.asxuexi.tool.RedisTool;
import cn.asxuexi.tool.Token_JWT;

@Controller
@RequestMapping("thirdlogin/")
public class ThirdLoginController {

	@Resource
	private ThirdLoginService thirdLoginService;
	@Resource
	private RegisterService registerService;
	@Resource
	private LogInDao logInDaoImpl;
	@Autowired
	private RedisTool redis;
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());

	@RequestMapping("wechat.do")
	public String wechat(String state, String code, Model model,HttpServletResponse response) {
		//获取微信用户id
		String wechatUserId;
		try {
			//如果用户在当前页面再次刷新,微信接口不再响应,会报异常.因此把wechatUserId存入缓存
			JSONObject userInfo = new OpenWechat().getUserInfo(code);
			wechatUserId = userInfo.getString("openid"); 
			redis.addString(code, wechatUserId, 30L, TimeUnit.MINUTES);
		} catch (Exception e) {
			wechatUserId=redis.getString(code);
		}
		if ("login".equals(state)) {
			// 第三方登录
			if (null == wechatUserId) {
				// 跳转回登录页面 携带错误信息
				model.addAttribute("message", "nullwechatid");
				return "login/log_in";
			} else {
				Map<String, Object> result = thirdLoginService.isBound("wx_" + wechatUserId);
				if ((boolean) result.get("isBound")) {
					//如果账号已绑定，登录
					String userId=(String) result.get("asxuexiUserId");
					
					//签发token
					String token = Token_JWT.createToken(userId);
					Cookie cookie= new Cookie("access-token", token);
					cookie.setPath("/");
					response.addCookie(cookie);
					return "homepage";
				} else {
					//将第三方id存到页面中
					model.addAttribute("thirdId", "wx_" + wechatUserId);
					model.addAttribute("thirdIdType", "wx");
					return "login/thirdlogin";
				}
			}
		}
		if (state.contains("bind")) {
			// 第三方绑定
			String userId = (String)Token_JWT.verifyToken().get("userId");
			if (userId == null) {
				// 如果用户id为空，代表登录失效,返回标识2,前端弹窗登录
				model.addAttribute("message", "2");
			} else {
				if (null == wechatUserId) {
					// 用户id获取不到，表示绑定失败
					model.addAttribute("message", "0");
				} else {
					Map<String, Object> result = thirdLoginService.isBound("wx_" + wechatUserId);
					if ((boolean) result.get("isBound")) {
						// 该微信号已被绑定，返回3，前端提示已绑定，需解绑
						model.addAttribute("message", "3");
					} else {
						// 将userid,weixinid新建记录存入表中
						String insertThirdId = thirdLoginService.insertThirdId(userId, "wx_" + wechatUserId);
						// 成功返回1 失败返回0
						if ("1".equals(insertThirdId)) {
							model.addAttribute("message", "1");
						} else {
							model.addAttribute("message", "0");
						}
					}
				}
			}
			return "personal/personalInfo";
		}
		return null;
	}
	//支付宝
	@RequestMapping("alipay.do")
	public String alipay(String state, String auth_code, Model model,HttpServletResponse response) {
		// 获取支付宝用户id
		String alipayUserId;
		try {
			//如果用户在当前页面再次刷新,微信接口不再响应,会报异常.因此把wechatUserId存入缓存
			alipayUserId = new OpenAlipay().getUserInfoByAuthCode(auth_code).getUseId();
			redis.addString(auth_code, alipayUserId, 30L, TimeUnit.MINUTES);
		} catch (Exception e) {
			alipayUserId=redis.getString(auth_code);
		}
		
		if ("login".equals(state)) {
			if (null == alipayUserId) {
				// 跳转回登录页面 携带错误信息
				model.addAttribute("message", "nullalipayid");
				return "login/log_in";
			} else {
				Map<String, Object> result = thirdLoginService.isBound("zfb_" + alipayUserId);
				if ((boolean) result.get("isBound")) {
					//账号已绑定，登录
					String userId=(String) result.get("asxuexiUserId");
					//签发token
					String token = Token_JWT.createToken(userId);
					Cookie cookie= new Cookie("access-token", token);
					cookie.setPath("/");
					response.addCookie(cookie);
					return "homepage";
					
				} else {
					model.addAttribute("thirdId", "wx_" + alipayUserId);
					model.addAttribute("thirdIdType", "zfb");
					return "login/thirdlogin";
				}
			}
		}
		if (state.contains("bind")) {
			// 第三方绑定
			String userId = (String)Token_JWT.verifyToken().get("userId");
			if (userId == null) {
				// 如果sessionid对应的session为空，代表登录超时(session已销毁),返回标识2,前端弹窗登录
				model.addAttribute("message", "2");
			} else {
				if (null == alipayUserId) {
					// 用户id获取不到，表示绑定失败
					model.addAttribute("message", "0");
				} else {
					Map<String, Object> result = thirdLoginService.isBound("zfb_" + alipayUserId);
					if ((boolean) result.get("isBound")) {
						// 该微信号已被绑定，返回3，前端提示已绑定，需解绑
						model.addAttribute("message", "3");
					} else {
						// 获取userid,alipayId新建记录存入表中
						String insertThirdId = thirdLoginService.insertThirdId(userId, "zfb_" + alipayUserId);
						// 成功返回1 失败返回0
						if ("1".equals(insertThirdId)) {
							model.addAttribute("message", "1");
						} else {
							model.addAttribute("message", "0");
						}
					}
				}
			}
			return "personal/personalInfo";
		}
		return null;
	}

	/**
	 * 
	 * @return "0" 绑定失败,"1" 绑定成功, "2" 已有绑定，是否更换
	 */
	@RequestMapping("bindThirdId.do")
	@ResponseBody
	public String bindThirdId(String thirdId) {
		try {
			String userId = (String)Token_JWT.verifyToken().get("userId");
			// 查询表中用户是否绑定了该类型的第三方账号
			String count = thirdLoginService.countThirdIdOfThird(userId, thirdId);
			if ("0".equals(count)) {
				// 没有绑定，新建记录，进行绑定
				String result = thirdLoginService.insertThirdId(userId, thirdId);
				if ("1".equals(result)) {
					return "1";
				}
			} else {
				// 已有绑定，前端进行提示
				return "2";
			}
		} catch (Exception e) {
			logger.error("绑定第三方账号出现异常:{}", e);
		}
		return "0";
	}

	/**
	 * 绑定新的第三方账号
	 * 
	 * @return "0" 失败, "1" 成功
	 */
	@RequestMapping("updateThirdId.do")
	@ResponseBody
	public Map<String, Object> updateThirdId(String thirdId) {
		Map<String, Object> resultJson=new HashMap<>();
		int code=400;
		String message="failure";
		try {
			String userId = (String)Token_JWT.verifyToken().get("userId");
			String result = thirdLoginService.updateThirdId(userId, thirdId);
			if ("1".equals(result)) {
				code=600;
				message="success";
			}
		} catch (Exception e) {
			logger.error("更换绑定第三方账号出现异常:{}", e);
			code=401;
		}
		resultJson.put("code", code);
		resultJson.put("message", message);
		return resultJson;
	}

}
