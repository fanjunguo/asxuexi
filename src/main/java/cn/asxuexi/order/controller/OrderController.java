package cn.asxuexi.order.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.message.entity.WechatTemplateParam;
import cn.asxuexi.message.tool.MessageTool;
import cn.asxuexi.order.entity.OrderEntity;
import cn.asxuexi.order.service.OrderService;
import cn.asxuexi.tool.DateTimeTool;
import cn.asxuexi.tool.JsonData;

/**
 * 网站订单相关的controller层
 * @author fanjunguo
 * @version 2019年4月12日 上午10:43:41
 */
@Controller
public class OrderController {
	
	@Resource
	private OrderService service;
	@Autowired
	private MessageTool messageTool;
	
	/**
	 * 生成订单
	 * @author fanjunguo
	 * @param studentId 上课人id
	 * @param packageId 套餐id
	 * @param paymentAmount 付款金额
	 * @return 生成订单的结果
	 */
	@RequestMapping(value="order/createOrder.action")
	@ResponseBody
	public Map<String, Object> createOrder(OrderEntity order){
		Map<String, Object> json = service.createOrder(order);
		if ((int)json.get("code") == 600) {
			
			String courseName = order.getCourseName()+"-"+order.getPackageName();
			//发消息给机构
			WechatTemplateParam	wechatTemplateParam = new WechatTemplateParam();
			wechatTemplateParam.setTemplateIndex(3);// 微信消息模板（订单取消通知）序号：1
			wechatTemplateParam.setKeyword1(courseName);// 课程名称
			wechatTemplateParam.setKeyword2(String.valueOf(order.getPaymentAmount()));// 费用
			wechatTemplateParam.setKeyword3("");// 
			wechatTemplateParam.setKeyword4(order.getTel());// 联系电话
			messageTool.sendMessage("新订单提醒", "您有新的报名订单", order.getOrgId(), 2, 203, 2, wechatTemplateParam);
			
			//发消息给用户
			WechatTemplateParam	wechatTemplateParam2 = new WechatTemplateParam();
			wechatTemplateParam2.setTemplateIndex(5);
			wechatTemplateParam2.setKeyword1(courseName);
			wechatTemplateParam2.setKeyword2("报名成功");
			messageTool.sendMessage("报名成功", "您的课程:"+courseName+",已报名成功", order.getUserId(), 2, 103, 1, wechatTemplateParam2);
		}
		return json;
	}
	
	
	/**
	 * 根据订单号,查询待支付的订单信息
	 * 
	 * @author fanjunguo
	 * @param orderId 订单号
	 * @return 数据结构:
	 * {
		    "code": 600,
		    "data": {
		        "gmt_create": 1560820493517,
		        "course_id": "course_1559894031717705",
		        "charge_type": 1,
		        "coursename": "初中素描",
		        "payment_amount": 1500,
		        "student_id": "student_1559917481189801",
		        "package_id": "package_1559894031771574",
		        "course_end": 1575043200,
		        "student_tel": "15862323265",
		        "order_status": 10,
		        "student_name": "刘小备",
		        "person_visibility": 1,
		        "user_id": "user_20190131215458713184",
		        "course_begin": 1559836800,
		        "package_name": "二学期",
		        "org_visibility": 1,
		        "order_id": "156082049351640573350"
		    },
		    "message": "success"
		}
	 */
	@RequestMapping(value="order/getOrderInfo.action")
	@ResponseBody
	public Map<String, Object> getOrderInfo(String orderId){
		return service.getOrderInfo(orderId);
	}
	
	
	
