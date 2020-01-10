package cn.asxuexi.tool;

import java.util.HashMap;

/**
 * 工具类:返回json格式的数据.
 * 格式为:{
 * 	int code
 * 	String message
 * 	Object data (不一定有)
 * }
 * 
 *
 * @author fanjunguo
 * @version 2019年6月5日 上午11:32:40
 */
public class JsonData extends HashMap<String, Object> {

	private static final long serialVersionUID = 1391223507531778956L;
	
	public JsonData() {}
	
	public JsonData(int code,String message) {
		super.put("code", code);
		super.put("message", message);
	}
	
	
	/**
	 * 根据参数,返回json格式数据
	 * 
	 * @param code
	 * @param message
	 */
	public static JsonData result(int code,String message) {
		JsonData json=new JsonData();
		json.put("code", code);
		json.put("message", message);
		return json;
	}
	
	/**
	 * 请求成功,不带data
	 */
	public static JsonData success() {
		return new JsonData(600, "success");
	}
	/**
	 * 请求成功,带data
	 */
	public static JsonData success(Object data) {
		JsonData json=new JsonData(600, "success");
		json.put("data", data);
		return json;
	}
	
	/**
	 * 返回请求失败400
	 * 
	 * @return
	 */
	public static JsonData error() {
		return new JsonData(400, "failure");
	}
	
	/**
	 * 返回请求失败400,带错误信息参数
	 * 
	 * @return
	 */
	public static JsonData error(String msg) {
		return new JsonData(400, msg);
	}
	
	/**
	 * 出现异常401
	 * 
	 * @param exceptionMessage 发生异常的信息描述.
	 * 
	 */
	public static JsonData exception(String exceptionMessage) {
		return new JsonData(401,exceptionMessage);
	}
	
	/**
	 * 发生异常,默认异常话术为:发生异常
	 * code:401
	 */
	public static JsonData exception() {
		return new JsonData(401,"发生异常");
	}
	/**
	 * 参数错误
	 * code:402
	 */
	public static JsonData illegalParam() {
		return new JsonData(402, "非法参数");
	}

}
