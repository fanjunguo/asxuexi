package cn.asxuexi.filterWord.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import cn.asxuexi.tool.FilterWord;

/**
 * @author 张顺
 * @作用 过滤敏感词汇
 */
@Controller
public class FilterWordController {
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("filterWord/isFilterWord.do")
	public List<Map<String, Object>> isFilterWord(String context) {
		List<Map<String, Object>> list = JSONObject.parseObject(context,new TypeReference<List<Map<String, Object>>>() {});
		for (Map<String, Object> map : list) {
			String text = (String) map.get("text");
			Map<String, Object> filter = FilterWord.filter(text);
			Map<String, Object> result = (Map<String, Object>) filter.get("result");
			if (result != null) {
				Integer spam = (Integer) result.get("spam");
				//根据百度检测的api,1是明确命中违禁词,2是可能有违禁词  两个都过滤  by junguo.fan
				if (spam == 1) {
					List<Map<String, Object>> reject = (List<Map<String, Object>>) result.get("reject");
					List<String> hit = (List<String>) reject.get(0).get("hit");
					if(hit.size()>0) {
						String string = hit.get(0);
						map.put("text", string);
					}
				} else if(spam==2){
					List<Map<String, Object>> review = (List<Map<String, Object>>)result.get("review");
					List<String> hit = (List<String>) review.get(0).get("hit");
					if(hit.size()>0) {
						String string = hit.get(0);
						map.put("text", string);
					}
				}else {
					map.put("text", "");
				}
			}
			/*	2019.6.19 范俊国
			 * 	经测试,敏感词校验接口有点问题,检测返回结果有时候会是空,这个时候会误以为是敏感词汇.
			 * 	在这里暂时“粗糙”处理一下,将空的情况处理为合法词汇
			 * 
			 * 	todo:重构敏感词校验代码
			 * 
			 * */
			else {
				map.put("text", "");
			}
		}
		return list;
	}
}
