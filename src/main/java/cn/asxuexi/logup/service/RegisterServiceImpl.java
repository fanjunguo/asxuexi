package cn.asxuexi.logup.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import cn.asxuexi.entity.UserInfo;
import cn.asxuexi.logup.dao.RegisterDao;
import cn.asxuexi.tool.Token_JWT;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Resource
	private RegisterDao registerDao;

	@Override
	public Map<String, Object> logup(UserInfo logupUser,HttpServletResponse response) {
		String id = getUserId();
		//注册的时候,生成默认昵称
		String name="as_"+logupUser.getTel();
		logupUser.setId(id);
		logupUser.setName(name);
		String result = registerDao.insertUserInfo(logupUser);
		Map<String, Object> json=new HashMap<>();
		int code=400;
		String message="failure";
		if (result=="1") {
			//成功之后,生成token,并存入cookie
			String token = Token_JWT.createToken(id);
			json.put("token", token);
			code=600;
			message="success";
			
			Cookie cookie=new Cookie("access-token", token);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		json.put("code", code);
		json.put("message", message);
		
		return json;
	}
	@Override
	public String codeIsMatched(String telcode, String tel) {
		String isMatched = "false";
		LocalDateTime now = LocalDateTime.now();
		List<Map<String, Object>> list = registerDao.getDateTimeAndCode(tel);
		if (1 == list.size()) {
			String code = (String) list.get(0).get("telcode");
			// 计算当前时刻和上次发送验证码时间的时间差（分钟为单位）
			Timestamp timestamp = (Timestamp) list.get(0).get("gmt_create");
			LocalDateTime lastTime = timestamp.toLocalDateTime();
			long minutesDifference = Duration.between(lastTime, now).toMinutes();
			Object obj = list.get(0).get("gmt_modified");
			if (null != obj) {
				timestamp = (Timestamp) obj;
				lastTime = timestamp.toLocalDateTime();
				minutesDifference = Duration.between(lastTime, now).toMinutes();
			}
			if (minutesDifference <= 5) {
				if (code.equals(telcode)) {
					//时间间隔小于5分钟，并且前端验证码和数据表中验证码相等，代表验证通过
					isMatched = "true";
				}
			}
		}
		return isMatched;
	}

	// 生成用户id
	public static String getUserId() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dt = sdf.format(new Date());
		String rd = Math.round(Math.random() * 900) + 100 + "";
		String id = "user_" + dt + rd;
		return id;

	}

	
}
