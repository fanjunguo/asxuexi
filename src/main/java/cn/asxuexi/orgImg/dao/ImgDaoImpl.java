package cn.asxuexi.orgImg.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.asxuexi.orgImg.entity.OrgImgEntity;

@Repository
public class ImgDaoImpl implements ImgDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * @return
	 * @作用 删除数据库图片数据
	 */
	public int deleteImg(String orgId, String imgName) {
		String sql = "delete org_img where org_id=? and img_name=?";
		int update = jdbcTemplate.update(sql, new Object[] { orgId, imgName });
		return update;
	}

	/**
	 * @return
	 * @作用 删除数据库文件夹数据
	 */
	public int deleteImgFIle(String orgId, String file) {
		String sql = "delete org_img where org_id=? and img_name like '%'+?+'%'";
		int update = jdbcTemplate.update(sql, new Object[] { orgId, file });
		return update;
	}

	/**
	 * @return
	 * @作用 查询数据库是否存在数据
	 **/
	public int isImg(String orgId, String imgName) {
		String sql = "select count(*) from org_img where org_id=? and img_name=?";
		int update = jdbcTemplate.queryForObject(sql, new Object[] { orgId, imgName }, Integer.class);
		return update;
	}

	/**
	 * @return
	 * @作用 查询数据库文件夹下是否存在数据
	 **/
	public int isImgFile(String orgId, String imgName) {
		String sql = "select count(*) from org_img where org_id=? and img_name like '%'+?+'%'";
		int update = jdbcTemplate.queryForObject(sql, new Object[] { orgId, imgName }, Integer.class);
		return update;
	}

	/**
	 * @return
	 * @作用 插入数据库数据(图片)
	 **/
	public int insertImg(String id, String orgName, String orgId, LocalDateTime now) {
		String sql = "insert into org_img(img_id,img_name,org_id,gmt_create,status) values (?,?,?,?,0)";
		int update = jdbcTemplate.update(sql, new Object[] { id, orgName, orgId, now });
		return update;
	}

	/**
	 * @return
	 * @作用 插入数据库数据（视频）
	 **/
	public int insertVideo(String id, String orgName, String orgId, LocalDateTime now) {
		String sql = "insert into org_video(video_id,video_name,org_id,gmt_create,status) values (?,?,?,?,0)";
		int update = jdbcTemplate.update(sql, new Object[] { id, orgName, orgId, now });
		return update;
	}

	@Override
	public int[] insertImgList(List<OrgImgEntity> orgImgList) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (OrgImgEntity orgImgEntity : orgImgList) {
			batchArgs.add(new Object[] { orgImgEntity.getImgId(), orgImgEntity.getImgName(), orgImgEntity.getOrgId(),
					orgImgEntity.getGmtCreate(), orgImgEntity.getStatus() });
		}
		String sql = "insert into org_img(img_id,img_name,org_id,gmt_create,status) values (?,?,?,?,?)";
		int[] batchUpdate = jdbcTemplate.batchUpdate(sql, batchArgs);
		return batchUpdate;
	}
}
