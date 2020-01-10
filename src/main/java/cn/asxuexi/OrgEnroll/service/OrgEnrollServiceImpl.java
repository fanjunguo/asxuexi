package cn.asxuexi.OrgEnroll.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.asxuexi.OrgEnroll.dao.OrgEnrollDao;
import cn.asxuexi.organization.dao.OrgAccountDao;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.RedisTool;
import cn.asxuexi.tool.Token_JWT;

@Repository
public class OrgEnrollServiceImpl implements OrgEnrollService {

	@Resource
	private OrgEnrollDao orgEnrollDao;
	@Resource
	private OrgAccountDao orgAccountDao;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private RedisTool redis;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private String asxuexi_resource = "../asxuexi_resource/";

	/**
	 * @作用 获取所有省份 、当前城市的省份下的城市，当前城市下的县区
	 */
	@Override
	public Map<String, Object> address() {
		String cityId = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("cityid".equals(cookie.getName())) {
				cityId = cookie.getValue();
			}
		}
		if (cityId == null) {
			cityId = "370500";
		}
		Map<String, Object> hashMap = new HashMap<String, Object>(16);
		List<Map<String, Object>> listCity = orgEnrollDao.listCity(cityId);
		Map<String, Object> provinceId = orgEnrollDao.getProvinceId(cityId);
		List<Map<String, Object>> listCounty = orgEnrollDao.listAddress(cityId);
		hashMap.put("listCity", listCity);
		hashMap.put("listCounty", listCounty);
		hashMap.put("provinceId", provinceId.get("ParentId"));
		hashMap.put("cityId", cityId);
		return hashMap;
	}

	/***
	 * @param parentId
	 *            父级id
	 * @作用 获取下一级城市
	 */
	@Override
	public List<Map<String, Object>> listAddress(String parentId) {
		List<Map<String, Object>> listCounty = orgEnrollDao.listAddress(parentId);
		return listCounty;
	}

	/**
	 * @param orgName
	 *            机构名称
	 * @param orgHead
	 *            机构负责人
	 * @param orgTel
	 *            机构电话
	 * @param loaction
	 *            机构所在县区id
	 * @作用 保存信息
	 */
	@Override
	public int insertOrgInfo(String orgName, String orgHead, String orgTel, String loacltion) {
		String userId = (String) Token_JWT.verifyToken().get("userId");
		int insertResult = 0;
		if (userId != null) {
			String orgId =RandomTool.randomId("org");
			LocalDateTime now = LocalDateTime.now();
			insertResult = orgEnrollDao.insertOrgInfo(orgId, orgName, orgHead, orgTel, loacltion, userId, now);
			if (insertResult == 1) {
				orgEnrollDao.insertUserOrgRelationship(userId, orgId, true,LocalDateTime.now());
				// 创建机构账户信息
				orgAccountDao.createOrgAccount(orgId);
				// 在logo表中创建该机构记录
				String logoId = RandomTool.randomId("logo");
				// 默认logo为爱上学习网logo
				String logoName = "img/logo.png";
				orgEnrollDao.insertOrgLogo(logoId, orgId, logoName, now);
				// 获取服务器物理地址
				String rootPath = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + orgId;
				String imgPath = rootPath + "/img";
				String imgInvalidPath = rootPath + "/img/invalid";
				String videoPath = rootPath + "/video";
				String videoInvalidPath = rootPath + "/video/invalid";
				String validPath = rootPath + "/validate";
				// 创建文件夹
				boolean imgDirIsMade = new File(imgPath).mkdirs();
				boolean imgInvalidDirIsMade = new File(imgInvalidPath).mkdirs();
				boolean videoDirIsMade = new File(videoPath).mkdirs();
				boolean videoInvalidDirIsMade = new File(videoInvalidPath).mkdirs();
				boolean validateDirIsMade = new File(validPath).mkdirs();
				// 存入缓存
				Map<String, Object> map = new HashMap<>();
				map.put("orgId", orgId);
				redis.addHash(userId, map, 24L, TimeUnit.HOURS);
				if (!imgDirIsMade && !videoDirIsMade && !validateDirIsMade && !imgInvalidDirIsMade
						&& !videoInvalidDirIsMade) {
					MDC.put("userId", userId);
					logger.error("机构文件夹创建失败");

				}
			}
		}
		return insertResult;
	}
}
