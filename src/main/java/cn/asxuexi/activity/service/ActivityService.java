package cn.asxuexi.activity.service;

import cn.asxuexi.tool.JsonData;

public interface ActivityService {

	/**
	 * 根据id,查询广告信息
	 * 
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
	JsonData submit(String id,String name, String tel);

}
