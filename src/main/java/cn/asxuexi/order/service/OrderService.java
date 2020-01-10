package cn.asxuexi.order.service;

import java.util.Map;

import cn.asxuexi.order.entity.OrderEntity;
import cn.asxuexi.tool.JsonData;

public interface OrderService {


	/**
	 * 创建订单
	 * 
	 * @param order 订单实体类
	 * @return
	 */
	Map<String, Object> createOrder(OrderEntity order);

	/**
	 * 根据订单号,查询待支付的订单信息
	 * 
	 * @author fanjunguo
	 * @param orderId
	 * @return  json
	 */
	Map<String, Object> getOrderInfo(String orderId);


	/**
	 * 根据订单号,查询订单的状态
	 * 
	 * @author fanjunguo
	 * @param orderId
	 * @return Integer,订单号正确,返回订单状态码.否则返回null
	 */
	JsonData getOrderPaymentResult(String orderId);

	/**
	 * 个人订单中心——查询所有的订单信息
	 * @param page 分页信息:页数
	 * @param rows 分页信息:每页行数
	 * @param orderStatus 订单状态
	 * 
	 */
	Map<String, Object> getAllOrders(int page, int rows, int orderStatus);

	/**
	 * 取消订单
	 * 
	 * @param orderId
	 * @param reasonCode 
	 * @param isOrg 
	 * @return json
	 */
	Map<String, Object> cancelOrder(String orderId, int reasonCode, boolean isOrg);

	/**
	 * 根据订单号,查询订单详细信息
	 * 
	 * @author fanjunguo
	 * @param orderId
	 * @return
	 */
	Map<String, Object> getOrderDetail(String orderId);

	/**
	 * 
	 * 
	 * @author fanjunguo
	 * @return
	 */
	Map<String, Object> getBillOfUser();

	/**
	 * 查询所有的机构订单
	 * 
	 * @author fanjunguo
	 * @param page 分页信息:页数
	 * @param rows 分页信息:每页行数
	 * @param orderStatus 订单状态
	 * @return
	 */
	Map<String, Object> getAllOrdersOfOrg(int page, int rows, int orderStatus);

	/**
	 * 删除订单
	 * 
	 * @date 2019.6.1
	 * @param orderId 订单id
	 * @param identity 客户端类型:person-普通用户 org-机构
	 * @return 删除结果
	 */
	Map<String, Object> deleteOrder(String orderId, String identity);

	/**
	 * 方法用于更新订单状态,变为[已支付]
	 * 
	 * @param orderId
	 */
	JsonData updateOrderStatus(String orderId);

	/**
	 * 用户付款给机构
	 * 
	 * @param orderId 订单号
	 */
	JsonData PersonPayToOrg(String orderId);



}