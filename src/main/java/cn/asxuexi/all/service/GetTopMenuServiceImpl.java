package cn.asxuexi.all.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.all.dao.GetTopMenuDao;
import cn.asxuexi.tool.Token_JWT;
/**
 * @author fanjunguo
 * @version 2018年9月16日 下午4:43:49
 * @description 
 */
@Service
public class GetTopMenuServiceImpl implements GetTopMenuService {

	@Resource
	private GetTopMenuDao dao;
	
	@Override
	public List<Map<String, Object>> getmenu(){
		List<Map<String, Object>> list = dao.getmenu();
		String userid = (String) Token_JWT.verifyToken().get("userId");
		
		//如果userid不为空，则替换顶部菜单
		if (userid!=null) {
			Map<String, Object> message = dao.getTelAndName(userid);
			Map<String, Object> map0=new HashMap<>();
			//判断用户是否设置用户名:如果有用户名,展示用户名;如果没有用户名,展示电话
			Object name = message.get("name");
			if (null==name) {
				name=message.get("tel");
			}
			map0.put("name", "["+name+"]");
			map0.put("href", "javascript:void(0)");
			Map<String, Object> map1=new HashMap<>();
			map1.put("name", "注销");
			map1.put("href", "logout/logout.do");
			list.set(0, map0);
			list.set(1, map1);
		}
		return list;
	}
}
