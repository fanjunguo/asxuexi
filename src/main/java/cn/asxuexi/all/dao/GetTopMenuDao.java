package cn.asxuexi.all.dao;

import java.util.List;
import java.util.Map;

public interface GetTopMenuDao {

	List<Map<String, Object>> getmenu();

	Map<String, Object> getTelAndName(String userid);

}