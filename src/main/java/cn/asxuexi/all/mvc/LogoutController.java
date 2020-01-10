package cn.asxuexi.all.mvc;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.asxuexi.all.service.LogoutService;

/**
 * @author fanjunguo
 * @version 2018年9月16日 下午5:26:56
 * @description 
 */
@Controller
public class LogoutController {
	@Resource
	LogoutService service;
	
	@RequestMapping(value="logout/logout.do")
	//用新方法：返回字符串的方法，重定向
	public String logout() {
		String[] keys=new String[] {"userid","orgid"};
		String logoutResult = service.logout(keys);
		return logoutResult;
	}
}
