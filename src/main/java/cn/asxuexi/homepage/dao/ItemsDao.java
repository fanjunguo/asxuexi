package cn.asxuexi.homepage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ItemsDao {
	/**
	 * 获取首页机构栏目信息，栏目下属的选项卡信息
	 * 
	 * @param cityId
	 *            城市ID
	 * @return
	 */
	List<Map<String, Object>> listDataOfOrg(@Param("cityId") String cityId);

	/**
	 * 获取首页课程栏目信息，栏目下属的选项卡信息
	 * 
	 * @param cityId
	 *            城市ID
	 * @return
	 */
	List<Map<String, Object>> listDataOfCourse(@Param("cityId") String cityId);

	/**
	 * 获取首页课程栏目包含的课程信息
	 * 
	 * @param cityId
	 *            城市ID
	 * @return
	 */
	List<Map<String, Object>> listCourses(@Param("cityId") String cityId);

	/**
	 * 获取首页机构栏目包含的机构信息
	 * 
	 * @param cityId
	 *            城市ID
	 * @return
	 */
	List<Map<String, Object>> listOrgs(@Param("cityId") String cityId);

	/**
	 * 获取首页机构栏目中通过审核的机构ID
	 * 
	 * @param orgIdList
	 *            机构栏目中所有机构的ID
	 * @return
	 */
	List<String> listValidatedOrgId(@Param("orgIdList") List<String> orgIdList);

	/**
	 * 获取首页课程栏目中所有课程的套餐信息
	 * 
	 * @param courseIdList
	 *            课程栏目中所有课程的ID
	 * @return
	 */
	List<Map<String, Object>> listPackages(@Param("courseIdList") List<String> courseIdList);

	/**
	 * 按照一定的排序规则,查询出给定城市下所有的课程list
	 * 
	 * @param orderRule
	 * @param cityId 县区id
	 */
	List<Map<String, Object>> getCourseOfRecommended(@Param("orderRule")int orderRule,@Param("cityId")String cityId);
	
	
	/**
	 * 取出课程数据表中,最新的发布的课程top8.如果同一个机构发布了多于1个课程,则继续往后取.保证取到8个不同的机构
	 * 
	 * 
	 * @author fanjunguo
	 * @param cityId
	 */
	List<Map<String, Object>> getTheLatestCourseOfRecommended(String cityId);

	/**
	 * 按照一定的排序规则,查询出给定城市下所有的机构list
	 * 
	 * @param orderRule 排序规则
	 * @param cityId 县区id
	 * @return
	 */
	List<Map<String, Object>> getOrgOfRecommended(@Param("orderRule")int orderRule,@Param("cityId")String cityId);

}
