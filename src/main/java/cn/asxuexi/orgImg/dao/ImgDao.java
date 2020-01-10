package cn.asxuexi.orgImg.dao;

import java.time.LocalDateTime;
import java.util.List;

import cn.asxuexi.orgImg.entity.OrgImgEntity;

public interface ImgDao {
	/**
	 * @return
	 * @作用 删除数据库图片数据
	 */
	int deleteImg(String orgId, String imgName);

	/**
	 * @return
	 * @作用 删除数据库文件夹数据
	 */
	int deleteImgFIle(String orgId, String file);

	/**
	 * @return
	 * @作用 查询数据库是否存在数据
	 **/
	int isImg(String orgId, String imgName);

	/**
	 * @return
	 * @作用 查询数据库是否存在数据
	 **/
	public int isImgFile(String orgId, String imgName);

	/**
	 * @return
	 * @作用 插入数据库数据
	 **/
	public int insertImg(String id, String orgName, String orgId, LocalDateTime now);

	/**
	 * @return
	 * @作用 插入数据库数据（视频）
	 **/
	public int insertVideo(String id, String orgName, String orgId, LocalDateTime now);

	/**
	 * 批量插入图片记录
	 * 
	 * @param orgImgList
	 *            图片记录列表
	 * @return
	 */
	public int[] insertImgList(List<OrgImgEntity> orgImgList);
}
