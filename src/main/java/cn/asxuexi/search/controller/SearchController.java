package cn.asxuexi.search.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.search.entity.SearchCondition;
import cn.asxuexi.search.service.SearchService;

@Controller
public class SearchController {
	@Resource
	private SearchService elasticsearchService;

	@RequestMapping("search.do")
	public String getResultListPage(HttpServletRequest request, Model model) {
		String keyword = request.getParameter("keyword");
		String searchCityId = request.getParameter("searchCityId");
		String searchCityName = request.getParameter("searchCityName");
		String searchContentType = request.getParameter("searchContentType");
		String searchSortId = request.getParameter("searchSortId");
		String searchSortIdLevel = request.getParameter("level");
		if (searchCityId==null) {
			Cookie[] cookies = request.getCookies();
			if (null!=cookies) {
				for (Cookie cookie : cookies) {
					if ("cityid".equals(cookie.getName())) {
						searchCityId=cookie.getValue();
					}
					if ("cityname".equals(cookie.getName())) {
						searchCityName=cookie.getValue();
					}
				}
			}
		}
		if (null==searchContentType) {
			searchContentType="course";
		}
		model.addAttribute("search_city_id", searchCityId);
		model.addAttribute("search_city_name", searchCityName);
		model.addAttribute("search_content_type", searchContentType);
		model.addAttribute("search_sort_id", searchSortId);
		model.addAttribute("search_keyword", keyword);
		model.addAttribute("search_sort_level", searchSortIdLevel);
		return "resultList";
	}

	@RequestMapping("searchForResult.do")
	@ResponseBody
	public Map<String, Object> searchForResult(SearchCondition searchCondition) {
		Map<String, Object> result = elasticsearchService.searchForResult(searchCondition);
		return result;
	}
	
	@RequestMapping("searchForCities.do")
	@ResponseBody
	public Map<String, Object> searchForCities(String keyword) {
		Map<String, Object> result = elasticsearchService.searchForCities(keyword);
		return result;
	}
	
	@RequestMapping("getFilterComponent.do")
	@ResponseBody
	public Map<String, Object> getFilterComponent(String cityId) {
		Map<String, Object> result = elasticsearchService.getFilterComponent(cityId);
		return result;
	}
}
