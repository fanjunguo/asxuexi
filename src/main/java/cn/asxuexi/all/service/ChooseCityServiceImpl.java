package cn.asxuexi.all.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.all.dao.ChooseCityDao;

@Service
public class ChooseCityServiceImpl implements ChooseCityService {

	@Resource
	ChooseCityDao dao;
	
	private  List<String> firstchar=new ArrayList<>();
	
	@Override
	public List<Map<String, Object>> listProvinces() {
		List<Map<String, Object>> list = dao.listProvinces();
		return list;
	}
	
	@Override
	public List<Map<String, Object>>  listCities(String id){
		List<Map<String, Object>> citylist = dao.listCities(id);
		return citylist;

	}
	
	@Override
	public List<String> listFirstChars() {
		firstchar = dao.listFirstChars();
		return firstchar;
	}
	
	@Override
	public Map<String, List<Map<String, Object>>> getCitylist(){
		List<Map<String, Object>> allCity = dao.getCitylist();
		Map<String, List<Map<String, Object>>> citylist=new HashMap<>();
		if (firstchar.size()==0) {
			listFirstChars();
		}
		for (String letters : firstchar) {
			List<Map<String, Object>> eachCity=new ArrayList<>();
			//把同个字母的map放到同一个的list下
			for (Map<String, Object> map : allCity) {
				if((map.get("firstchar")).equals(letters)) {
					eachCity.add(map);
				}
			}
			citylist.put(letters, eachCity);
		}
		return citylist;
	}
	
	
	@Override
	public Map<String, List<Map<String, Object>>> getCitylistForH5(){
		List<Map<String, Object>> allCity = dao.getCitylistForAndroid();
		List<Map<String, Object>> topCity = dao.listTopCitiesForH5();
		Map<String, List<Map<String, Object>>> citylist=new LinkedHashMap<>();
		citylist.put("hot", topCity);
		if (firstchar.size()==0) {
			listFirstChars();
		}
		for (String letters : firstchar) {
			List<Map<String, Object>> eachCity=new ArrayList<>();
			//把同个字母的map放到同一个的list下
			for (Map<String, Object> map : allCity) {
				if((map.get("firstchar")).equals(letters)) {
					eachCity.add(map);
				}
			}
			citylist.put(letters, eachCity);
		}
		return citylist;
	}
	
	@Override
	public Map<String, List<Map<String, Object>>> getAllCityForAndroid() {
		//为了防止firstchar没有数据,在这里判断一下
		if (firstchar.size()==0) {
			listFirstChars();
		}
		List<Map<String, Object>> allCity = dao.getCitylistForAndroid();  //所有城市的数据的集合
		
		Map<String, List<Map<String, Object>>> json=new HashMap<>();
		List<Map<String, Object>> data=new ArrayList<>();
		for (String letter : firstchar) {
			Map<String, Object> eachLetter=new HashMap<>();
			List<Map<String, Object>> eachCity=new ArrayList<>();//每个字母的集合
			//遍历所有城市总集合
			for (Map<String, Object> map : allCity) {
				if ((map.get("firstchar")).equals(letter)) {
					eachCity.add(map);
				}
			}
			eachLetter.put("firstChar", letter);
			eachLetter.put("cityList", eachCity);
			data.add(eachLetter);
		}
		json.put("data", data);
		return json;
	}
	
	
	@Override
	public List<Map<String, Object>> listTopCities(){
		List<Map<String, Object>> topcity = dao.listTopCities();
		return topcity;
	}

	@Override
	public Map<String, List<Map<String, Object>>> getTopCityForAndroid() {
		List<Map<String, Object>> topCity = dao.listTopCitiesForAndroid();
		Map<String, List<Map<String, Object>>> json=new HashMap<String, List<Map<String, Object>>>();
		json.put("data", topCity);
		return json;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAreasOfJson() {
		List<Map<String, Object>> areas = dao.getAreasOfJson();
		List<Map<String, Object>> province=new ArrayList<Map<String, Object>>();
		
		//第一次循环,找出所有的省份数据
		for (Map<String, Object> eachAreas : areas) {
			if ("1".equals(eachAreas.get("leveltype"))) {
				province.add(eachAreas);
			}
		}
		
		//第二次循环,找出每个省份下的所有城市
		for (Map<String, Object> eachProvice : province) {
			List<Map<String, Object>> city=new ArrayList<Map<String, Object>>();
			for (Map<String, Object> eachAreas : areas) {
				if (eachProvice.get("id").equals(eachAreas.get("parentid"))) {
					city.add(eachAreas);
				}
			}
			eachProvice.put("city", city);
		}
		//第三次循环,找出每个城市下所有的县区
		for (Map<String, Object> eachProvince : province) {
			List<Map<String, Object>> city = (List<Map<String, Object>>) eachProvince.get("city");
			for (Map<String, Object> eachCity :city ) {
				List<Map<String, Object>> country=new ArrayList<Map<String, Object>>();
				for (Map<String, Object> eachAreas : areas) {
					if (eachCity.get("id").equals(eachAreas.get("parentid"))) {
						country.add(eachAreas);
					}
				}
				eachCity.put("country", country);
			}
		}
		return province;
	}
}
