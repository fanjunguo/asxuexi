package cn.asxuexi.search.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.asxuexi.tool.FileTool;

@Component
public class ElasticsearchTool {
	@Autowired
	private RestHighLevelClient client;
	private static Logger logger = LoggerFactory.getLogger(ElasticsearchTool.class);

	/**
	 * 使用Elasticsearch搜索引擎进行搜索
	 * 
	 * @param indexName
	 *            索引名称
	 * @param indexType
	 *            索引类型
	 * @param sourceBuilder
	 *            查询参数对象
	 * @return 搜索结果，存储在map中 { "pagedate":List对象,结果集 "totalrows":查询到的记录总数}
	 */
	public Map<String, Object> searchForResult(String indexName, SearchSourceBuilder sourceBuilder) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> pageData = new ArrayList<Map<String, Object>>();
		// 建立查询请求，传入参数对象（索引名称，类型，查询参数）
		SearchRequest searchRequest = new SearchRequest(indexName).source(sourceBuilder);
		try {
			SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = response.getHits();
			SearchHit[] searchhits = hits.getHits();
			// 将结果集数组转化为Map队列
			for (SearchHit searchHit : searchhits) {
				pageData.add(searchHit.getSourceAsMap());
			}
			// 将当页结果集以及其他参数存入Map中
			long totalRows = hits.getTotalHits().value;
			result.put("pagedata", pageData);
			result.put("totalrows", totalRows);
		} catch (IOException e) {
			logger.error("搜索课程或机构时发生错误",e);
		}
		return result;
	}

	public Map<String, Object> searchForCities(String indexName, SearchSourceBuilder sourceBuilder) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> cityList = new ArrayList<Map<String, Object>>();
		// 建立查询请求，传入参数对象（索引名称，类型，查询参数）
		SearchRequest searchRequest = new SearchRequest(indexName).source(sourceBuilder);
		try {
			SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = response.getHits();
			SearchHit[] searchhits = hits.getHits();
			// 将结果集数组转化为Map队列
			for (SearchHit searchHit : searchhits) {
				cityList.add(searchHit.getSourceAsMap());
			}
			result.put("citylist", cityList);
		} catch (IOException e) {
			logger.error("搜索城市时发生错误",e);
		}
		return result;
	}

	public void deleteCourseDocumentsById(String indexName, List<String> documentIdList) {
		DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
		request.setQuery(QueryBuilders.termsQuery("_id", documentIdList));
		try {
			client.deleteByQuery(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			logger.error("删除课程文档时发生错误",e);
		}
	}

}
