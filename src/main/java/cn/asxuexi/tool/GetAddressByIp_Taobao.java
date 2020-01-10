package cn.asxuexi.tool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * @author fanjunguo
 * @version 2019年3月7日 下午4:18:25
 * @description 由于在使用过程中,经常出现接口请求超时,所以弃用.该用百度接口
 */

public class GetAddressByIp_Taobao {
	 public static  Map<String, Object> GetAddressByIp(String IP){
		  Map<String, Object> location=new HashMap<String, Object>(16);
		  try{
			   String json = getJsonContent("http://ip.taobao.com/service/getIpInfo.php?ip="+IP);
			   JSONObject obj = JSONObject.fromObject(json);
			   JSONObject obj2 =  (JSONObject) obj.get("data");//淘宝接口的返回格式，key就是data
			   int code = (int) obj.get("code");
			   //code=0：成功；1：失败
			   if(code==0){
				   String city1 = (String)obj2.get("city");
				   String city2 = new String(city1.getBytes("utf-8"), "utf-8");
				   location.put("cityname", city2);
				   location.put("cityid", obj2.get("city_id"));
			   }else{
				   //获取失败，默认东营
				   location.put("cityname", "东营");
				   location.put("cityid", "370500");
			   }
		  }catch(Exception e){
			  	//获取失败，默认东营
			  	location.put("cityname", "东营");
			    location.put("cityid", "370500");
		  }
		  return location;

		 }

		    private static  String getJsonContent(String urlStr){
		    	HttpURLConnection httpConn=null;
		    	try{// 获取HttpURLConnection连接对象
		            URL url = new URL(urlStr);
		            httpConn = (HttpURLConnection) url.openConnection();
		            // 设置连接属性
		            httpConn.setConnectTimeout(3000);
		            httpConn.setDoInput(true);
		            httpConn.setRequestMethod("GET");
		            // 获取相应码
		            int respCode = httpConn.getResponseCode();
		            if (respCode == 200){
		                return ConvertStreamToJson(httpConn.getInputStream());
		            }
		        }catch (Exception e){
		            e.printStackTrace();
		        }finally {
		        	if(null!=httpConn) {
		        		httpConn.disconnect();
		        	}
		        }
		        return "";
		    }
		    
		    private static String ConvertStreamToJson(InputStream inputStream){
		       StringBuilder string=new StringBuilder();
		        // ByteArrayOutputStream相当于内存输出流
		        //ByteArrayOutputStream out = new ByteArrayOutputStream();
		        byte[] buffer = new byte[1024];
		        int len;
		        try {
					while((len=inputStream.read(buffer))!=-1) {
						//out.write(buffer, 0, len);
						
						string.append(new String(buffer,0,len));
					}
					// 将内存流转换为字符串
		            //jsonStr = new String(out.toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				}
		        return string.toString();
		    }
}
