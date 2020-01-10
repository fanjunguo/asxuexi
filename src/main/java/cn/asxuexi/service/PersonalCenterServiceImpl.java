package cn.asxuexi.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.dao.PersonalCenterDao;
import cn.asxuexi.entity.PersonalMenu;
import cn.asxuexi.tool.PersonalMenuSort;
import cn.asxuexi.tool.Token_JWT;

@Service
public class PersonalCenterServiceImpl implements PersonalCenterService {
	@Resource
	private PersonalCenterDao personalCenterDao;

	private String asxuexiResource = "/asxuexi_resource/";

	/**
	 * @author 张顺
	 * @作用 请求左菜单数据
	 */
	@Override
	public List<PersonalMenu> getPersonalMenu() {
		PersonalMenuSort personalMenuSort = new PersonalMenuSort();
		List<Map<String, Object>> personal_sort = personalCenterDao.listPersonalMenu();
		List<PersonalMenu> fatherNode = personalMenuSort.getFatherNode(personal_sort);
		return fatherNode;
	}

	/**
	 * @author 张顺
	 * @作用 获取用户信息
	 */
	public Map<String, Object> getUserInfo() {
		Map<String, Object> information = null;
		// 获取用户的id
		String userId = (String)Token_JWT.verifyToken().get("userId");
		if (userId != null) {
			information = personalCenterDao.getUserInfo(userId);
			if (information != null) {
				String photo = (String) information.get("photo");
				if ("".equals(photo) || photo == null) {
				} else {
					String scheme = asxuexiResource + "user/" + userId + "/" + photo;
					information.put("photo", scheme);
				}
			}
		}
		return information;
	}
}
