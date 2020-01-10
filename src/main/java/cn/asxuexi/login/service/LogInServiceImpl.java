package cn.asxuexi.login.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.asxuexi.entity.UserInfo;
import cn.asxuexi.login.dao.LogInDao;
import cn.asxuexi.tool.Token_JWT;

@Service
public class LogInServiceImpl implements LogInService {

	@Resource
	private LogInDao logInDaoImpl;

	@Override
	public String setNewPassword(UserInfo logupUser) {
		String back = String.valueOf(logInDaoImpl.updatePassword(logupUser));
		return back;
	}

	/**
	 * @descriptioin 用户登录
	 */
	@Override
	public Map<String, Object> login(String tel, String password) {
		Map<String, Object> json = new HashMap<String, Object>();
		Map<String, Object> userInfo = logInDaoImpl.getUserId(tel, password);
		int code;
		String message;
		if (null != userInfo) {
			String userId = (String) userInfo.get("id");
			String token = Token_JWT.createToken(userId);
			HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			String userAgent = req.getHeader("user-agent");
			// 保存手机的设备号，用于推送消息
			if ("Android".equals(userAgent) || "IOS".equals(userAgent)) {
				String clientId = req.getHeader("client-id");
				int updateClientId = logInDaoImpl.updateClientId(userId, clientId);
				if (1 == updateClientId) {
					// TODO 将clientId与pn(md5后的手机号)绑定，用于个推短信补发（绑定方法见个推文档）
				}
			}
			code = 600;
			message = "success";
			json.put("token", token);
			Cookie cookie = new Cookie("access-token", token);
			cookie.setPath("/");
			cookie.setHttpOnly(true); // 设置httponly
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getResponse();
			response.addCookie(cookie);
		} else {
			message = "failure";
			code = 400;
		}
		json.put("code", code);
		json.put("message", message);
		return json;
	}
}
