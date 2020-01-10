package cn.asxuexi.all.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.tool.RedisTool;

/**
 * @author fanjunguo
 * @version 2018年9月17日 上午9:08:54
 * @description 注销登录
 */
@Service
public class LogoutServiceImpl implements LogoutService {

	@Resource
	RedisTool redis;
	
	/**
	 * 注销登录,清除缓存中的token数据
	 */
	@Override
	public String logout(String[] keys) {
		String success="login/log_in";
		redis.delToken();
		return success;
	}
}
