package cn.asxuexi.android.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.android.dao.AppLocationDao;

/**
 * @author fanjunguo
 * @version 2018年9月20日 下午4:32:36
 * @description 
 */
@Service
public class AppLocationServiceImpl implements AppLocationService {
	
	@Resource
	AppLocationDao dao;
	
	@Override
	public String getCityId(String cityname) {
		String cityId = dao.getCityId(cityname);
		return cityId;
	}
	
	@Override
	public Map<String, Object> getCityInfo(String cityname){
		Map<String, Object> cityInfo = dao.getCityInfo(cityname);
		return cityInfo;
	}
}
