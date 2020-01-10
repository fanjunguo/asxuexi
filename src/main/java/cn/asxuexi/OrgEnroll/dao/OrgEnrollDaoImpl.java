package cn.asxuexi.OrgEnroll.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrgEnrollDaoImpl implements OrgEnrollDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * @ 作用 查询全国省份和城市
	 **/
	@Override
	public List<Map<String, Object>> listCity(String cityId) {
		String sql = "select id,name,LevelType from areas where ParentId in (select ParentId from areas where ID=?) or LevelType=1";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, new Object[] { cityId });
		return queryForList;
	}

	/**
	 * @param cityId
	 *            城市id
	 * @作用 获取城市所在的省份id
	 */
	@Override
	public Map<String, Object> getProvinceId(String cityId) {
		String sql = "select ParentId from areas where ID=?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { cityId });
		return map;
	}

	/**
	 * @param cityId
	 *            城市id
	 * @作用 获取城市所在的县区
	 */
	@Override
	public List<Map<String, Object>> listAddress(String cityId) {
		String sql = "select id,name from areas where ParentId=?";
		List<Map<String, Object>> map = jdbcTemplate.queryForList(sql, new Object[] { cityId });
		return map;
	}

	/**
	 * @param orgId
	 *            机构id
	 * @param orgName
	 *            机构名称
	 * @param orgHead
	 *            机构负责人
	 * @param orgTel
	 *            机构电话
	 * @param loacltion
	 *            机构所在县区id
	 * @param userId
	 *            用户id
	 * @param now
	 *            现在的时间
	 * @作用 新增机构,保存机构信息
	 */
	@Override
	public int insertOrgInfo(String orgId, String orgName, String orgHead, String orgTel, String loacltion,
			String userId, LocalDateTime now) {
		String sql = "insert into org (orgid,orgname,head,tel,localid,user_id,lng,lat,gmt_create) values(?,?,?,?,?,?,(SELECT  [Lng] FROM dbo.[areas] where [ID]=?),(SELECT [Lat]  FROM dbo.[areas] where [ID]=?),?)";
		int update = jdbcTemplate.update(sql,
				new Object[] { orgId, orgName, orgHead, orgTel, loacltion, userId, loacltion, loacltion, now });
		return update;
	}

	@Override
	public int insertOrgLogo(String logoId, String orgId, String logoName, LocalDateTime now) {
		String sql="insert into logo (id,name,org_id,gmt_create) values (?,?,?,?)";
		int update = jdbcTemplate.update(sql, new Object[] {logoId,logoName,orgId,now});
		return update;
	}

	/**
	 * 维护用户-机构关系表(新增关系)
	 * 
	 * @author fanjunguo
	 * @param userId 用户ID
	 * @param orgId 新增的机构id
	 * @param isMainSchool 是否是主校
	 * @return
	 */
	@Override
	public int insertUserOrgRelationship(String userId, String orgId, boolean isMainSchool,LocalDateTime now) {
		String sql="insert into user_org values (?,?,?,?)";
		return jdbcTemplate.update(sql, new Object[] {userId,orgId,isMainSchool,now});
	}

}
