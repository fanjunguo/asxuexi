package cn.asxuexi.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.asxuexi.search.dao.SearchDao;
import cn.asxuexi.search.entity.SearchCondition;
import cn.asxuexi.search.tool.ElasticsearchTool;
import cn.asxuexi.tool.GeoTool;
import cn.asxuexi.tool.Paging;

@Service
public class SearchServiceImpl implements SearchService {
	@Resource
	private SearchDao elasticsearchDao;
	@Autowired
	private ElasticsearchTool elasticsearchTool;

	private final static String COURSE_INDEX_NAME = "course";
	private final static String ORG_INDEX_NAME = "org";

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 接收前台的查询条件，生成Elasticsearch可接受的查询对象，向Dao层传递，最终获得查询结果
	 * 
	 * @param searchCondition
	 *            查询条件
	 * @return 搜索结果，存储在map中 { "pagedate":List对象,结果集
	 *         "totalrows":查询到的记录总数,"toatlpage":总页数,"pagenum":当前页码}
	 */
	public Map<String, Object> searchForResult(SearchCondition searchCondition) {
		String keyword = searchCondition.getKeyword().trim();
		String order = searchCondition.getOrder().trim();
		String sortId = searchCondition.getSortId().trim();
		String pageNum = searchCondition.getPageNum().trim();
		String pageRows = searchCondition.getPageRows().trim();
		String distance = searchCondition.getDistance().trim();
		String type = searchCondition.getType().trim();
		String cityId = searchCondition.getCityId().trim();
		String latitude = searchCondition.getLatitude().trim();
		String longitude = searchCondition.getLongitude().trim();
		// 默认查询课程，将一些参数设定为课程相关参数
		// 关键词检索的字段
		String[] fieldNameArray = new String[] { "course_name", "org_name" };
		// 默认排序字段
		String defaultOrderFieldName = "course_order_weight";
		// 索引名称
		String indexName = COURSE_INDEX_NAME;
		// 查询机构时，更改相应参数
		if ("2".equals(type)) {
			fieldNameArray = new String[] { "org_name" };
			defaultOrderFieldName = "org_order_weight";
			indexName = ORG_INDEX_NAME;
		}
		// 建立bool查询对象
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		// 关键字搜索
		if ("".equals(keyword)) {
			boolQueryBuilder.must(QueryBuilders.matchAllQuery());
		} else {
			boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyword, fieldNameArray));
		}
		// 分类筛选
		if (!"".equals(sortId)) {
			if ("2".equals(type)) {
				// 机构
				QueryBuilder query = QueryBuilders.multiMatchQuery(sortId,
						new String[] { "org_sort.first_sort_id", "org_sort.second_sort_id", "org_sort.third_sort_id" });
				boolQueryBuilder.filter(QueryBuilders.nestedQuery("org_sort", query, ScoreMode.None));
			} else {
				// 课程
				boolQueryBuilder.filter(QueryBuilders.multiMatchQuery(sortId, new String[] {
						"course_sort.first_sort_id", "course_sort.second_sort_id", "course_sort.third_sort_id" }));
			}
		}
		// 地区筛选
		if (!"".equals(cityId)) {
			boolQueryBuilder.filter(
					QueryBuilders.multiMatchQuery(cityId, new String[] { "org_area.county_id", "org_area.city_id" }));
		}
		// 距离筛选
		if (!"".equals(distance)) {
			GeoDistanceQueryBuilder geoDistanceQuery = QueryBuilders.geoDistanceQuery("geo_point");
			geoDistanceQuery.distance(distance, DistanceUnit.KILOMETERS);
			geoDistanceQuery.point(Double.parseDouble(latitude), Double.parseDouble(longitude));
			boolQueryBuilder.filter(geoDistanceQuery);
		}
		// 建立查询参数对象，传入查询参数
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(boolQueryBuilder);
		// 设置返回的结果数量（每页数量）
		int pageSize = Integer.parseInt(pageRows);
		sourceBuilder.size(pageSize);
		// 设置查询结果的起点（分页起点）
		int begin = (Integer.parseInt(pageNum) - 1) * pageSize;
		sourceBuilder.from(begin);
		// 设置结果的排序方式
		switch (order) {
		case "price_asc":
			if (!"2".equals(type)) {
				sourceBuilder.sort(new FieldSortBuilder("package_list.package_price").sortMode(SortMode.MIN)
						.setNestedSort(new NestedSortBuilder("package_list")).order(SortOrder.ASC));
			}
			break;
		case "price_desc":
			if (!"2".equals(type)) {
				sourceBuilder.sort(new FieldSortBuilder("package_list.package_price").sortMode(SortMode.MIN)
						.setNestedSort(new NestedSortBuilder("package_list")).order(SortOrder.DESC));
			}
			break;
		case "distance_asc":
			GeoDistanceSortBuilder sortAsc = SortBuilders.geoDistanceSort("geo_point", Double.parseDouble(latitude),
					Double.parseDouble(longitude));
			sortAsc.order(SortOrder.ASC).unit(DistanceUnit.KILOMETERS).geoDistance(GeoDistance.PLANE);
			sourceBuilder.sort(sortAsc);
			break;
		case "distance_desc":
			GeoDistanceSortBuilder sortDesc = SortBuilders.geoDistanceSort("geo_point", Double.parseDouble(latitude),
					Double.parseDouble(longitude));
			sortDesc.order(SortOrder.DESC).unit(DistanceUnit.KILOMETERS).geoDistance(GeoDistance.PLANE);
			sourceBuilder.sort(sortDesc);
			break;
		// TODO 评分的排序，待评价系统完成后实现
		case "score":
			break;
		default:
//			sourceBuilder.sort(defaultOrderFieldName, SortOrder.DESC);
			break;
		}
		Map<String, Object> result = elasticsearchTool.searchForResult(indexName, sourceBuilder);
		if (!"".equals(latitude)) {
			// 经纬度不为空时，向前端返回距离
			List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("pagedata");
			for (Map<String, Object> map : list) {
				String point = latitude + "," + longitude;
				String anotherPoint = (String) map.get("geo_point");
				Double geoDistance = GeoTool.getDistance(point, anotherPoint);
				map.put("geoDistance", geoDistance);
			}
		}
		long totalRows = ((Long) result.get("totalrows")).longValue();
		long toatlPage = Paging.totalpages(totalRows, pageRows);
		result.put("toatlpage", toatlPage);
		result.put("pagenum", pageNum);
		return result;
	}

	@Override
	public Map<String, Object> searchForCities(String keyword) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("citylist", new ArrayList<Map<String, Object>>());
		if (null != keyword) {
			if (!"".equals(keyword.trim())) {
				String[] fieldNameArray = new String[] { "areas_firstchar", "areas_jianpin", "areas_pinyin",
						"areas_shortname" };
				MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(keyword, fieldNameArray);
				SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(multiMatchQuery);
				String indexName = "city";
				result = elasticsearchTool.searchForCities(indexName, sourceBuilder);
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> getFilterComponent(String cityId) {
		Map<String, Object> result = new HashMap<String, Object>(16);
		Map<String, Object> categoryMap = new HashMap<String, Object>(16);
		List<Map<String, Object>> subjectList = new ArrayList<>(10);
		List<Map<String, Object>> categoryList = new ArrayList<>(10);
		List<Map<String, Object>> sortList = elasticsearchDao.listSorts();
		for (Map<String, Object> map : sortList) {
			String sortGrade = map.get("sort_grade").toString();
			if ("1".equals(sortGrade)) {
				subjectList.add(map);
			}
			if ("2".equals(sortGrade)) {
				categoryList.add(map);
			}
		}
		for (Map<String, Object> category : categoryList) {
			String sortId2 = category.get("sort_id").toString();
			List<Map<String, Object>> subcategoryList = new ArrayList<>(10);
			for (Map<String, Object> map : sortList) {
				String parentId3 = map.get("sort_parentid").toString();
				if (parentId3.equals(sortId2)) {
					subcategoryList.add(map);
				}
			}
			category.put("subcategory", subcategoryList);
		}
		for (Map<String, Object> map : subjectList) {
			String sortId1 = map.get("sort_id").toString();
			List<Map<String, Object>> categories = new ArrayList<>(10);
			for (Map<String, Object> category : categoryList) {
				String parentId2 = category.get("sort_parentid").toString();
				if (sortId1.equals(parentId2)) {
					categories.add(category);
				}
			}
			categoryMap.put(sortId1, categories);
		}
		List<Map<String, Object>> areaList = elasticsearchDao.listAreas(cityId);
		// TODO 品牌表建立后，增加品牌的筛选条件
		result.put("subject", subjectList);
		result.put("category", categoryMap);
		result.put("area", areaList);
		return result;
	}

}
