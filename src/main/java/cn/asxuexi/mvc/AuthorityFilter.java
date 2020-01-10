package cn.asxuexi.mvc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMethod;

import cn.asxuexi.tool.Token_JWT;

public class AuthorityFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String type = request.getHeader("X-Requested-With");
		String url = request.getContextPath();
		if ((String)Token_JWT.verifyToken().get("userId") == null) {
			if ("XMLHttpRequest".equalsIgnoreCase(type)) {
				response.setHeader("Access-Control-Allow-Origin", "*");//跨域
				response.setHeader("sessionstatus", "timeout");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else {
				if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
		            chain.doFilter(request, response);
		        }else {
					response.sendRedirect(url + "/pagers/login/log_in.jsp");
		        }
			}
		} else {
			chain.doFilter(req, res);
		}
	}

}
