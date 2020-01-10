package cn.asxuexi.tool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import cn.asxuexi.login.dao.LogInDao;

/**
 * @author fanjunguo
 * @version 2019年2月25日 下午9:24:38
 * @description token相关的工具类,包括生成token,验证token以及通过解析token获得用户id
 * 			要求app和微信端,在发送请求时,设置请求头字段user-agent.app设置“app”,微信端设置“wechat”
 */
@Component
public class Token_JWT {
	
	private static final String SECRET="asxuexi";
	private static Logger logger=LoggerFactory.getLogger(Token_JWT.class);
	
	
	private static RedisTool redis;
	private static LogInDao logInDaoImpl;
	
	@Autowired(required=true)
	public void SetRedis(RedisTool redis) {
		Token_JWT.redis=redis;
	}
	
	@Autowired(required=true)
	public void SetLogInDaoImpl(LogInDao logInDaoImpl) {
		Token_JWT.logInDaoImpl=logInDaoImpl;
	}
	
	/**
	 * @author fanjunguo
	 * @description 使用JWT规则,生成token.!!并将生成的token以hashmap的形式存入缓存
	 * @param userId 
	 * @return Sting token字符串
	 */
	public static String createToken(String userId) {
		//首先判断请求登录的用户端类型
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String userAgent = req.getHeader("user-agent");
		String clientType=getClientType(userAgent);
				
		//定义header
		Map<String, Object> header=new HashMap<String, Object>(); 
		header.put("alg", "HS256");
		header.put("typ", "JWT");
		Date issuedAt=new Date(); //签发时间
		/* 过期时间:要根据客户端不同,区别定义.
		 * 19.3.11更新: 如果强制规定30分钟后,token失效,对于正在操作的用户突然登录失效,体验很不好.所以修改规则:
		 * 				生成token的时候不再定义失效时间,只在redis中存token的有效时间;
		 * 				每次用户验证token,并且成功时,验证redis中token的有效时间
		 * */
		Long redisTime; //redis的缓存时间(min)
		if("Android".equals(clientType)||"wechat".equals(clientType)||"IOS".equals(clientType)) {
			redisTime=30*24*60L;
		}else {
			redisTime=30L;
		}
		String token=JWT.create().withHeader(header)
				.withClaim("iss", "asxuexi") //签发方
				.withClaim("aud", clientType) //使用方
				.withClaim("userId", null == userId ? null : userId) //自定义内容
				.withIssuedAt(issuedAt)
				.sign(Algorithm.HMAC256(SECRET));
		//将token存入缓存
		redis.addString("token:"+token, userId+"_"+userAgent,redisTime , TimeUnit.MINUTES);
		storeOrgIdToRedis(userId);
		return token;
	}
	
	/**
	 * @author fanjunguo
	 * @description 查询该用户是否开通机构,如果开通,将机构id存入缓存
	 * @param userId
	 */
	public static void storeOrgIdToRedis(String userId) {
		//存缓存之前,先验证此用户缓存是否已经存在
		boolean existed = redis.isExisted(userId);
		if (!existed) {
			String orgId = logInDaoImpl.getOrgId(userId);
			if (null!=orgId) {
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("orgId", orgId);
				redis.addHash(userId, map, 24L, TimeUnit.HOURS);
			}
		}
	}

	/**
	 * @author fanjunguo
	 * @description 验证token.
	 * @return 标准json:如果token有效,code=600,userId=用户id;如果token无效,code=400,userId==null
	 */
	public static  Map<String, Object> verifyToken(){
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String token = request.getHeader("access-token");
		//(兼容pc)如果请求头中获取不到token,从cookie中获取
		if (null==token) {
			Cookie[] cookies = request.getCookies();
			if(cookies != null && cookies.length>0) {
				for (Cookie cookie : cookies) {
					if ("access-token".equals(cookie.getName())) {
						token=cookie.getValue();
					}
				}
			}
		}
		Map<String, Object> resultJson=new HashMap<>();
		int code=400;
		try {
			Algorithm hmac256 = Algorithm.HMAC256(SECRET);
			JWTVerifier verifier = JWT.require(hmac256)  //生成验证器,并要求token中必须带有签发方“asxuexi”和使用方的信息
					.withClaim("iss","asxuexi")
					.build();
			DecodedJWT jwt = verifier.verify(token); //验证token
			//去缓存中验证token
			boolean existed = redis.isExisted("token:"+token);
			if (existed) {
				Claim claim = jwt.getClaim("userId");
				String clientType = jwt.getClaim("aud").asString();
				//更新redis和cookie中,token的有效时间.也更新缓存中orgId的时间
				Long redisTime; //redis的缓存时间(min)
				if("Android".equals(clientType)||"wechat".equals(clientType)||"IOS".equals(clientType)) {
					redisTime=30*24*60L;
				}else {
					redisTime=30L;
				}
				redis.setExpiryMinutes("token:"+token, redisTime);
				String userId = claim.asString();
				code=600;
				resultJson.put("userId", userId);
			}
		} catch (Exception e) {
			logger.debug("未登录或登录失效");
		}
		resultJson.put("code", code);
		return resultJson;
	}

	/**
	 * 私有方法,解析http请求头的user-agent,来判断请求的客户端类型.
	 * 		在安卓端,主要设置user-agent为“Android”.
	 * 		由于在微信内置浏览器无法更改user-agent,但是微信的user-agent中包含关键字“MicroMessenger”
	 * 
	 * @description 
	 * @param userAgent 
	 * @return String 客户端类型
	 * 
	 * 
	 */
	private static String getClientType(String userAgent) {
		String clientType;
		if ("Android".equals(userAgent)||"IOS".equals(userAgent)) {
			clientType=userAgent;
		}else if(null!=userAgent&userAgent.indexOf("MicroMessenger")!=-1) {
			clientType="wechat";
		}else {
			clientType="pc";
		}
		return clientType;
		
	}
}
