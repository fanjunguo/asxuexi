package cn.asxuexi.cros;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * @ClassName: CorsFilter.java
 * @Description: CORS过滤器
 *
 * @version: v1.0.0
 * @author: zhoubin
 * @date: 2019年3月6日 下午7:39:51
 * @Modification_History: Date----------Author-----------Version-----------Description
 *                        *---------------------------------------------------------*
 *                        2019年2月20日---zhoubin-----------v1.0.0------------修改原因
 */
@Component
public class CorsFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,access-token,content-type,user-agent");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}
}