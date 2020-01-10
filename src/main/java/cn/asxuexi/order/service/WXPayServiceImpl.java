package cn.asxuexi.order.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import cn.asxuexi.order.dao.OrderDao;
import cn.asxuexi.order.tool.WXPayConfig;
import cn.asxuexi.order.tool.WXPayConfig.TradeType;
import cn.asxuexi.order.tool.WXPayConstants;
import cn.asxuexi.order.tool.WXPayUtil;
import cn.asxuexi.organization.service.OrgAccountService;
import cn.asxuexi.tool.JsonData;
import cn.asxuexi.tool.RandomTool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class WXPayServiceImpl implements WXPayService {

	@Resource
	private OrderDao orderDao;
	@Resource
	private OrgAccountService accountService;
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 预下单
	 */
	@Override
	public JsonData WXPrePay(String orderId,String ip,String openId) {
		JsonData respData;
		//获取订单信息:订单号,课程名称,套餐名,价格,
		Map<String, Object> orderDetail = orderDao.getOrderDetail(orderId);
		Map<String, String> requestData = new HashMap<>(16);
		requestData.put("appid", WXPayConfig.AppID);
		requestData.put("mch_id", WXPayConfig.mch_id);
		//拼凑商品描述,要求格式:商家名称-销售商品类目	例如:腾讯-游戏
		String body=orderDetail.get("orgName")+"-"+orderDetail.get("coursename")+":"+orderDetail.get("package_name");
		requestData.put("body",body );  //商品描述
		requestData.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串
		requestData.put("out_trade_no", orderId);  
		requestData.put("total_fee", ((BigDecimal)orderDetail.get("payment_amount")).movePointRight(2).toString()); 
		requestData.put("spbill_create_ip", ip);
		requestData.put("trade_type", TradeType.JSAPI.toString());
		requestData.put("notify_url", WXPayConfig.notify_url);
		requestData.put("openid",openId);  //JSAPI支付必须传openid
		
		try {
			//生成签名
			requestData.put("sign",WXPayUtil.generateSignature(requestData, WXPayConfig.WXPay_key) );
			String dataOfXml = WXPayUtil.mapToXml(requestData);
			//尝试用okhttp请求
			MediaType mediaType=MediaType.parse("text/xml; charset=utf-8");
			OkHttpClient client=new OkHttpClient();
			Request request=new Request.Builder().url(WXPayConfig.prepay_url)
							.post(RequestBody.create(mediaType, dataOfXml)).build();
			Response response = client.newCall(request).execute();
			String respXML = response.body().string();
			//解析返回的xml数据
			respData = this.processResponseXml(respXML);
			String timeStamp=String.valueOf(System.currentTimeMillis()/1000);
			respData.put("timeStamp", timeStamp);
			Map<String, String> dataMap = JSONObject.parseObject(JSONObject.toJSONString(respData.get("data")), new TypeReference<Map<String, String>>(){});
			String prepayId = dataMap.get("prepay_id");
			String paySign = this.generateSign(timeStamp,prepayId,dataMap.get("nonce_str").toString());
			respData.put("paySign", paySign);
		} catch (Exception e) {
			String message = e.getMessage();
			respData=JsonData.exception(message);
			logger.error(message,e);

		}
		
		return respData;
	}
	
	/**
	 * 为微信中调起支付,重新生成签名
	 * 
	 * @author fanjunguo
	 * @param appId
	 * @param timeStamp
	 * @param nonceStr
	 * @param packageStr
	 * @param signType
	 * @return
	 * @throws Exception
	 */
	private String generateSign(String timeStamp,String prepayId,String nonceStr) throws Exception {
		Map<String, String> signData = new HashMap<>(16);
		signData.put("appId", WXPayConfig.AppID);
		signData.put("timeStamp", timeStamp);
		signData.put("nonceStr", nonceStr);
		signData.put("package", "prepay_id="+prepayId);
		signData.put("signType","MD5");
		return WXPayUtil.generateSignature(signData, WXPayConfig.WXPay_key);
	}
	
	
	/**
	 * 微信支付结果回调接口
	 * 1.同样的通知可能会多次,所以每次先判断订单状态,再做处理
	 * 2.验证签名、订单信息是否匹配
	 * 
	 */
	
	@Override
	@Transactional
	public void notify(HttpServletRequest request) throws Exception{
		//接口返回data中的数据;订单数据
		Map<String, Object> dataObj,orderDetail;
		try {
			InputStream inputStream = request.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer buffer=new StringBuffer();
			String line;
			while ((line=reader.readLine()) != null) {
				buffer.append(line);
			}
			JsonData resultJson = processResponseXml(buffer.toString());
			/* 根据微信的返回结果,处理订单
			 * 1.验签(解析接口返回的xml数据的时候,已经验签过了,所以不用再单独验签了)
			 * 2.验签成功之后,查询订单信息,验证订单信息是否匹配
			 */
			if ((int)resultJson.get("code") == 600) {
				Object object = resultJson.get("data");
				dataObj = JSONObject.parseObject(JSON.toJSONString(object),new TypeReference<Map<String, Object>>() {});
				String orderId = (String) dataObj.get("out_trade_no");
				orderDetail = orderDao.getOrderDetail(orderId);
				//验证订单状态
				int orderStatus = (int) orderDetail.get("order_status");
				if (orderStatus == 10) {
					//验证订单金额
					double paymentAmount=((BigDecimal)orderDetail.get("payment_amount")).doubleValue();
					double totalFee = Double.parseDouble((String) dataObj.get("total_fee"));
					if (paymentAmount*100 == totalFee) {
						orderDao.updateOrder(orderId, 11);
						//更新账户记录
						accountService.updateAccountOfSingleField(orderId, paymentAmount, "amount_entering", 1, 1);
					}
				}
			} 
		} catch (IOException e) {
			logger.error("微信回调接口,流读取出现异常:"+e.getMessage(),e);
		} 
	}
	
	
	
	
	/**
     * 处理 微信接口返回的xml数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     * @param xmlStr API返回的XML格式数据
     * @return jsonData类型数据,data是微信返回的原始数据的解析
	 */
    private JsonData processResponseXml(String xmlStr) throws Exception {
        String RETURN_CODE = "return_code";
        JsonData json;
        Map<String, String> respData = WXPayUtil.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
        	String return_code = respData.get(RETURN_CODE);
        	if (return_code.equals(WXPayConstants.FAIL)) {
        		json=JsonData.error("微信接口请求错误:"+respData.get("err_code")+"/"+respData.get("err_code_des"));
        	}
        	else if (return_code.equals(WXPayConstants.SUCCESS)) {
        		if (WXPayUtil.isSignatureValid(xmlStr, WXPayConfig.WXPay_key)) {
        			json=JsonData.success();
        		}
        		else {
        			json=JsonData.error("签名错误");
        		}
        	}
        	else {
        		json=JsonData.error("微信接口调用失败:无return_code返回");
        	}
        }
        else {
        	json=JsonData.error("微信接口调用失败:"+respData.get("return_msg"));
        }
        //无论请求是否错误,把微信返回的数据放到data中返回
        json.put("data", respData);
        return json;
    }
    
    /**
     * 微信退款
     * 
     * @param orderId 订单号
     * @param refundFee 退款金额(单位是分)
     * @return 退款申请结果
     */
    @Override
    public JsonData refund(String orderId,int refundFee) {
    	JsonData json;
    	Map<String, String> requestData = new HashMap<>(16);
		requestData.put("appid", WXPayConfig.AppID);
		requestData.put("mch_id", WXPayConfig.mch_id);
		requestData.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串
		try {
			requestData.put("sign",WXPayUtil.generateSignature(requestData, WXPayConfig.WXPay_key));  //签名
			requestData.put("out_trade_no", orderId);
			String outRequestNo=orderId+RandomTool.getRandomCode(3);
			requestData.put("out_refund_no", outRequestNo); //退款单号
			String totalFee =String.valueOf(orderDao.getPaymentAmount(orderId)*100);
			requestData.put("total_fee", totalFee); //订单金额
			requestData.put("refund_fee", String.valueOf(refundFee)); //退款金额
			String dataOfXml = WXPayUtil.mapToXml(requestData);
			String respDateOfXml = WXPayUtil.requestOnce(WXPayConfig.refund_url, dataOfXml);
			Map<String, String> respData = WXPayUtil.xmlToMap(respDateOfXml);
			json=JsonData.success(respData);
		} catch (Exception e) {
			json=JsonData.error("微信退款申请接口出现异常:"+e.getMessage());
			logger.error("微信退款申请接口出现异常:"+e.getMessage(), e);
		}

    	return json;
    }
}
