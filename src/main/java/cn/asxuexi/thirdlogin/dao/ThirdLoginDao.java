package cn.asxuexi.thirdlogin.dao;

import java.util.List;
import java.util.Map;

public interface ThirdLoginDao {

	String insertThirdId(String userId, String thirdId);

	String updateThirdId(String userId, String thirdId);

	List<Map<String, Object>> getUserIdOfThird(String thirdId);

	String countThirdIdOfThird(String userId, String thirdId);

}
