package cn.asxuexi.organizationWebsite.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author fanjunguo
 * @version 2018年11月22日 下午9:24:50
 * @description 
 */
public interface OrgWebsiteDao {

	int savePages(String html, String pageName, String orgId, LocalDateTime createTime);

	Map<String, Object> getPages(String orgId, String pageName);

	/**
	 * @description 此方法用于往数据库存入制作好的二级网站模版. 如果该页不存在,则创建;如果该页存在,则更新html内容
	 * @param htmlContent 网站模版内容
	 * @param pageName 页面名称
	 * @param templateName 模版名称(自己在前端命名,暂时template1,template2...)
	 */
	int insertTemplate(String htmlContent, String pageName, String templateName, LocalDateTime createTime);

	int getWebsite(String orgId);

	List<Map<String, Object>> getWebsiteTemplate();

	List<Map<String, Object>> getAllTemplateName();

	void chooseTemplate(String tempalteName, String orgId);

	/**
	 * @author fanjunguo
	 * @description 查询模版页是否已经存在
	 * @param templateName 模版名称
	 * @param pageName 页面名称
	 * @return 如果存在返回1,不存在返回0
	 */
	int isTemplateExisted(String templateName, String pageName);


	/**
	 * @author fanjunguo
	 * @description 更新模版页内容
	 * @param templateName 模版名
	 * @param pageName 页面名
	 * @param htmlContent 页面内容
	 * @return 更新成功返回1,失败返回0
	 */
	int updateTemplateContent(String templateName, String pageName, String htmlContent, LocalDateTime time);

	/**
	 * @author fanjunguo
	 * @description 查询二级网站头部内容,不需要传页面名字,默认为header
	 * @param orgId 机构id
	 * @return map
	 */
	Map<String, Object> getHeader(String orgId);

	/**
	 * @author fanjunguo
	 * @description  更新机构二级网站的头部内容
	 * @param orgId 机构id
	 * @param headerHtml 头部内容
	 * @return int 1表示更新成功;0表示更新失败
	 */
	int updateOrgHeader(String orgId, String headerHtml,LocalDateTime time);

	/**
	 * @author fanjunguo
	 * @description 更新页面的title
	 */
	int updatePageTitle(String title, String pageName, String orgId);

	/**
	 * @author fanjunguo
	 * @description 删除机构二级网站
	 * @param orgId 机构id
	 */
	void delWebsite(String orgId);


}