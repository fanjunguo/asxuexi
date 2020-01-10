package cn.asxuexi.organizationWebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author fanjunguo
 * @version 2018年11月22日 下午9:24:09
 * @description 
 */
@Repository
public class OrgWebsiteDaoImpl implements OrgWebsiteDao {

	@Resource
	JdbcTemplate template;
	@Override
	public int savePages(String html,String pageName,String orgId,LocalDateTime editTime) {
		String sql="update org_website set page_html=?,gmt_modified=? where org_id=? and page_name=? ";
		int updateResult = template.update(sql, new Object[] {html,editTime,orgId,pageName});
		return updateResult;
	}
	
	
	@Override
	public Map<String, Object> getPages(String orgId,String pageName) {
		String sql="select page_html,title from org_website where org_id=? and page_name=?";
		Map<String, Object> pageMap = template.queryForMap(sql, new Object[] {orgId,pageName});
		return pageMap;
	}
	
	/**
	 * @author fanjunguo
	 * @description 查询二级网站头部内容,不需要传页面名字,默认为header
	 * @param orgId 机构id
	 * @return map
	 */
	@Override
	public Map<String, Object> getHeader(String orgId){
		String pageName="header";
		String a="select page_html from org_website where org_id=? and page_name=?";
		Map<String, Object> queryForMap = template.queryForMap(a, new Object[] {orgId,pageName});
		return queryForMap;
	}
	
	/**
	 * @author fanjunguo
	 * @description  更新机构二级网站的头部内容
	 * @param orgId 机构id
	 * @param headerHtml 头部内容
	 * @return int 1表示更新成功;0表示更新失败
	 */
	@Override
	public int updateOrgHeader(String orgId,String headerHtml,LocalDateTime time) {
		String pageName="header";
		String sql="update org_website set page_html=?,gmt_modified=? where org_id=? and page_name=?";
		int updateResult = template.update(sql, new Object[] {headerHtml,time,orgId,pageName});
		return updateResult;
	}
	/**
	 * @description 此方法用于往数据库存入制作好的二级网站模版. 如果该页不存在,则创建;如果该页存在,则更新html内容
	 * @param htmlContent 网站模版内容
	 * @param pageName 页面名称
	 * @param templateName 模版名称(自己在前端命名,暂时template1,template2...)
	 */
	@Override
	public int insertTemplate(String htmlContent,String pageName,String templateName,LocalDateTime createTime) {
		String sql="insert into template (template_name,page_name,html,gmt_create) values (?,?,?,?)";
		int result=0;
		result= template.update(sql, new Object[] {templateName,pageName,htmlContent,createTime});
		return result;
	}
	
	/**
	 * 
	 * @author fanjunguo
	 * @description 查询模版页是否已经存在
	 * @param templateName 模版名称
	 * @param pageName 页面名称
	 * @return 如果存在返回1,不存在返回0
	 */
	@Override
	public int isTemplateExisted(String templateName,String pageName) {
		String sql="select count(*) from template where template_name=? and page_name=?";
		Integer queryForObject = template.queryForObject(sql, Integer.class, new Object[] {templateName,pageName});
		return queryForObject;
	}
	
	/**
	 * @author fanjunguo
	 * @description 更新模版页内容
	 * @param templateName 模版名
	 * @param pageName 页面名
	 * @param htmlContent 页面内容
	 * @return 更新成功返回1,失败返回0
	 */
	@Override
	public int updateTemplateContent(String templateName,String pageName,String htmlContent ,LocalDateTime time) {
		String sql="update template set html=?, gmt_modified=? where template_name=? and page_name=?";
		int updateResult = template.update(sql, new Object[] {htmlContent,time,templateName,pageName});
		return updateResult;
	}
	/**
	 * @description 查询org_website表中,是否有机构的数据.用来判断机构是否已经开通过二级网站
	 */
	@Override
	public int getWebsite(String orgId) {
		String sql="select count(*) from org_website where org_id=?";
		int result = template.queryForObject(sql, int.class, new Object[] {orgId});
		return result;
	}
	
	/**
	 * @description 取得所有的网站模版
	 */
	@Override
	public List<Map<String, Object>> getWebsiteTemplate(){
		String sql="select * from template";
		List<Map<String, Object>> templateList = template.queryForList(sql);
		return templateList;
	}
	
	/**
	 * @description 查询所有的模版名称,为了在service层,把同一模版的网页整合到一起去
	 */
	@Override
	public List<Map<String, Object>> getAllTemplateName() {
		String sql="select distinct template_name from template ";
		List<Map<String, Object>> templateNameList = template.queryForList(sql);
		return templateNameList;
	}
	
	@Override
	public void chooseTemplate(String tempalteName,String orgId) {
		String selectSql="select page_name,html,title from template where template_name=?";
		List<Map<String, Object>> templateList = template.queryForList(selectSql, new Object[] {tempalteName});
		String insertSql="insert into org_website (org_id,page_name,title,page_html,gmt_create) values (?,?,?,?,?)";
		LocalDateTime now = LocalDateTime.now();
		BatchPreparedStatementSetter batch=new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement arg, int i) throws SQLException {
				arg.setString(1, orgId);
				arg.setString(2, (String) templateList.get(i).get("page_name"));
				arg.setString(3, (String) templateList.get(i).get("title"));
				arg.setString(4, (String) templateList.get(i).get("html"));
				arg.setObject(5, now);
			}
			
			@Override
			public int getBatchSize() {
				return templateList.size();
			}
		};
		template.batchUpdate(insertSql, batch);
	}
	
	//删除机构的二级网站内容
	@Override
	public void delWebsite(String orgId) {
		String sql="delete from org_website where org_id=?";
		template.update(sql, orgId);
	}
	
	/**
	 * @author fanjunguo
	 * @description 更新页面的title
	 */
	@Override
	public int updatePageTitle(String title,String pageName,String orgId) {
		String sql="update org_website set title=? where org_id=? and page_name=?";
		int update = template.update(sql, new Object[] {title,orgId,pageName});
		return update;
	}
	
}
