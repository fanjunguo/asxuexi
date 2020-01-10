package cn.asxuexi.all.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.asxuexi.all.dao.HomepageBannerDao;

@Service
public class HomepageBannerServiceImpl implements HomepageBannerService {
	@Resource
	private HomepageBannerDao bannerCarousel_dao;
	@Autowired
	private HttpServletRequest request;

	/**
	 * 获取首页轮播图
	 */
	@Override
	public List<Map<String, Object>> getImg(String cityid) {
//		获取cookie中的城市id
		if (cityid == null) {
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				switch (cookie.getName()) {
				case "cityid":
					cityid = cookie.getValue();
					break;
				default:
					break;
				}
			}
		}
		if (cityid == null) {
			cityid="370500";
		}
		long currentTimeMillis = System.currentTimeMillis()/1000;
		List<Map<String, Object>> imgs = bannerCarousel_dao.getImg(cityid,currentTimeMillis);

		return imgs;
	}

}
