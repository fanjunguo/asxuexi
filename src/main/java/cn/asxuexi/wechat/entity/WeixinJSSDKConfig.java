package cn.asxuexi.wechat.entity;

public class WeixinJSSDKConfig {
	private String appId; // 公众号的唯一标识
	private String timestamp; // 生成签名的时间戳
	private String nonceStr; // 生成签名的随机串
	private String signature;// 签名

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
