package cn.asxuexi.order.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

import org.apache.ibatis.annotations.Param;

import cn.asxuexi.order.entity.OrderEntity;
import cn.asxuexi.tool.JsonData;

public interface OrderDao {
	
	/**
	 * 
	 * 创建订单,保存订单信息. 数据库表:order
	 * @author fanjunguo
	 * @param order 实体类
	 */
	 int createOrder(OrderEntity order);
	
	 /**
	  * 根据套餐id,查询产品信息:价格,课程有效期等
	  * 
	  * @param packageId
	  */
	 Map<String, Object> getPackageInfo(String packageId);
	 
	 /**
		 * 根据订单号和订单状态,查询订单信息
		 * 
		 * @author fanjunguo
		 * @param orderId
		 */
	 Map<String, Object> getOrderInfo(@Param("orderId")String orderId,@Param("userId") String userId,@Param("orderStatus") int orderStatus);
	 
	 /**
	  * 更新订单信息
	  * 
	  * @param orderId 订单id
	  * @param orderStatus 目标订单状态(订单改变后的状态)
	  * @return 受影响的数据行数
	  */
	 int updateOrder(@Param("orderId")String orderId,@Param("orderStatus")int orderStatus) ;
	 
	 /**
	  * 取消订单:更新订单状态和取消原因
	  * 
	  * @param orderId
	  * @param orderStatus
	  * @param cancelReason
	  */
	 void cancelOrder(@Param("orderId")String orderId,@Param("orderStatus")int orderStatus,@Param("cancelReason")Integer cancelReason) ;

	/**
	 * 根据订单号,查询订单的状态
	 * 
	 * @param orderId
	 */
	JsonData getOrderStatus(String orderId);

	/**
	 * 个人订单中心——查询所有的订单信息
	 * 
	 * @param userId
	 * @param orderStatus 订单状态
	 * @return 所有的订单的信息
	 */
	List<OrderEntity> getAllOrders(@Param("userId")String userId, @Param("orderStatus")int orderStatus);

	/**
	 * 查询的订单支付金额
	 * 
	 * @param orderId
	 */
	double getPaymentAmount(String orderId);
	
	/**
	 * 根据订单号,查询订单详细信息
	 * 
	 * @param orderId
	 * @return 请求中的字段示例
	 * address: "中国,山东省,潍坊市,奎文区,山东省税务干部学校"
	 * cancelRules: "预订人因自身原因或因其他非因法定原因要求变更或取..."
	 * charge_type: 1
		courseBegin: 1562515200
		courseEnd: 1564502400
		courseImg: "/asxuexi_resource/system/物理.jpg"
		course_id: "course_1562334683769545"
		course_length: 2
		coursename: "物理"
		gmt_create: 1563851062713
		gmt_modified: 1563854400020
		order_id: "156385106271388383316"
		order_status: -1
		orgId: "org_1548943140432"
		orgName: "至上教育"
		org_id: "org_1548943140432"
		org_visibility: 1
		package_id: "package_1562924501663239"
		package_name: "试听课"
		payment_amount: 0.01
		person_visibility: 1
		student_id: "student_1559917481189801"
		student_name: "刘小备"
		student_tel: "15862323265"
		teacher: "姜老师"
		tel: "18254644076"
		user_id: "user_20190131215458713184"
	 */
	public Map<String, Object> getOrderDetail(String orderId);
	
	/**
	 * 单独查询订单对应课程的图片
	 * 	多表联合查询,如果关联太多的表也会影响性能.由于查询订单详情方法:getOrderDetail 中没有查课程图片,所以多写一个方法,来查图片.
	 * 	以后的编程中也要注意,如果数据量大并且关联的表多,尽量分开查询数据
	 * 
	 * @version 2019.6.10
	 * @author fanjunguo
	 * @param courseId 课程ID
	 * @return 图片地址
	 */
	String getCourseImg(String courseId);
	
	
	
	/**
	 * 获取用户的所有账单信息,包括帐期、金额、是否支付等
	 * 
	 */
	 List<Map<String, Object>> getBillOfUser(String userId);

	/**
	 * 机构--查询所有订单
	 * 
	 * @date 2019-5-23
	 * @param orgId 机构id
	 * @param orderStatus 订单状态
	 */
	 List<Map<String, Object>> getAllOrdersOfOrg(@Param("orgId")String orgId, @Param("orderStatus") int orderStatus);

	 /**
	 * 删除订单
	 * 
	 * @date 2019.6.1
	 * @param orderId 订单id
	 * @param identity 客户端类型:person-普通用户 org-机构
	 * @return 删除结果
	 */
	 int deleteOrder(@Param("orderId")String orderId, @Param("identity")String identity);
	 
	 /**
	  * 查询订单退改规则
	  * 
	  * @param chargeType 订单收费类型
	  * @return 规则文本
	  */
	 String getCancelRules(int chargeType);
	 
	 
	 /**
	  * 根据传入的时间节点,取消所有超时未支付的订单
	  * 
	  * @param date 订单有效期
	  */
	 void cancelOrderAuto(Date date);
	 
	 
		/**
		 * 根据订单号,确定此订单对应的机构
		 * (我认为,一个dao层应该跟数据表对应的.这个功能是对应订单表的,所以写在里这个dao层里面  by junguo.fan)
		 * 
		 * @return 机构id
		 */
	String getOrgIdByOrderId(String orderId);
	
	/**
	 * 查询订单的支付渠道
	 * 
	 * 
	 * @author fanjunguo
	 * @param orderId
	 * @return
	 */
	int getPayType(String orderId);
}
