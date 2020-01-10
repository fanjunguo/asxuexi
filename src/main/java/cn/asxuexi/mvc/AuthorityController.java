package cn.asxuexi.mvc;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.asxuexi.service.AuthorityService;

/**
 * @author fanjunguo
 * @version 2018年11月21日 上午9:46:15
 * @description 根据地址,跳转到相应的页面.但是因为有的页面需要用户登录才能访问,所以需要先通过后端,判断用户是否登录.
 * 				目前用到的地方:网站顶部菜单、机构中心和个人中心的左菜单
 */
@Controller
public class AuthorityController {
	@Resource
	private AuthorityService service;
	
	/**
	 * @description 判断用户和机构是否登录,如果已经登录,跳转到目的页面,如果没有登录,跳到登录页面(如果是机构,跳到机构注册页面)
	 * 				!!!!!!!!另外,此方法只能处理“两级”(如org/orgcenter.do)的请求,所以尽量保证请求是两级的结构
	 * @author fanjunguo
	 * @param folderName 第一级目录名称,即目标页面所在的文件夹
	 * @param pageName	第二级目录名称,即目标文件的文件名称
	 * @return String,跳转的页面名称
	 */
	@RequestMapping(value="{folderName}/{pageName}.do")
	public String isLogin(@PathVariable("folderName") String folderName,@PathVariable("pageName") String pageName) {
		String purposePageName = service.isLogin(folderName,pageName);
		return purposePageName;
		
	}
}
