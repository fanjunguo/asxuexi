package cn.asxuexi.organizationWebsite.service;

import java.util.Map;

/**
 * @author fanjunguo
 * @version 2018年11月22日 下午9:21:47
 * @description 
 */
public interface OrgWebsiteService {


	Map<String, Object> getPages(String pageName, String orgId);

	/**
	 * @description 此方法用于往数据库存入制作好的二级网站模版. 如果该页不存在,则创建;如果该页存在,则更新html内容
	 * @param htmlContent 网站模版内容
	 * @param pageName 页面名称
	 * @param templateName 模版名称(自己在前端命名,暂时template1,template2...)
	 */
	int insertTemplate(String htmlContent, String pageName, String templateName);

	Map<String, Object> getWebsite();

	String loadOrgWebsite(String orgId ,String pageName);

	String chooseTemplate(String tempalteName);

	/**
	 * @author fanjunguo
	 * @description 将修改的网页,更新到数据库;包含header部分的内容
	 * @param html 网页内容
	 * @param pageName 网页名字(哪一个页面)
	 */
	int savePages(String contentHtml, String headerHtml, String pageName);

	/**
	 * @author fanjunguo
	 * @description 更新页面的title
	 */
	int updatePageTitle(String title, String pageName);

	/**
	 * @author fanjunguo
	 * @description 查询机构二级网站的头部内容.由于此请求是可以非登录状态下请求的,orgId从前端传入,所以有可能传来的orgId并没有开通机构,这个时候会出异常
	 * @param orgId
	 * @return Map<String, Object> 如果机构有二级网站,result="success",否则result="fail".   
	 */
	Map<String, Object> getHeader(String orgId);

	/**
	 * @author fanjunguo
	 * @description 更换模版:先删除原来的网站,然后再重新选择模版
	 * @param tempalteName 模版名称
	 * @return String 需要跳转的页面的url
	 */
	String changeTemplate(String tempalteName);



}