package cn.asxuexi.orgInfo.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.asxuexi.message.tool.MessageTool;
import cn.asxuexi.orgInfo.dao.OrgInfoDao;
import cn.asxuexi.orgInfo.entity.OrgInfo;
import cn.asxuexi.tool.FileTool;
import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.JsonData;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.RedisTool;

@Service
public class OrgInfoServiceImpl implements OrgInfoService {
	@Resource
	private OrgInfoDao orgInfoDao;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private MessageTool messageTool;

	private String asxuexi_resource = "../asxuexi_resource/";
	private String validate = "/validate/";

	@Resource
	private RedisTool redis;
	
	/**
	 * 获取机构(主校)信息.如果返回为null,说明没有开通机构
	 */
	@Override
	public Map<String, Object> getOrgInformation() {
		Map<String, Object> orgInformation = null;
		String orgId = GetOrgIdFromRedis.getOrgId();
		if (orgId != null) {
			orgInformation=this.getOrgInformation(orgId);
		}
		return orgInformation;
	}

	/**
	 * 根据机构id,查询机构信息
	 * 
	 * @param orgId 机构id
	 * @return 返回map格式的机构信息数据
	 */
	@Override
	public Map<String, Object> getOrgInformation(String orgId) {
		Map<String, Object> orgInformation = orgInfoDao.getOrgInformation(orgId);
		// 添加logo
		Map<String, Object> logo = orgInfoDao.getLogo(orgId);
		if (logo != null) {
			Object logoName = logo.get("name");
			orgInformation.put("name", logoName);
		}
		// 解析城市
		String region = (String) orgInformation.get("MergerName");
		String province = "";
		String city = "";
		String county = "";
		int count = 0;// 计数器
		String regex = ",";
		Matcher matcher = Pattern.compile(regex).matcher(region);
		while (matcher.find()) {
			count++;
		}
		// 去除字符串前三个
		region = region.substring(3, region.length());
		// 代表直辖市
		if (count == 2) {
			int indexOf = region.indexOf(",");
			province = region.substring(0, indexOf);
			city = province;
			county = region.substring(indexOf + 1, region.length());
		} else {
			// 省市
			int indexOf = region.indexOf(",");
			province = region.substring(0, indexOf);
			region = region.substring(indexOf + 1, region.length());
			indexOf = region.indexOf(",");
			city = region.substring(0, indexOf);
			county = region.substring(indexOf + 1, region.length());
		}
		// 字符串拼接 合成父id
		String regionId = (String) orgInformation.get("localid");
		String cityId = regionId.substring(0, regionId.length() - 2) + "00";
		String provinceId = regionId.substring(0, regionId.length() - 4) + "0000";
		orgInformation.put("province", province);
		orgInformation.put("provinceId", provinceId);
		orgInformation.put("city", city);
		orgInformation.put("cityId", cityId);
		orgInformation.put("county", county);
		orgInformation.put("countyId", regionId);
		return orgInformation;
	}
	
	@Override
	public List<Map<String, Object>> getAreas(String parentId) {
		List<Map<String, Object>> areas = orgInfoDao.listAreas(parentId);
		return areas;
	}

	@Override
	public List<Object> editProvince(String province, String city, String county) {
		List<Object> arrayList = new ArrayList<>(8);
		List<Map<String, Object>> cityInfo = orgInfoDao.listAreasName(province, "1");
		List<Map<String, Object>> countyInfo = null;
		for (Map<String, Object> map : cityInfo) {
			if (map.get("name").equals(city)) {
				countyInfo = orgInfoDao.listAreas((String) map.get("id"));
			}
		}
		arrayList.add(cityInfo);
		arrayList.add(countyInfo);
		return arrayList;
	}

	@Override
	public JsonData editOrg(String file, OrgInfo orgInfo, String LogoImg) {
		JsonData json=null;
		String orgId = GetOrgIdFromRedis.getOrgId();
		if (orgId != null) {
			LocalDateTime now = LocalDateTime.now();
			int updateLogo = 0;
			int updateOrgInfo =0;
			try {
				//如果没有上传头像
				if ("".equals(file) || file == null) {
					
				}else if ("".equals(LogoImg) || LogoImg == null) {
					String randomId = RandomTool.randomId("logo");
					updateLogo = orgInfoDao.insertLogo(orgId, randomId, file, now);
				} else {
					updateLogo = orgInfoDao.updateLogo(file, orgId, now);
				}
				
				updateOrgInfo = orgInfoDao.updateOrgInfo(orgId, orgInfo, now);
			} catch (Exception e) {
				e.printStackTrace();
				json=JsonData.exception("机构信息保存到数据库时发生异常");
			}
			if (updateOrgInfo != 0 || updateLogo != 0) {
				json=JsonData.success();
			} else {
				json=JsonData.error();
			}
		}
		return json;
	}

	@Override
	public Map<String, Object> getOrgName(String orgId) {
		Map<String, Object> orgName = new HashMap<String, Object>();
		try {
			orgName = orgInfoDao.getOrgName(orgId);
			orgName.put("code", 200);
		} catch (Exception e) {
			orgName.put("code", 201);
		}
		return orgName;
	}

	@Override
	public Map<String, Object> getIdentityAuthenticationStatus() {
		String orgId = GetOrgIdFromRedis.getOrgId();
		Map<String, Object> status = new HashMap<String, Object>();
		if (null != orgId) {
			status = orgInfoDao.getIdentityAuthenticationStatus(orgId);
		}
		return status;
	}

