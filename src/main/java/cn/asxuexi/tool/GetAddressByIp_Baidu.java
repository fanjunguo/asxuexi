package cn.asxuexi.tool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.asxuexi.android.service.AppLocationService;
/**
 * @desciption 工具类：根据传入的ip，调用百度接口，获取地理位置信息
 * @author fanjunguo
 *
 */
@Component
public class GetAddressByIp_Baidu {
	
	
	private static AppLocationService service;
	
	@Autowired
	public void setService(AppLocationService service) {
		GetAddressByIp_Baidu.service=service;
	}
	
	public static Map<String, Object> getAddresses(String ip, String encode)
			throws UnsupportedEncodingException {
		String url = "http://api.map.baidu.com/location/ip?ip="+ip+"&ak=P9yAZyPp52QY8L2Hfk3gm655P4OnDjRz&coor=bd09ll";
		String returnStr = getResult(url, ip, encode);
		
		Map<String, Object> location=new HashMap<String, Object>(16);
		String cityid="370500";
		String cityname="东营";
		
		if (returnStr != null) {
			// 将返回的数据数据解码
			returnStr = decodeUnicode(returnStr);
			JSONObject parseObject = JSONObject.parseObject(returnStr);
			int statusCode=(int) parseObject.get("status"); //获取状态码,0表示请求成功,其他表示请求失败
			if (statusCode==0) {
				cityname = ((JSONObject)((JSONObject)parseObject.get("content")).get("address_detail")).get("city").toString();
				cityid=service.getCityInfo(cityname).get("id").toString();
				cityname=service.getCityInfo(cityname).get("shortname").toString();
			}
			
		}
		location.put("cityid", cityid);
		location.put("cityname", cityname);
		return location;
	}

	private static String getResult(String urlStr, String ip, String encode) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例
			connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod("POST");// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
			connection.connect();// 打开连接端口
			DataOutputStream out = new DataOutputStream(connection
					.getOutputStream());// 打开输出流往对端服务器写数据
			out.writeBytes(ip);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			out.flush();// 刷新
			out.close();// 关闭输出流
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), encode));// 往对端写完数据对端服务器返回数据
			// ,以BufferedReader流来读取
			StringBuffer buffer = new StringBuffer();
			
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}
	private static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}
}

