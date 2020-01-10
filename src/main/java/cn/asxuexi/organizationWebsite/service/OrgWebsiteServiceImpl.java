package cn.asxuexi.organizationWebsite.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.asxuexi.organizationWebsite.dao.OrgWebsiteDao;
import cn.asxuexi.tool.GetOrgIdFromRedis;

/**
 * @author fanjunguo
 * @version 2018年11月22日 下午9:07:36
 * @description 
 */
@Service
public class OrgWebsiteServiceImpl implements OrgWebsiteService {

	@Resource
	private OrgWebsiteDao dao;
	
	/**
	 * @author fanjunguo
	 * @description 将修改的网页,更新到数据库;包含header部分的内容
	 * @param html 网页内容
	 * @param pageName 网页名字(哪一个页面)
	 */
	@Override
	public int savePages(String contentHtml,String headerHtml,String pageName) {
		LocalDateTime now = LocalDateTime.now();
		String orgId = GetOrgIdFromRedis.getOrgId();
		int result;
		try {
			result = dao.savePages(contentHtml,pageName,orgId,now);
			int updateOrgHeader = dao.updateOrgHeader(orgId, headerHtml,now);
			if(result==0||updateOrgHeader==0) {
				result=0;
			}
		} catch (Exception e) {
			result=0;
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @author fanjunguo
	 * @description 根据条件机构id和页面名称,查询网页内容; 由于头部是所有页面共用的,所以默认查出头部内容,与网页内容拼接
	 * @param pageName 网页名
	 * @param orgId 机构ID
	 * @return json机构数据: result表示结果(1成功,0有异常) page_html是网页内容
	 */
	@Override
	public Map<String, Object> getPages(String pageName,String orgId) {
		//controller层接受了两个请求,.action请求不传递机构id参数,需要从session中获取
		if (orgId==null) {
			orgId = GetOrgIdFromRedis.getOrgId();
		}
		Map<String, Object> resultMap=new HashMap<>();
		//EmptyResultDataAccessException异常是数据库中查不到数据的时候出现的,所以判断一下,加个“result值”
		try {
			Map<String, Object> header = dao.getHeader(orgId);
			resultMap = dao.getPages(orgId,pageName);
			resultMap.put("header", header.get("page_html"));
			resultMap.put("result", "1");
		} catch (Exception e) {
			resultMap.put("result", "0");
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * @author fanjunguo
	 * @description 查询机构二级网站的头部内容.由于此请求是可以非登录状态下请求的,orgId从前端传入,所以有可能传来的orgId并没有开通机构,这个时候会出异常
	 * @param orgId
	 * @return Map<String, Object> 如果机构有二级网站,result="success",否则result="fail".   
	 */
	@Override
	public Map<String, Object> getHeader(String orgId){
		Map<String, Object> header=new HashMap<String, Object>();
		try {
			header = dao.getHeader(orgId);
			header.put("result", "success");
		} catch (Exception e) {
			header.put("result", "fail");
		}
		return header;
		
	}

	/**
	 * @description 此方法用于往数据库存入制作好的二级网站模版. 如果该页不存在,则创建;如果该页存在,则更新html内容
	 * @param htmlContent 网站模版内容
	 * @param pageName 页面名称
	 * @param templateName 模版名称(自己在前端命名,暂时template1,template2...)
	 */
	@Override
	public int insertTemplate(String htmlContent,String pageName,String templateName) {
		LocalDateTime time = LocalDateTime.now();
		int templateExisted = dao.isTemplateExisted(templateName, pageName); //0表示不存在
		int result;
		if (templateExisted==1) {
			//更新
			result=dao.updateTemplateContent(templateName, pageName, htmlContent,time);
		} else {
			//插入
			result = dao.insertTemplate(htmlContent, pageName, templateName, time);
		}
		return result;
	}
	
	/**
	 * @description 查询机构是否已经设置过二级网站,不传参数.通过session获取orgid. 并对查询结果进行判断:如果已经有二级网站则展示二级网站数据,否则展示模版选择
	 */
	@Override
	public Map<String, Object> getWebsite() {
		String orgId = GetOrgIdFromRedis.getOrgId();
		int isHaveWebsite = dao.getWebsite(orgId);
		Map<String, Object> resultMap=new HashMap<>();
		List<List<Map<String, Object>>> resultList=null;
		//机构没有开通过二级网站,那么展示模版
		if(isHaveWebsite==0) {
			List<Map<String, Object>> allTemplateNameList = dao.getAllTemplateName();
			List<Map<String, Object>> websiteTemplate = dao.getWebsiteTemplate();
			resultList=new ArrayList<>();
			//第一层:遍历所有的模版
			for (Map<String, Object> nameMap : allTemplateNameList) {
				List<Map<String, Object>> thisList=new ArrayList<Map<String, Object>>();
				String templateName=(String) nameMap.get("template_name");
				//第二层:遍历模版的网页
				for (Map<String, Object> map2 : websiteTemplate) {
					if (map2.get("template_name").equals(templateName)) {
						thisList.add(map2);
					}
				}
				resultList.add(thisList);
			}
			resultMap.put("list", resultList);
			resultMap.put("orgId", "");
		}else {
			resultMap.put("list", "");
			resultMap.put("orgId", orgId);
		}
		return resultMap;
	}
	
	/**
	 * @description 处理机构二级网站请求,根据pageName做判断,如果是null,则默认是index首页
	 * @param orgId 机构id
	 * @param pageName 网页的名称
	 * @return 
	 */
	@Override
	public String loadOrgWebsite(String orgId,String pageName) {
		if(null==pageName) {
			pageName="index";
		}
		String url="organization_website/"+pageName;
		return url;
	}
	
	@Override
	public String chooseTemplate(String tempalteName) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		String url="edit/"+orgId+".action";
		try {
			dao.chooseTemplate(tempalteName, orgId);
		} catch (Exception e) {
			url="pagers/wrong_pages/failure.jsp";
			e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * @author fanjunguo
	 * @description 更换模版:先删除原来的网站,然后再重新选择模版
	 * @param tempalteName 模版名称
	 * @return String 需要跳转的页面的url
	 */
	@Override
	public String changeTemplate(String tempalteName) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		String url="edit/"+orgId+".action";
		try {
			dao.delWebsite(orgId);
			dao.chooseTemplate(tempalteName, orgId);
		} catch (Exception e) {
			url="pagers/wrong_pages/failure.jsp";
			e.printStackTrace();
		}
		return url;
	}
	
	
	/**
	 * @author fanjunguo
	 * @description 更新页面的title
	 */
	@Override
	public int updatePageTitle(String title,String pageName) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		int result = dao.updatePageTitle(title, pageName, orgId);
		return result;
	}
	
}
