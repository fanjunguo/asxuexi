package cn.asxuexi.organizationWebsite.controller;

import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.organizationWebsite.service.OrgWebsiteService;

/**
 * @author fanjunguo
 * @version 2018年11月22日 下午9:10:21
 * @description 用于机构二级网站的页面设计的保存;访问二级网站时,将机构的页面写到对应的页面文件中
 * 
 */
@Controller
public class OrgWebsiteController {

	@Resource
	private OrgWebsiteService service;
	
	/**
	 * @author fanjunguo
	 * @description 将修改的网页,更新到数据库;包含header部分的内容
	 * @param html 网页内容
	 * @param pageName 网页名字(哪一个页面)
	 */
	@RequestMapping(value="orgWebsite/savePages.action")
	@ResponseBody
	public int savePages(String contentHtml,String headerHtml,String pageName) {
		int path = service.savePages(contentHtml,headerHtml,pageName);
		return path;
	}
	
	/**
	 * @author fanjunguo
	 * @description 根据条件:机构id和页面名称,查询网页内容
	 * @param pageName 网页名
	 * @param orgId 机构ID
	 */
	@RequestMapping(value= {"orgWebsite/getPages.do","orgWebsite/getPages.action"})
	@ResponseBody
	public Map<String, Object> getPages(String pageName,String orgId) {
		Map<String, Object> pages = service.getPages(pageName,orgId);
		return pages;
	}
	
	/**
	 * @author fanjunguo
	 * @description 查询机构二级网站的头部内容.由于此请求是可以非登录状态下请求的,orgId从前端传入,所以有可能传来的orgId并没有开通机构,这个时候会出异常
	 * @param orgId
	 * @return Map<String, Object> 如果机构有二级网站,result="success",否则result="fail".   
	 */
	@RequestMapping(value= {"orgWebsite/getHeader.do"})
	@ResponseBody
	public Map<String, Object> getHeader(String orgId){
		
		return service.getHeader(orgId);
		
	}
	
	/**
	 * @description 此方法用于往数据库存入制作好的二级网站模版. 如果该页不存在,则创建;如果该页存在,则更新html内容
	 * @param htmlContent 网站模版内容
	 * @param pageName 页面名称
	 * @param templateName 模版名称(自己在前端命名,暂时template1,template2...)
	 */
	@RequestMapping(value="orgWebsite/insertTemplate.do")
	@ResponseBody
	public int insertTemplate(String htmlContent,String pageName,String templateName) {
		int result = service.insertTemplate(htmlContent, pageName, templateName);
		return result;
	}
	
	/**
	 * @description 查询机构二级网站
	 */
	@RequestMapping(value="orgWebsite/getWebsite.action")
	@ResponseBody
	public Map<String, Object> getWebsite() {
		Map<String, Object> website = service.getWebsite();
		return website;
	}
	
	/**
	 * @description 处理机构二级网站请求,并处理完成后跳转到相应到页面.如果不传pageName,默认跳到首页
	 * @param orgId 机构id
	 * @param pageName 网页的名称
	 * @return 
	 */
	@RequestMapping("org/{orgId}.school")
	public String loadOrgWebsite(@PathVariable String orgId,String pageName) {
		String url = service.loadOrgWebsite(orgId,pageName);
		return url;
	}
	
	 //接收编辑二级网站的请求,重定向到二级网站编辑页面
	@RequestMapping("edit/{orgId}.action")
	public String editOrgWebsite() {
		return "organization_website/editor";
	}
	
	/**
	 * @description 
	 * @param tempalteName
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="orgWebsite/chooseTemplate.action")
	@ResponseBody
	public String chooseTemplate(String tempalteName) {
		String url = service.chooseTemplate(tempalteName);
		return url;
	}
	
	/**
	 * @description 更换模版
	 * @param tempalteName
	 * @return
	 */
	@RequestMapping(value="orgWebsite/changeTemplate.action")
	@ResponseBody
	public String changeTemplate(String tempalteName) {
		String url=service.changeTemplate(tempalteName);
		return url;
	}
	
	/**
	 * @author fanjunguo
	 * @description 更新页面的title
	 */
	@RequestMapping(value="orgWebsite/updatePageTitle.action")
	@ResponseBody
	public int updatePageTitle(String title,String pageName) {
		int result = service.updatePageTitle(title, pageName);
		return result;
	}
}
