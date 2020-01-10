package cn.asxuexi.service;

import org.springframework.stereotype.Service;

import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.Token_JWT;

/**
 * @author fanjunguo
 * @version 2018年11月21日 上午10:42:12
 * @description 详见controller层
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {
	/**
	 * @description 通过session判断用户是否登录,以及是否注册过机构;根据参数folderName判断客户端的请求:
	 * 				1.如果是请求个人中心,并且没有登录,跳转到个人登录界面;
	 * 				2.如果是请求机构中心,并且没有登录个人账户,跳转到个人登录
	 * 				3.如果已经登录个人账户,但是没有注册过机构,跳转到机构注册页面
	 * 				
	 */
	@Override
	public String isLogin(String folderName,String pageName) {
		String slash="/";
		String whichPage="";
		//不管是进个人中心还是机构中心,一开始都要先判断是否登录个人账户
		String userId = (String)Token_JWT.verifyToken().get("userId");
		//个人中心登录页面地址
		String loginPath="login/log_in";
		if(null==userId) {
			//如果没登录,跳个人登录页面
			whichPage= loginPath;
		}else {
			//如果登录了,判断客户端请求的是个人中心还是机构中心
			whichPage= folderName+slash+pageName;
			//如果是初次进入机构中心,需要跳注册页面
			if (folderName.equals("org")&&null==GetOrgIdFromRedis.getOrgId()) {
				whichPage= "org/orgRegister";
			}
		}
		return whichPage;	
	}
}
