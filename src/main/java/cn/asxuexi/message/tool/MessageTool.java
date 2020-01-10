package cn.asxuexi.message.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import cn.asxuexi.message.entity.WechatTemplateParam;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class MessageTool {
	@Autowired
	private OkHttpClient okHttpClient;
	private static String sendMessageUrl = "https://www.asxuexi.cn:8081/server/message";
	private static String countMessagesUrl = "https://www.asxuexi.cn:8081/server/messages/count";
	private static String listUserMessagesUrl = "https://www.asxuexi.cn:8081/server/messages/user";
	private static String listOrgMessagesUrl = "https://www.asxuexi.cn:8081/server/messages/org";
	private static String updateMessageStatusUrl = "https://www.asxuexi.cn:8081/server/messages";
	private static MediaType contentType = MediaType.parse("application/json; charset=utf-8");
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 发送消息给单一收信方
	 * 
	 * @param title
	 *            消息标题，不能为空，最多20字
	 * @param content
	 *            消息内容，不能为空
	 * @param addressee
	 *            收信方ID， 包括：user_id，org_id，staff_id，不能为空
	 * @param pushType
	 *            推送方式， 1：仅推送；2：推送，并短信补推
	 * @param messageType
	 *            消息类型
	 * @param addresseeType
	 *            收信方类型， 1：user；2：org；3：staff
	 * @param wechatTemplateParam
	 *            发送微信模板消息所需要的参数
	 * @return
	 */
	public Map<String, Object> sendMessage(String title, String content, String addressee, int pushType,
			int messageType, int addresseeType, WechatTemplateParam wechatTemplateParam) {
		List<String> addresseeList = new ArrayList<String>();
		addresseeList.add(addressee);
		Map<String, Object> sendMessageToList = sendMessageToList(title, content, addresseeList, pushType, messageType,
				addresseeType, wechatTemplateParam);
		return sendMessageToList;
	}

	/**
	 * 发送同样的消息给多个收信方
	 * 
	 * @param title
	 *            消息标题，不能为空，最多20字
	 * @param content
	 *            消息内容，不能为空
	 * @param addresseeList
	 *            收信方ID列表
	 * @param pushType
	 *            推送方式， 1：仅推送；2：推送，并短信补推
	 * @param messageType
	 *            消息类型
	 * @param addresseeType
	 *            收信方类型， 1：user；2：org；3：staff
	 * @param wechatTemplateParam
	 *            发送微信模板消息所需要的参数
	 * @return
	 */
	public Map<String, Object> sendMessageToList(String title, String content, List<String> addresseeList, int pushType,
			int messageType, int addresseeType, WechatTemplateParam wechatTemplateParam) {
		String addressee = JSON.toJSONString(addresseeList);
		int code = 400;
		String message = "失败";
		Object data = null;
		JSONObject requestDataObject = new JSONObject();
		requestDataObject.put("title", title);
		requestDataObject.put("content", content);
		requestDataObject.put("addresseeArray", addressee);
		requestDataObject.put("pushType", pushType);
		requestDataObject.put("messageType", messageType);
		requestDataObject.put("addresseeType", addresseeType);
		requestDataObject.put("wechatTemplateParam", wechatTemplateParam);
		RequestBody body = RequestBody.create(contentType, requestDataObject.toJSONString());
		Request request = new Request.Builder().url(sendMessageUrl).post(body).build();
		Response response;
		try {
			response = okHttpClient.newCall(request).execute();
			Map<String, Object> map = JSON.parseObject(response.body().string(),
					new TypeReference<Map<String, Object>>() {
					});
			if ("success".equals(map.get("code"))) {
				code = 600;
				message = "成功";
				data = map.get("data");
			}
		} catch (IOException e) {
			logger.error("发生异常", e);
			code = 401;
			message = "发生异常";
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", code);
		result.put("message", message);
		result.put("data", data);
		return result;
	}

	/**
	 * 获得某人不同状态消息的数量
	 * 
	 * @param id
	 *            用户或机构ID
	 * @param type
	 *            两种。user：用户；org：机构
	 * @param status
	 *            字符串，消息状态，五种。-1：已删除；0：未读；1：已读；2：未读和已读的；3：全部的
	 * @return
	 */
	public Map<String, Object> countMessages(String id, String type, String status) {
		int code = 400;
		String message = "失败";
		Object data = null;
		String url = String.format("%s?id=%s&type=%s&status=%s", countMessagesUrl, id, type, status);
		Request request = new Request.Builder().url(url).get().build();
		Response response;
		try {
			response = okHttpClient.newCall(request).execute();
			Map<String, Object> map = JSON.parseObject(response.body().string(),
					new TypeReference<Map<String, Object>>() {
					});
			if ("success".equals(map.get("code"))) {
				code = 600;
				message = "成功";
				data = map.get("data");
			}
		} catch (IOException e) {
			logger.error("发生异常", e);
			code = 401;
			message = "发生异常";
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", code);
		result.put("message", message);
		result.put("data", data);
		return result;
	}

	/**
	 * 获取用户或机构的消息
	 * 
	 * @param collectionName
	 *            MongoDB-Collection名称，user：用户；org：机构
	 * @param id
	 *            用户ID、机构ID
	 * @param status
	 *            消息状态，五种。-1：已删除；0：未读；1：已读；2：未读和已读的；3：全部的
	 * @param page
	 *            分页数据，当前页码
	 * @param rows
	 *            分页数据，每页行数
	 * @return
	 */
	public Map<String, Object> listMessages(String collectionName, String id, String status, int page, int rows) {
		int code = 400;
		String message = "失败";
		Object data = null;
		String url = String.format("%s?id=%s&status=%s&page=%d&rows=%d", listUserMessagesUrl, id, status, page, rows);
		if ("org".equals(collectionName)) {
			url = String.format("%s?id=%s&status=%s&page=%d&rows=%d", listOrgMessagesUrl, id, status, page, rows);
		}
		Request request = new Request.Builder().url(url).get().build();
		Response response;
		try {
			response = okHttpClient.newCall(request).execute();
			Map<String, Object> map = JSON.parseObject(response.body().string(),
					new TypeReference<Map<String, Object>>() {
					});
			if ("success".equals(map.get("code"))) {
				code = 600;
				message = "成功";
				data = map.get("data");
			}
		} catch (IOException e) {
			logger.error("发生异常", e);
			code = 401;
			message = "发生异常";
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", code);
		result.put("message", message);
		result.put("data", data);
		return result;
	}

	/**
	 * 更改某人的消息的状态
	 * 
	 * @param id
	 *            用户、机构ID
	 * @param type
	 *            三种。user：用户；org：机构
	 * @param list
	 *            JSON字符串，消息ID数组
	 * @param status
	 *            要更改为的消息状态，两种。1：已读；-1：已删除
	 */
	public Map<String, Object> updateMessageStatus(String id, String type, String list, int status) {
		int code = 400;
		String message = "失败";
		Object data = null;
		RequestBody body = new FormBody.Builder().add("id", id).add("type", type).add("list", list).build();
		Request request = new Request.Builder().url(updateMessageStatusUrl).put(body).build();
		if (-1 == status) {
			request = new Request.Builder().url(updateMessageStatusUrl).delete(body).build();
		}
		Response response;
		try {
			response = okHttpClient.newCall(request).execute();
			Map<String, Object> map = JSON.parseObject(response.body().string(),
					new TypeReference<Map<String, Object>>() {
					});
			if ("success".equals(map.get("code"))) {
				code = 600;
				message = "成功";
				data = map.get("data");
			}
		} catch (IOException e) {
			logger.error("发生异常", e);
			code = 401;
			message = "发生异常";
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", code);
		result.put("message", message);
		result.put("data", data);
		return result;
	}

}
