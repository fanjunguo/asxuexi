package cn.asxuexi.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.dao.AccountIsExistedDao;


@Service
public class AccountIsExistedServiceImpl implements AccountIsExistedService {
	
	@Resource
	private AccountIsExistedDao accountIsExistedDao;
	
	@Override
	public String accountIsExisted(String parameter,String flag) {
		return accountIsExistedDao.countUser(parameter,flag);
		 
	}
	
	@Override
	public String countTelOfUser(String tel) {
		return accountIsExistedDao.countTelOfUser(tel);
		 
	}
	
	@Override
	public String countThirdIdOfThird(String userId,String thirdId) {
		return accountIsExistedDao.countThirdIdOfThird(userId, thirdId);
	}
}
