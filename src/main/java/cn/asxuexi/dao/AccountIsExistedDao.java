package cn.asxuexi.dao;

import java.util.List;
import java.util.Map;

public interface AccountIsExistedDao {

	String countUser(String parameter, String flag);

	String countTelOfUser(String tel);

	List<Map<String,Object>> getUserIdOfThird(String thirdId);

	String countThirdIdOfThird(String userId, String thirdId);


}