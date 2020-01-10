package cn.asxuexi.activity.dao;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;

import cn.asxuexi.tool.JsonData;


public interface ActivityDao {

	/**
	 * 根据id,查询广告信息
	 */
	JsonData getAdInfo(String id);

	/**
	 * 提交报名信息
	 * 
	 * @author fanjunguo
	 * @param name 姓名
	 * @param tel 电话
	 * @return 报名结果
	 */
	int submit(@Param("id")String id,@Param("name")String name,@Param("tel") String tel,@Param("now")LocalDateTime now);
}
