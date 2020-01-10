package cn.asxuexi.message.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.asxuexi.message.tool.MessageTool;
import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.Token_JWT;

@RestController
public class MessageController {
	@Autowired
	private MessageTool messageTool;

	/**
	 * 获得用户不同状态消息的数量
	 * 
	 * @param status
	 *            字符串，消息状态，五种。-1：已删除；0：未读；1：已读；2：未读和已读的；3：全部的
	 * @return
	 */
	@GetMapping("message/countUserMessages.action")
	public Map<String, Object> countUserMessages(String status) {
		String userId = (String) Token_JWT.verifyToken().get("userId");
		return messageTool.countMessages(userId, "user", status);
	}

	/**
	 * 获得机构不同状态消息的数量
	 * 
	 * @param status
	 *            字符串，消息状态，五种。-1：已删除；0：未读；1：已读；2：未读和已读的；3：全部的
	 * @return
	 */
	@GetMapping("message/countOrgMessages.action")
	public Map<String, Object> countOrgMessages(String status) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		return messageTool.countMessages(orgId, "org", status);
	}

	/**
	 * 从数据库获取用户消息
	 * 
	 * @param status
	 *            字符串，消息状态，五种。-1：已删除；0：未读；1：已读；2：未读和已读的；3：全部的
	 * @param page
	 *            数字，分页数据，当前页码
	 * @param rows
	 *            数字，分页数据，每页行数
	 * @return
	 */
	@GetMapping("message/listUserMessages.action")
	public Map<String, Object> listUserMessages(String status, int page, int rows) {
		String userId = (String) Token_JWT.verifyToken().get("userId");
		return messageTool.listMessages("user", userId, status, page, rows);
	}

	/**
	 * 从数据库获取机构消息
	 * 
	 * @param status
	 *            字符串，消息状态，五种。-1：已删除；0：未读；1：已读；2：未读和已读的；3：全部的
	 * @param page
	 *            数字，分页数据，当前页码
	 * @param rows
	 *            数字，分页数据，每页行数
	 * @return
	 */
	@GetMapping("message/listOrgMessages.action")
	public Map<String, Object> listOrgMessages(String status, int page, int rows) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		return messageTool.listMessages("org", orgId, status, page, rows);
	}

	/**
	 * 将用户的消息标记为已读
	 * 
	 * @param list
	 *            JSON字符串，消息ID数组
	 * @return
	 */
	@PostMapping("message/readUserMessages.action")
	public Map<String, Object> readUserMessages(String list) {
		String userId = (String) Token_JWT.verifyToken().get("userId");
		return messageTool.updateMessageStatus(userId, "user", list, 1);
	}

	/**
	 * 将机构的消息标记为已读
	 * 
	 * @param list
	 *            JSON字符串，消息ID数组
	 * @return
	 */
	@PostMapping("message/readOrgMessages.action")
	public Map<String, Object> readOrgMessages(String list) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		return messageTool.updateMessageStatus(orgId, "org", list, 1);
	}

	/**
	 * 将用户的消息标记为已删除
	 * 
	 * @param list
	 *            JSON字符串，消息ID数组
	 * @return
	 */
	@PostMapping("message/deleteUserMessages.action")
	public Map<String, Object> deleteUserMessages(String list) {
		String userId = (String) Token_JWT.verifyToken().get("userId");
		return messageTool.updateMessageStatus(userId, "user", list, -1);
	}

	/**
	 * 将机构的消息标记为已删除
	 * 
	 * @param list
	 *            JSON字符串，消息ID数组
	 * @return
	 */
	@PostMapping("message/deleteOrgMessages.action")
	public Map<String, Object> deleteOrgMessages(String list) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		return messageTool.updateMessageStatus(orgId, "org", list, -1);
	}

}
