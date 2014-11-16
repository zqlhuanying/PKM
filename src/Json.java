import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import info.PJsonArray;

public class Json extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Json() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String uid=request.getParameter("uid");
		String pid=request.getParameter("tid");
		String scope=request.getParameter("scope");
		String scheduletime=request.getParameter("scheduletime");
		String sql=null;
		if(scope!=null){
			sql="select * from PKM_Schedule where userid="+"'"+uid+"'"+"and time="+"'"+scheduletime+"'";
		}else {
			if(pid==null) {
				//uid="100";
				sql="select * from PKM_Tag where userid="+"'"+uid+"'"+"and parentid is null";
			}else{
				String text = request.getParameter("text");
				if("分享".equals(text)) sql="select * from PKM_Tag where userid="+"'"+uid+"'"+"and isShare=1"+"and parentid !=(select tagid from PKM_Tag where userid="+"'"+uid+"'"+" and tagname='回收站')";
				else sql="select * from PKM_Tag where userid="+"'"+uid+"'"+"and parentid="+"'"+pid+"'";
			}
		}
		JSONArray json=new PJsonArray().getJSON(sql,scope);
		PrintWriter out = response.getWriter();
		//System.out.println(json);
		//System.out.println(sql);
		out.print(json);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
