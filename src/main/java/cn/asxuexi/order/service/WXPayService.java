package cn.asxuexi.order.service;

import javax.servlet.http.HttpServletRequest;

import cn.asxuexi.tool.JsonData;

/**
 * 微信支付sevice层
 *
 * @author fanjunguo
 * @version 2019年8月21日 下午2:15:39
 */
public interface WXPayService {

	JsonData WXPrePay(String orderId,String ip,String openId);

	/**
	 * 微信支付结果回调接口
	 * @throws Exception 
	 * 
	 */
	void notify(HttpServletRequest request) throws Exception;

	/**
     * 微信退款
     * 
     * @param orderId 订单号
     * @param refundFee 退款金额(单位是分)
     * @return 退款申请结果
     */
	JsonData refund(String orderId, int refundFee);

}