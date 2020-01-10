package cn.asxuexi.thirdlogin.service;

import java.util.Map;

public interface ThirdLoginService {

	Map<String,Object> isBound(String userId);

	String updateThirdId(String userId, String thirdId);

	String insertThirdId(String userId, String thirdId);

	String countThirdIdOfThird(String userId, String thirdId);

}