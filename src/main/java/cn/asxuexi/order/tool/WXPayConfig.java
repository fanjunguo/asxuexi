package cn.asxuexi.order.tool;
/**
 *
 *
 * @author fanjunguo
 * @version 2019年8月20日 下午3:52:25
 */
public class WXPayConfig {
	/**
	 * 交易类型
	 */
	public enum TradeType{
		JSAPI,NATIVE,APP;
	}

	public static final String AppID="wx6de372d97ccf6fb8"; 
	public static final String AppSecret="31e7ecbe767271b0b8b1475ee1f754a7"; 
	public static final String mch_id="1548153141";  //微信支付商户号
	public static final String notify_url="http://www.asxuexi.cn/WXPay/WXPayNotify.do";
//	public static final String notify_url="http://fjg.ngrok2.xiaomiqiu.cn/webviews/WXPay/WXPayNotify.do";  //本机测试地址
	public static final String WXPay_key="2309ujf90ju0f29juw39guj0qw349gju"; //支付安全密钥
	public static final String cert_path="C:/wechatCert/apiclient_cert.p12"; //服务器上的证书路径
	
	
	//预下单接口地址
	public static final String prepay_url="https://api.mch.weixin.qq.com/pay/unifiedorder";  //预下单接口
	public static final String refund_url="https://api.mch.weixin.qq.com/secapi/pay/refund";  //退款申请接口
	
	
	
}
