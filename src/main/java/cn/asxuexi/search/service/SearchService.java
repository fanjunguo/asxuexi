package cn.asxuexi.search.service;

import java.util.Map;

import cn.asxuexi.search.entity.SearchCondition;

public interface SearchService {
	Map<String, Object> searchForResult(SearchCondition searchCondition);

	Map<String, Object> getFilterComponent(String cityId);

	Map<String, Object> searchForCities(String keyword);
}
