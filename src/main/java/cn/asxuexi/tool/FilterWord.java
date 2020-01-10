package cn.asxuexi.tool;

import java.net.URLEncoder;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * @author 张顺
 * @作用  获取敏感字返回状态
 * */
public class FilterWord {
	
	private static String URL="https://aip.baidubce.com/rest/2.0/antispam/v2/spam";
	
	public static Map<String, Object>  filter(String word) {
		Map<String, Object> list = null;
		String access_token = GetTokenBaidu.getAuth();
		// 获取access_token
		try {
			String get_text = get_text(word, URL, access_token);
			list = JSONObject.parseObject(get_text, new TypeReference<Map<String, Object>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	private static String get_text(String content, String url, String accessToken) {
		String param;
		String data = "";
		try {
			// 设置请求的编码
			param = "content=" + URLEncoder.encode(content, "UTF-8");
			// 发送并取得结果
			data = HttpUtil.post(url, accessToken, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
}