	/**
	 * 支付完成后,查询订单状态
	 * 
	 * @author fanjunguo
	 * @param orderId
	 * @return
	 */
	@RequestMapping("order/getOrderPaymentResult.action")
	@ResponseBody
	public  JsonData getOrderPaymentResult(@RequestParam(required=true) String orderId){
		
		return service.getOrderPaymentResult(orderId);
	}
	
	
	/**
	 * 个人订单中心——查询所有的订单信息
	 * 
	 * @author fanjunguo
	 * @param identity 请求订单的客户端身份:person-普通用户;org-机构
	 * @param orderStatus 订单状态.非必填,如果不传,默认查询全部状态订单
	 * @param page 分页信息:页数
	 * @param rows 分页信息:每页行数
	 * @return json
	 */
	@RequestMapping("order/getAllOrders.action")
	@ResponseBody
	public Map<String, Object> getAllOrders(@RequestParam(required=true)String identity,@RequestParam(defaultValue="1")int page,@RequestParam(defaultValue="100")int rows,
			@RequestParam(defaultValue="0") int orderStatus){
		String person="person";
		String org="org";
		Map<String, Object> allOrders=new HashMap<String, Object>(16);
		if (person.equals(identity)) {
			allOrders = service.getAllOrders(page,rows,orderStatus);
		}else if (org.equals(identity)) {
			allOrders=service.getAllOrdersOfOrg(page,rows,orderStatus);
		}else {
			allOrders.put("code", 402);
			allOrders.put("message", "参数错误");
		}
		return allOrders;
	}
	
	
	/**
	 * 根据订单号,取消订单
	 * 
	 * @param orderId 订单号,必填
	 * @param reasonCode 订单取消原因,必填
	 * @param isOrg 操作方是否是机构 true-是,false-不是
	 * 
	 * 			普通用户取消的原因:11-个人原因,课程不想上了 12-重复下单 13-对课程不满意 14-对机构不满意 15-其他原因
	 * 			机构取消的原因:21-与用户协商,取消订单 22-招生名额已满 23-课程已停课 24-其他原因
	 * @return json
	 */
	@RequestMapping("order/cancelOrder.action")
	@ResponseBody
	public Map<String, Object> cancelOrder(@RequestParam(required=true) String orderId,@RequestParam(required=true) int reasonCode,boolean isOrg){
		Map<String, Object> cancelOrder = service.cancelOrder(orderId,reasonCode,isOrg);
		//取消订单后,发送消息 (service层有多处逻辑,比较复杂.在controller层根据结果统一判断,更方便)
		Map<String, Object> orderInfo = service.getOrderDetail(orderId);
		Map<String, Object> info = (Map<String, Object>) orderInfo.get("data");
		if ((int)cancelOrder.get("code") == 600) {
			if (isOrg) {
				String reason = "";
				switch (reasonCode) {
				case 21:
					reason = "与用户协商,取消订单";
					break;
				case 22:
					reason = "招生名额已满";
					break;
				case 23:
					reason = "课程已停课";
					break;
				case 24:
					reason = "其他原因";
					break;
				}
				
				WechatTemplateParam	wechatTemplateParam = new WechatTemplateParam();
				wechatTemplateParam.setTemplateIndex(1);// 微信消息模板（订单取消通知）序号：1
				wechatTemplateParam.setKeyword1(orderId);// 订单ID
				wechatTemplateParam.setKeyword2(info.get("coursename").toString());// 订单科目
				wechatTemplateParam.setKeyword3(info.get("payment_amount").toString());// 退费金额
				wechatTemplateParam.setKeyword4(reason);// 取消原因
				wechatTemplateParam.setKeyword5(DateTimeTool.getDateStr(LocalDate.now()));// 取消日期
				messageTool.sendMessage("订单取消", "您的订单:"+orderId+"被取消了.取消原因:机构取消", (String)info.get("user_id"), 2, 103, 1, wechatTemplateParam);
			} else {
				messageTool.sendMessage("订单取消", "您的订单:"+orderId+"被取消了.取消原因:学生取消", (String)info.get("org_id"), 2, 203, 2, null);
			}
		} 
		return cancelOrder;
	}
	
	
	/**
	 * 重定向,进入订单详情页
	 */
	@RequestMapping("order/orderDetail.action")
	public String redirectToOrderDetail(String orderId,HttpServletResponse response) {
		return "order/orderDetail.jsp?orderId="+orderId;
	}
	
	/**
	 * 根据订单号,获取订单详细信息
	 * 
	 * 
	 * @author fanjunguo
	 * @return
	 */
	@RequestMapping("order/getOrderDetail.action")
	@ResponseBody
	public Map<String, Object> getOrderDetail(@RequestParam(required=true) String orderId){
		Map<String, Object> json = service.getOrderDetail(orderId);
		return json;
	}
	
	/**
	 * 获取用户的所有账单信息,包括帐期、金额、是否支付等
	 * 
	 * @return json
	 */
	@RequestMapping("order/getBillOfUser.action")
	@ResponseBody
	public Map<String, Object> getBillOfUser(){
		
		return service.getBillOfUser();
	}
	
	/**
	 * 删除订单
	 * 
	 * @date 2019.6.1
	 * @param orderId 订单id
	 * @param clientType 客户端类型:1-普通用户 2-机构
	 * @return 删除结果
	 */
	@RequestMapping("order/deleteOrder.action")
	@ResponseBody
	public Map<String, Object> deleteOrder(@RequestParam(required=true)String orderId,@RequestParam(defaultValue="person")String identity){
		Map<String, Object> json = service.deleteOrder(orderId,identity);
		return json;
	}
	
	/**
	 * 如果订单金额为0,不需要走支付流程.确定报名之后直接报名成功. 此方法用于更新订单状态,变为[已支付]
	 * @param orderId 订单号.必须
	 * @return
	 */
	@RequestMapping("order/updateOrderStatus.action")
	@ResponseBody
	public JsonData updateOrderStatus(@RequestParam(required=true)String orderId) {
		
		return service.updateOrderStatus(orderId);
	}
	
	
	/**
	 * 用户付款给机构
	 * 
	 * 
	 * @param orderId 订单号
	 * @return 成功-600; 402-更新失败,订单非法
	 */
	@RequestMapping("order/PersonPayToOrg.action")
	@ResponseBody
	public JsonData PersonPayToOrg(@RequestParam(required=true)String orderId) {
		return service.PersonPayToOrg(orderId);
	}
	
}
