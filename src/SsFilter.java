import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

public class SsFilter implements Filter {
	 @Override
	 public void destroy() {}
	 
	 @Override
	 public void doFilter(ServletRequest request, ServletResponse response,
	   FilterChain chain) throws IOException, ServletException {
		 System.out.println("1");
		  HttpServletRequest req = (HttpServletRequest) request;
		  HttpServletResponse resp = (HttpServletResponse) response;
		  String url=req.getRequestURL().toString();
		  int i = url.lastIndexOf("/");
		  String t = url.substring(i + 1);
		  System.out.println(t);
		  if(!"".equals(url) && !"login.jsp".equals(t) && !"register.jsp".equals(t)){
			  HttpSession session = req.getSession(false);
			  if (session == null) {
				  
				  if("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))){
					  System.out.println("2");
						resp.setHeader("sessionstatus", "timeout");
						resp.setHeader("redirectUrl", req.getContextPath() + "/login.jsp");
					} else {
						System.out.println("3");
						resp.sendRedirect(req.getContextPath() + "/login.jsp");
					}
				   //resp.sendRedirect("login.jsp");
				   return;
			  }
			  
		  }
		  chain.doFilter(request, response);
	 }
	 
	 @Override
	 public void init(FilterConfig arg0) throws ServletException {}
}