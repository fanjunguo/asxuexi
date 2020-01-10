package cn.asxuexi.all.session;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.asxuexi.tool.GetAddressByIp_Baidu;

@Service
public class LocationServiceImpl implements LocationService {
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Map<String, Object> ipLocation(String ip) {
		Map<String, Object> addresses=null;
		try {
			addresses = GetAddressByIp_Baidu.getAddresses(ip, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("定位出现异常", e);
		}
		return addresses;
		
	}
}
