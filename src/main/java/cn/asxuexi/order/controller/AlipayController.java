package cn.asxuexi.order.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

import cn.asxuexi.message.tool.MessageTool;
import cn.asxuexi.order.dao.OrderDao;
import cn.asxuexi.order.service.OrderService;
import cn.asxuexi.organization.service.OrgAccountService;
import cn.asxuexi.tool.AlipayConfig;
import cn.asxuexi.tool.JsonData;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.Token_JWT;

/**
 * 支付宝支付功能相关controller,主要是调用支付宝的接口和接受支付宝的回调
 *
 * @author fanjunguo
 * @version 2019年4月26日 下午9:35:50
 */
@Controller
public class AlipayController {
	
	private static Logger logger=LoggerFactory.getLogger(AlipayController.class);
	
	//实例化客户端
	private static AlipayClient alipayClient=new DefaultAlipayClient(AlipayConfig.serverUrl, AlipayConfig.appId, AlipayConfig.privateKey, 
			AlipayConfig.format,AlipayConfig.charset, AlipayConfig.zfb_PublicKey, AlipayConfig.signType);
	
	@Resource
	private OrderService orderService;
	@Resource
	private OrderDao orderDao;
	@Resource
	private OrgAccountService orgAccountService;
	
	@Autowired
	private MessageTool messageTool;
	
	
	/**
	 * 发起支付宝支付
	 * 
	 * @author fanjunguo
	 * @param orderId
	 * @return 如果成功,会返回html代码,其为能自动跳转支付宝付款页面的<form>; 如果失败,返回null
	 */
	@RequestMapping("order/alipay.action")
	@ResponseBody
	public String alipay(String orderId) {
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		
		Map<String, Object> orderInfo = orderService.getOrderInfo(orderId);
		Map<String, Object> bizContent=new HashMap<>();
		if ((int)orderInfo.get("code")==600) {
			Object object = orderInfo.get("data");
			//如果在此之前,订单已经取消,这里会null,需要判断一下
			if (null!=object) {
				Map<String, Object> data = JSONObject.parseObject(JSONObject.toJSONString(object),new TypeReference<Map<String, Object>>(){});
				//设置业务参数:
				bizContent.put("out_trade_no", data.get("order_id")); //订单号,必填
				bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");  //销售产品码，与支付宝签约的产品码名称。 注：目前仅支持FAST_INSTANT_TRADE_PAY
				bizContent.put("total_amount",(BigDecimal)data.get("payment_amount")); //订单金额,必填
				//todo:subject传中文的问题没有解决. 可能还是中文乱码问题.需要排查一下
				bizContent.put("subject","asxuexi");
				bizContent.put("timeout_express","30m");//设置订单超时时间30min
				try {
					String userId = (String)Token_JWT.verifyToken().get("userId");
					bizContent.put("passback_params", URLEncoder.encode(userId, "utf-8")); //设置回传参数,回传用户id
				} catch (UnsupportedEncodingException e) {
					logger.error("转码错误",e);
				}
			}
		}
		
		//将map:bizContent 转成字符串
		String bizContent_Str = JSONObject.toJSONString(bizContent);
		alipayRequest.setBizContent(bizContent_Str);		
		
		String form=null;
		try {
			form = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return form;
	}	
	
	/**
	 * 移动端支付宝支付:移动端逻辑
	 * 
	 */
	@RequestMapping("alipay/app.action")
	@ResponseBody
	public Map<String, Object> appAlipay(@RequestParam(required=true)String orderId) {
		Map<String, Object> json=new HashMap<String, Object>();
		int code=400;
		String message="failure";
		String userId = (String)Token_JWT.verifyToken().get("userId");
		//后台获取订单信息,不能依赖前台传入的数据
		Map<String, Object> orderInfo = orderService.getOrderInfo(orderId);
		if ((int)orderInfo.get("code")==600) {
			Object object = orderInfo.get("data");
			Map<String, Object> data = JSONObject.parseObject(JSONObject.toJSONString(object),new TypeReference<Map<String, Object>>(){});
			//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
			AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
			request.setNotifyUrl(AlipayConfig.notify_url);
			//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setSubject((String)data.get("coursename"));
			model.setBody((String)data.get("coursename")+" "+(String)data.get("package_name"));
			model.setOutTradeNo(orderId);
			model.setTimeoutExpress("30m");
			model.setTotalAmount(((BigDecimal)data.get("payment_amount")).toString()); 
			model.setProductCode("QUICK_MSECURITY_PAY");
			try {
				model.setPassbackParams(URLEncoder.encode(userId, "utf-8"));
				request.setBizModel(model);
				AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
				String body = response.getBody();
				json.put("data", body);
			} catch (Exception e) {
				code=401;
				message="exception";
				logger.error("app支付发起失败,出现异常,user:"+userId,e);
			}
		}
		json.put("code", code);
		json.put("message", message);
		return json;
		
	}
	
	/**
	 * h5端支付宝支付接口
	 * 
	 * @param orderId 订单号
	 * @param returnUrl 回跳页面地址
	 * @return json.如果请求成功,data是支付宝返回的页面请求表单form,该form可以直接跳转到支付宝支付的h5页面
	 */
	@RequestMapping("alipay/h5.action")
	@ResponseBody
	public JsonData h5Alipay(@RequestParam(required=true)String orderId,String returnUrl) {
		JsonData jd=null;
		
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
		if (null == returnUrl) {
			returnUrl=AlipayConfig.return_url;
		}
		alipayRequest.setReturnUrl(returnUrl); 
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		AlipayTradeWapPayModel h5Modal=new AlipayTradeWapPayModel();
		h5Modal.setOutTradeNo(orderId);
		//根据订单号,查询订单信息; 并验证订单状态
		Map<String, Object> orderInfo = orderService.getOrderInfo(orderId);
		if ((int)orderInfo.get("code")==600) {
			Object object = orderInfo.get("data");
			Map<String, Object> data = JSONObject.parseObject(JSONObject.toJSONString(object),new TypeReference<Map<String, Object>>(){});
			h5Modal.setTotalAmount(((BigDecimal)data.get("payment_amount")).toString()); 
			//商品名称
			h5Modal.setSubject((String)data.get("coursename"));
			h5Modal.setBody((String)data.get("coursename")+" "+(String)data.get("package_name"));
			//固定值 QUICK_WAP_WAY
			h5Modal.setProductCode("QUICK_WAP_WAY");
			h5Modal.setTimeoutExpress("30m");
			String userId = (String)Token_JWT.verifyToken().get("userId");
			try {
				h5Modal.setPassbackParams(URLEncoder.encode(userId, "utf-8"));
			} catch (UnsupportedEncodingException e1) {
				logger.error("h5支付,参数encode转码异常",e1);
			}
			alipayRequest.setBizModel(h5Modal);
			String form="";
			try {
				form = alipayClient.pageExecute(alipayRequest).getBody();
				jd=JsonData.success(form);
			} catch (AlipayApiException e) {
				String msg="h5调用支付宝支付接口,出现异常";
				logger.error(msg,e);
				jd=JsonData.exception(msg);
			}
		} else {
			jd=JsonData.error("调用支付宝接口失败");
		}
		
		return jd;
	}
	
	
	
	/**
	 * 支付宝异步通知回调接口:
	 * 	支付通知的参数说明详见:https://docs.open.alipay.com/270/105902#s7. 
	 * 		以下重要信息存到订单中
	 * 			trade_no 交易凭证号
	 * 			buyer_id 买家支付宝账号id
	 * 		
	 * 		其他重要字段
	 * 			trade_status 交易状态,只有当为“TRADE_SUCCESS”或者“TRADE_FINISHED”时,才认为支付成功
	 * 
	 * @return "success"或“failure”. 验签成功返回success
	 */
	@RequestMapping("alipayNotify.do")
	@ResponseBody
	public String alipayNotify(HttpServletRequest request) {
		Map<String, String> paramMap = getParamMap(request);
		String returnStr="failure";
		try {
			boolean signResult= AlipaySignature.rsaCheckV1(paramMap, AlipayConfig.zfb_PublicKey, AlipayConfig.charset, AlipayConfig.signType);
			if (signResult) {
				returnStr="success";
				/*验证支付宝返回的订单信息 ,如果验证通过,修改订单状态:
				 * 验证规则:
				 * 	1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号
				 * 	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）
				 * 	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
				 * 	4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
				 * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
				 * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
				 * 
				 * */
				String orderId_alipay = paramMap.get("out_trade_no");
				String userId = URLDecoder.decode(paramMap.get("passback_params"), "utf-8");
				
				Map<String, Object> orderInfo = orderDao.getOrderInfo(orderId_alipay,userId,10); //10表示待付款状态
				//判断返回的订单号是否正确,如果正确再验证其他信息是否匹配.
				if (orderInfo!=null) {
					String paymentAmount = paramMap.get("total_amount");
					boolean amount = paymentAmount.equals(orderInfo.get("payment_amount").toString());
					boolean appId = paramMap.get("app_id").equals(AlipayConfig.appId);
					String tradeStatus = paramMap.get("trade_status"); //订单状态
					boolean trade=tradeStatus.equals("TRADE_FINISHED")||tradeStatus.equals("TRADE_SUCCESS");
					logger.info(amount+"//"+tradeStatus);
					logger.info(paramMap.toString());
					if (amount&&appId&&trade) {
						orderDao.updateOrder(orderId_alipay, 11);   //11-支付成功
						//更新机构账户信息和日志
						orgAccountService. updateAccountOfSingleField(orderId_alipay,Double.parseDouble(paymentAmount), "amount_entering", 1, 1);
						String orgId = (String) orderInfo.get("org_id");
						messageTool.sendMessage("您有新的订单", "您的课程有新的学生报名,快去看看吧", orgId, 2, 203, 2, null);
					}
				}
			}
		} catch (AlipayApiException e) {
			logger.error("支付宝支付,验签错误", e);
		} catch(Exception e) {
			logger.error("解析数据出现异常", e);
		}
		
		return returnStr;
	}
	
	
	
	/**
	 * 用户确认付款后,同步通知接口
	 * 
	 * @author fanjunguo
	 * @param request
	 * @return 支付结果页面名字的字符串
	 */
	@RequestMapping("alipayReturn.do")
	public String alipayReturn(HttpServletRequest request) {
		String orderId = getParamMap(request).get("out_trade_no");
		return "redirect:pagers/order/paymentResult.jsp?order_id="+orderId;
	}
	
	
	
	/**
	 * 
	 * 工具方法:解析支付宝同步和异步通知回调中返回的数据
	 * 
	 * @author fanjunguo
	 * @param request
	 * @return 支付宝返回的参数
	 */
	private Map<String, String> getParamMap(HttpServletRequest request){
		Map<String, String> params=new HashMap<>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (Iterator<String> iter = parameterMap.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) parameterMap.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]
		                : valueStr + values[i] + ",";
		    }
		    //乱码解决，这段代码在出现乱码时使用
		    //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		    params.put(name, valueStr);
		}
		return params;
	}
	
	/**
	 * 支付宝退款
	 * 
	 * @author fanjunguo
	 * @param orderId 需要退款的订单号
	 * @param refundAmount 退款金额
	 * @param isPartialRefund 是否为部分退款 true-部分退款
	 * 
	 * @return 如果退款成功,code=600;请求失败400;
	 */
	
	@RequestMapping("alipay/refund.action")
	@ResponseBody
	public static JsonData refund(String orderId,double refundAmount){
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		Map<String, Object> bizContent=new HashMap<>();
		bizContent.put("out_trade_no", orderId);
		bizContent.put("refund_amount", refundAmount);
		String outRequestNo=orderId+RandomTool.getRandomCode(3);
		bizContent.put("out_request_no", outRequestNo);
		String jsonString = JSONObject.toJSONString(bizContent);
		request.setBizContent(jsonString);
		Map<String, String> resultMap=null;
		//将支付宝返回的结果转成自己约定的json格式
		JsonData json=JsonData.error();
		try {
			AlipayTradeRefundResponse response = alipayClient.execute(request);
			String body = response.getBody();  //获取退款返回信息的方法
			//支付宝返回结果:如果退款成功,code=“10000”;退款失败返回其他code码
			resultMap = JSONObject.parseObject(body,new TypeReference<Map<String, String>>() {});
			if (response.isSuccess()) {
				String string = resultMap.get("alipay_trade_refund_response");
				Map<String, String> responseData = JSONObject.parseObject(string, new TypeReference<Map<String, String>>() {});
				if ("10000".equals(responseData.get("code"))) {
					json=JsonData.success();
				}
				logger.debug(orderId+"支付宝退款接口调用成功");
			}else {
				logger.debug(orderId+"支付宝退款接口调用失败");
			}
		} catch (AlipayApiException e) {
			json=JsonData.exception();
			logger.error("支付宝退款异常",e);
		}
		
		return json;
	} 
	
	/**
	 * 交易关闭接口:在用户订单未支付的情况下,可以调用此接口取消
	 * 
	 * @return 如果发生异常,返回null; 如果接口调用成功,会返回json格式的结果:
	 * 		{
			    "alipay_trade_close_response": {
			        "code": "10000",
			        "msg": "Success",
			        "trade_no": "2013112111001004500000675971",
			        "out_trade_no": "YX_001"
			    },
			    "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
			}
	 */
	public static Map<String, String> tradeClose(String orderId) {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		Map<String, Object> bizContent=new HashMap<>();
		bizContent.put("out_trade_no", orderId);
		request.setBizContent(JSONObject.toJSONString(bizContent));
		try {
			AlipayTradeCloseResponse response=alipayClient.execute(request);
			String bodyStr = response.getBody();
			return JSONObject.parseObject(bodyStr, new TypeReference<Map<String, String>>() {});
		} catch (AlipayApiException e) {
			logger.error("调用支付宝交易取消接口失败",e);
		}
		return null;
	}

	
}