	@Override
	public Map<String, Object> uploadIdentityPhoto(MultipartFile file,String key) {
		String photoName;
		if (null!=key && key.trim().length()>0) {
			photoName=key+".png";
		} else {
			photoName= file.getOriginalFilename();
		}
		
		boolean saveFile = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fileName", photoName);
		String orgId = GetOrgIdFromRedis.getOrgId();
		if (orgId != null) {
			String rootPath = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + orgId
					+ validate;
			saveFile = FileTool.saveFile(rootPath, photoName, file);
		}
		map.put("flag", saveFile);
		return map;
	}

	@Override
	public Map<String, Object> insertIdentityInfo(String name, String idNumber, String expiryDate) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", false);
		if (orgId != null) {
			String[] split = expiryDate.split("-");
			LocalDateTime expiryDateTime = LocalDateTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]),
					Integer.parseInt(split[2]), 0, 0);
			LocalDateTime now = LocalDateTime.now();
			int insertIdentityInfo = orgInfoDao.insertIdentityInfo(orgId, name, idNumber, expiryDateTime, now);
			if (1 == insertIdentityInfo) {
				// 插入成功后，将标志更改为true，并返回插入的数据
				map.put("flag", true);
				map.put("org_id", orgId);
				map.put("name", name);
				map.put("id_number", idNumber);
				map.put("expiry_date", Timestamp.valueOf(expiryDateTime));
				map.put("gmt_create", Timestamp.valueOf(now));
				map.put("status", 0);
				map.put("front_status", 0);
				map.put("back_status", 0);
				map.put("person_photo_status", 0);
				// 通知员工进行审核
				List<String> staffIdList = orgInfoDao.getStaffIdList(orgId,"0802");
				String content = "编号为["+orgId+"]的机构上传了新的身份证信息，请及时审核。";
				messageTool.sendMessageToList("身份证审核提醒", content, staffIdList, 1, 300, 3, null);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> updateIdentityInfo(String name, String idNumber, String expiryDate, String gmtKey,
			String updateKey) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", false);
		if (orgId != null) {
			String[] split = expiryDate.split("-");
			LocalDateTime expiryDateTime = LocalDateTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]),
					Integer.parseInt(split[2]), 0, 0);
			LocalDateTime now = LocalDateTime.now();
			int updateIdentityInfo = orgInfoDao.updateIdentityInfo(orgId, name, idNumber, expiryDateTime, now, gmtKey,
					updateKey);
			if (1 == updateIdentityInfo) {
				map = orgInfoDao.getIdentityAuthenticationStatus(orgId);
				map.put("flag", true);
				if (updateKey != null) {
					// 通知员工进行审核
					List<String> staffIdList = orgInfoDao.getStaffIdList(orgId,"0802");
					String content = "编号为["+orgId+"]的机构上传了新的身份证信息，请及时审核。";
					messageTool.sendMessageToList("身份证审核提醒", content, staffIdList, 1, 300, 3, null);
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getLicenceAuthenticationStatus() {
		String orgId = GetOrgIdFromRedis.getOrgId();
		Map<String, Object> status = new HashMap<String, Object>();
		if (null != orgId) {
			status = orgInfoDao.getLicenceAuthenticationStatus(orgId);
		}
		return status;
	}

	@Override
	public Map<String, Object> insertLicenceInfo(String licenceNumber, String companyName, String companyAddress,
			String legalRepresentative) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", false);
		if (orgId != null) {
			LocalDateTime now = LocalDateTime.now();
			int insertLicenceInfo = orgInfoDao.insertLicenceInfo(orgId, licenceNumber, companyName, companyAddress,
					legalRepresentative, now);
			if (1 == insertLicenceInfo) {
				// 插入成功后，将标志更改为true，并返回插入的数据
				map.put("flag", true);
				map.put("org_id", orgId);
				map.put("licence_number", licenceNumber);
				map.put("company_name", companyName);
				map.put("company_address", companyAddress);
				map.put("legal_representative", legalRepresentative);
				map.put("gmt_create", Timestamp.valueOf(now));
				map.put("status", 0);
				// 通知员工进行审核
				List<String> staffIdList = orgInfoDao.getStaffIdList(orgId,"0803");
				String content = "编号为["+orgId+"]的机构上传了新的营业执照信息，请及时审核。";
				messageTool.sendMessageToList("营业执照审核提醒", content, staffIdList, 1, 300, 3, null);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> updateLicenceInfo(String licenceNumber, String companyName, String companyAddress,
			String legalRepresentative, String gmtKey) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", false);
		if (orgId != null) {
			LocalDateTime now = LocalDateTime.now();
			int updateLicenceInfo = orgInfoDao.updateLicenceInfo(licenceNumber, companyName, companyAddress,
					legalRepresentative, gmtKey, now, orgId);
			if (1 == updateLicenceInfo) {
				map = orgInfoDao.getLicenceAuthenticationStatus(orgId);
				map.put("flag", true);
				if (gmtKey.equals("gmt_create")) {
					// 通知员工进行审核
					List<String> staffIdList = orgInfoDao.getStaffIdList(orgId,"0803");
					String content = "编号为["+orgId+"]的机构上传了新的营业执照信息，请及时审核。";
					messageTool.sendMessageToList("营业执照审核提醒", content, staffIdList, 1, 300, 3, null);
				}
			}
		}
		return map;
	}

}
