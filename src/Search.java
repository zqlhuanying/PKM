import info.PJsonArray;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;


public class Search extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Search() {
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
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String uid   = request.getParameter("uid");
		String sear  = request.getParameter("sear_con");
		String scope = request.getParameter("scope");
		String sql   = null;
		if(scope != null){
			sql = "select * from PKM_Schedule where userid = " + "'" + uid + "'" + " and ScheduleContent like '%"+ sear +"%'"; 
		}else{
			String sear_if = request.getParameter("sear_if");
			if("站内".equals(sear_if)){
				sql = "select * from PKM_Tag where userid = " + "'" + uid + "'" + " and tagName like '%"+ sear +"%' and parentid is not null";
			}else{
				sql = "select * from PKM_Tag where parentid is not null and tagName like '%"+ sear +"%' and (userid = " + "'" + uid + "'" + " or isShare = '1')";
			}
		}
		JSONArray json=new PJsonArray().getJSON(sql,scope);
		PrintWriter out = response.getWriter();
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
