package cn.asxuexi.service;

public interface AccountIsExistedService {

	String accountIsExisted(String parameter, String flag);

	String countTelOfUser(String tel);

	String countThirdIdOfThird(String userId, String thirdId);

}