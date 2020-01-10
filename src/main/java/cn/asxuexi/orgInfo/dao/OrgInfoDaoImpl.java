package cn.asxuexi.orgInfo.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.asxuexi.orgInfo.entity.OrgInfo;

@Repository
public class OrgInfoDaoImpl implements OrgInfoDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查询机构信息
	 * 
	 * @param orgId
	 *            {@link String} 机构id
	 * @return List<Map<String, Object>>
	 */
	public Map<String, Object> getOrgInformation(String orgId) {
		Map<String, Object> queryForList = null;
		String sql = "SELECT "
				+ "dbo.areas.MergerName,dbo.areas.LevelType,  dbo.org.*  FROM  dbo.areas "
				+ "INNER JOIN  dbo.org ON dbo.areas.ID = dbo.org.localid where orgid=?";
		try {
			queryForList = jdbcTemplate.queryForMap(sql, new Object[] { orgId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryForList;
	}

	/**
	 * 查询logo
	 * 
	 * @param orgId
	 *            {@link String} 机构id
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getLogo(String orgId) {
		Map<String, Object> queryForMap = null;
		String sql = "select name ,org_id from Logo where org_id=?";
		try {
			queryForMap = jdbcTemplate.queryForMap(sql, new Object[] { orgId });
		} catch (Exception e) {
		}
		return queryForMap;
	}

	/**
	 * 获取地址的下一级省市
	 * 
	 * @param parentname
	 *            {@link String} id 父name
	 * @param level
	 *            父级等级
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> listAreasName(String parentname, String level) {
		String sql = "select id,name from areas where parentid in(select id from areas where name=? and LevelType=?)";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, new Object[] { parentname, level });
		return queryForList;
	}

	/**
	 * 获取地址的下一级省市
	 * 
	 * @param parentId
	 *            {@link String} id 父id
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> listAreas(String parentId) {
		String sql = "select id,name from areas where parentid=?";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, new Object[] { parentId });
		return queryForList;
	}

	/**
	 * 增加logo
	 * 
	 * @param orgId
	 *            {@link String} 机构id
	 * @param name
	 *            {@link String} 图片地址
	 * 
	 */
	public int insertLogo(String orgId, String id, String name, LocalDateTime gmt_create) {
		String sql = "insert into logo (id,name,org_id,gmt_create) values(?,?,?,?)";
		int update = jdbcTemplate.update(sql, new Object[] { id, name, orgId, gmt_create});
		return update;
	}

	/**
	 * 更改logo
	 * 
	 * @param name
	 *            {@link String} 图片名称
	 * @param orgId
	 *            {@link String} 机构id
	 * @param now
	 *            {@link LocalDateTime} 现在时间
	 * @return {@link Integer}
	 */
	public int updateLogo(String name, String orgId, LocalDateTime now) {
		String sql = "update logo set name=? ,gmt_modified=? where org_id=?";
		int update = jdbcTemplate.update(sql, new Object[] { name, now, orgId });
		return update;
	}

	/**
	 * 更改机构信息
	 * 
	 * @param orgId
	 *            机构id
	 * @param orgInfo
	 *            机构信息
	 * @return int
	 */
	public int updateOrgInfo(String orgId, OrgInfo orgInfo, LocalDateTime gmt_modified) {
		String sql = "update org set orgname=?,localid=?,address=?, head=?, tel=?, lng=?,lat=?, des=?,gmt_modified=?,room_number=? where orgId=?";
		int update = jdbcTemplate.update(sql,
				new Object[] { orgInfo.getOrgName(), orgInfo.getLocalId(), orgInfo.getOrgAddress(),
						orgInfo.getOrgLegalPerson(), orgInfo.getOrgtel(), orgInfo.getLng(), orgInfo.getLat(),
						orgInfo.getOrgDes(), gmt_modified, orgInfo.getRoomNumber(),orgId });

		return update;
	}

	/**
	 * 
	 * @author fanjunguo
	 * @description 根据机构id查询机构名称
	 * @param orgId
	 * @return json数据
	 */
	@Override
	public Map<String, Object> getOrgName(String orgId) {
		String sql = "select orgname from org where orgid=?";
		Map<String, Object> orgName = jdbcTemplate.queryForMap(sql, new Object[] { orgId });
		return orgName;
	}

	@Override
	public Map<String, Object> getIdentityAuthenticationStatus(String orgId) {
		String sql = "SELECT * FROM identity_authentication WHERE org_id=?";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = jdbcTemplate.queryForMap(sql, new Object[] { orgId });
		} catch (EmptyResultDataAccessException e) {
			// 没有查询到记录说明，机构没有进行身份证认证；无需处理该异常
		}
		return map;
	}

	@Override
	public int insertIdentityInfo(String orgId, String name, String idNumber, LocalDateTime expiryDateTime,
			LocalDateTime gmtCreate) {
		String delSql = "DELETE FROM identity_authentication WHERE org_id=?";
		jdbcTemplate.update(delSql, orgId);
		String sql = "INSERT INTO identity_authentication (org_id,name,id_number,expiry_date,gmt_create) VALUES (?,?,?,?,?)";
		int update = jdbcTemplate.update(sql, new Object[] { orgId, name, idNumber, expiryDateTime, gmtCreate });
		return update;
	}

	@Override
	public int updateIdentityInfo(String orgId, String name, String idNumber, LocalDateTime expiryDateTime,
			LocalDateTime gmtTime, String gmtKey, String updateKey) {
		StringBuffer buffer = new StringBuffer("UPDATE identity_authentication set name=?,id_number=?,expiry_date=?,");
		List<Object> list = new ArrayList<Object>();
		list.add(name);
		list.add(idNumber);
		list.add(expiryDateTime);
		if (updateKey != null) {
			buffer.append("status=?,");
			list.add(0);
			if (updateKey.contains("front")) {
				buffer.append("front_status=?,");
				list.add(0);
			}
			if (updateKey.contains("back")) {
				buffer.append("back_status=?,");
				list.add(0);
			}
			if (updateKey.contains("person")) {
				buffer.append("person_photo_status=?,");
				list.add(0);
			}
		}
		buffer.append(gmtKey + "=? WHERE org_id=?");
		list.add(gmtTime);
		list.add(orgId);
		String sql = buffer.toString();
		Object[] args = list.toArray();
		int update = jdbcTemplate.update(sql, args);
		return update;
	}

	@Override
	public Map<String, Object> getLicenceAuthenticationStatus(String orgId) {
		String sql = "SELECT * FROM licence_authentication WHERE org_id=?";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = jdbcTemplate.queryForMap(sql, new Object[] { orgId });
		} catch (EmptyResultDataAccessException e) {
			// 没有查询到记录说明，机构没有进行营业执照认证；无需处理该异常
		}
		return map;
	}

	@Override
	public int insertLicenceInfo(String orgId, String licenceNumber, String companyName, String companyAddress,
			String legalRepresentative, LocalDateTime now) {
		String delSql = "DELETE FROM licence_authentication WHERE org_id=?";
		jdbcTemplate.update(delSql, orgId);
		String sql = "INSERT INTO licence_authentication ( org_id, licence_number, company_name, company_address, legal_representative, gmt_create ) VALUES (?,?,?,?,?,?)";
		int update = jdbcTemplate.update(sql,
				new Object[] { orgId, licenceNumber, companyName, companyAddress, legalRepresentative, now });
		return update;
	}

	@Override
	public int updateLicenceInfo(String licenceNumber, String companyName, String companyAddress,
			String legalRepresentative, String gmtKey, LocalDateTime now, String orgId) {
		StringBuffer buffer = new StringBuffer(
				"UPDATE licence_authentication set licence_number=? ,company_name=? ,company_address=? ,legal_representative=? ,");
		List<Object> list = new ArrayList<Object>();
		list.add(licenceNumber);
		list.add(companyName);
		list.add(companyAddress);
		list.add(legalRepresentative);
		if (gmtKey.equals("gmt_create")) {
			buffer.append("status=? ,gmt_create=? WHERE org_id=?");
			list.add(0);
			list.add(now);
			list.add(orgId);
		}
		if (gmtKey.equals("gmt_modified")) {
			buffer.append("gmt_modified=? WHERE org_id=?");
			list.add(now);
			list.add(orgId);
		}
		String sql = buffer.toString();
		Object[] args = list.toArray();
		int update = jdbcTemplate.update(sql, args);
		return update;
	}

	@Override
	public List<String> getStaffIdList(String orgId, String menuId) {
		String sql = " SELECT DISTINCT [staff_id] " 
				+ " FROM [asxuexi_dev].[dbo].[staff_areas] "
				+ " WHERE province_id = " 
				+ " (SELECT city.ParentId AS province_id "
				+ " FROM [asxuexi_dev].[dbo].[org] " 
				+ " INNER JOIN dbo.areas AS county ON dbo.org.localid = county.ID "
				+ " INNER JOIN dbo.areas AS city ON county.ParentId = city.ID " 
				+ " WHERE orgid = ?) "
				+ " OR city_id = " 
				+ " (SELECT county.ParentId AS city_id " 
				+ " FROM [asxuexi_dev].[dbo].[org] "
				+ " INNER JOIN dbo.areas AS county ON dbo.org.localid = county.ID " 
				+ " WHERE orgid = ?) "
				+ " OR [range] = 1 " 
				+ " Intersect " 
				+ " SELECT DISTINCT [staff_id] "
				+ " FROM [asxuexi_dev].[dbo].[staff_menu] " 
				+ " WHERE menu_id = ? ";
		List<String> staffIdList = jdbcTemplate.queryForList(sql, String.class, orgId, orgId, menuId);
		return staffIdList;
	}
}
