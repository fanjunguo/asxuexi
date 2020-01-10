package cn.asxuexi.all.session;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 
 * @author fanjunguo
 * @version 1.0
 * @description 对session信息进行处理的Controller类
 */
@Controller
public class LocationController {

	@Resource
	private LocationService service;
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="location/ipLocation.do")
	@ResponseBody
	public Map<String, Object> ipLocation(String ip,HttpServletResponse resp) {
		Map<String, Object> location = service.ipLocation(ip);
		//增加cookie (by bufanpu)
		Cookie cityIdCookie=new Cookie("cityid", (String)location.get("cityid"));
		cityIdCookie.setPath("/");
		cityIdCookie.setComment("城市编号");
		Cookie cityNameCookie=new Cookie("cityname", (String)location.get("cityname"));
		cityNameCookie.setPath("/");
		cityNameCookie.setComment("城市名称");
		resp.addCookie(cityNameCookie);
		resp.addCookie(cityIdCookie);
		return location;
	}
	
	/**
	 * 切换城市后，重定向
	 */
	@RequestMapping(value="location/redirect.do")
	@ResponseBody
	public void redirect(HttpServletRequest request,HttpServletResponse resp,String cityid,String cityname) {
		String contextPath = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath+ "/";
		String pagePath="pagers/homepage.jsp";

		//增加cookie (by bufanpu)
		Cookie cityIdCookie=new Cookie("cityid", cityid);
		cityIdCookie.setPath("/");
		cityIdCookie.setComment("城市编号");
		Cookie cityNameCookie=new Cookie("cityname", cityname);
		cityNameCookie.setPath("/");
		cityNameCookie.setComment("城市名称");
		resp.addCookie(cityNameCookie);
		resp.addCookie(cityIdCookie);
		try {
			resp.sendRedirect(basePath+pagePath);
		} catch (IOException e) {
			logger.error("重定向发生异常:{}", e);
		}
		
	}
}
