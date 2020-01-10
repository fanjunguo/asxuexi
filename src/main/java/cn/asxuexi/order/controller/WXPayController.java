package cn.asxuexi.order.controller;
/**
 * 微信支付
 *
 * @author fanjunguo
 * @version 2019年8月20日 上午11:22:58
 */

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.asxuexi.order.service.WXPayService;
import cn.asxuexi.order.tool.WXPayUtil;
import cn.asxuexi.tool.JsonData;
@RestController
public class WXPayController {
	@Resource
	private WXPayService service;
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());

	/**
	 * 微信统一下单接口:先调用此接口向微信下单.返回支付凭证之后,让用户确认支付
	 * 
	 * 
	 * @author fanjunguo
	 * @param orderId 待支付的订单号
	 * @param ip 终端ip地址
	 * @param openId 用户的微信标识
	 * @return 返回数据格式如下,具体的返回规则参见微信下单api:
	 * {
		    "code": 600,
		    "data": {
		        "nonce_str": "wgYmE6iEzhsxrGR3",
		        "appid": "wx6de372d97ccf6fb8",
		        "sign": "8AF00407C0FDB7FBEE26DB9E03FF7FB8",
		        "trade_type": "JSAPI",
		        "return_msg": "OK",
		        "result_code": "SUCCESS",
		        "mch_id": "1548153141",
		        "return_code": "SUCCESS",
		        "prepay_id": "wx271641583045957bea9ba5741331162400"
		        "timeStamp": "15012050151515"
		        "paySign":"2098hgqp9hg9q8wg9qhg9qwg"
		    },
		    "message": "success"
		}
	 */
	@RequestMapping("WXPay/WXPrePay.action")
	public JsonData WXPrePay(@RequestParam String orderId,@RequestParam String ip,@RequestParam String openId) {
		String regex="\\d{2,3}.\\d{2,3}.\\d{2,3}.\\d{2,3}";
		boolean isMatched = Pattern.matches(regex, ip);
		if (isMatched) {
			return service.WXPrePay(orderId,ip,openId);
		} else {
			return JsonData.illegalParam();
		}
	}
	
	
	/**
	 * 接受微信异步通知支付结果,并应答
	 * 
	 * @author fanjunguo
	 */
	@RequestMapping("WXPay/WXPayNotify.do")
	public String notify(HttpServletRequest request) {
		try {
			service.notify(request);
		} catch (Exception e) {
			logger.error("微信支付结果回调接口异常:"+e.getMessage(),e);
		}
		//收到微信通知之后,给微信一个确认答复
		Map<String, String> returnXml=new HashMap<String, String>(16);
		returnXml.put("return_code", "SUCCESS");
		returnXml.put("return_msg", "OK");
		try {
			return WXPayUtil.mapToXml(returnXml);
		} catch (Exception e) {
			logger.error("微信回调接口,controller层map转xml异常",e);
			return null;
		}
	}
	
	

}
