package cn.asxuexi.thirdlogin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.dao.AccountIsExistedDao;
import cn.asxuexi.thirdlogin.dao.ThirdLoginDao;

@Service
public class ThirdLoginServiceImpl implements ThirdLoginService {

	@Resource
	private AccountIsExistedDao accountIsExistedDaoImpl;
	@Resource
	private ThirdLoginDao thirdLoginDao;

	@Override
	public Map<String, Object> isBound(String thirdId) {
		Map<String, Object> map = new HashMap<>(16);
		map.put("isBound", false);
		// 查询该第三方id是否有对应的用户id
		List<Map<String, Object>> userIdList = accountIsExistedDaoImpl.getUserIdOfThird(thirdId);
		if (1 == userIdList.size()) {
			String uesrId = userIdList.get(0).get("user_id").toString();
			map.put("isBound", true);
			map.put("asxuexiUserId", uesrId);
		}
		return map;
	}

	@Override
	public String insertThirdId(String userId, String thirdId) {
		return thirdLoginDao.insertThirdId(userId, thirdId);
	}

	@Override
	public String updateThirdId(String userId, String thirdId) {
		return thirdLoginDao.updateThirdId(userId, thirdId);
	}

	@Override
	public String countThirdIdOfThird(String userId, String thirdId) {
		return thirdLoginDao.countThirdIdOfThird(userId, thirdId);
	}
}
