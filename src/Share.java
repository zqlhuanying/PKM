import info.LinkToDB;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Share extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Share() {
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
		PrintWriter out = response.getWriter();
		String uid = request.getParameter("uid");
		String tid = request.getParameter("tid");
		String level=request.getParameter("level");
		String cancel=request.getParameter("cancel");
		String sql = null;
		SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Connection con = LinkToDB.linktodb();
		if(cancel.equals("0"))
			sql="update pkm_tag set isShare=1 , edittime=? where userid=? and tagid=? and ilevel=?";
		else
			sql="update pkm_tag set isShare=0 , edittime=? where userid=? and tagid=? and ilevel=?";
		try{
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setTimestamp(1, Timestamp.valueOf(dd.format(new java.util.Date())));
			pstmt.setString(2, uid);
			pstmt.setString(3, tid);
			pstmt.setString(4, level);
			pstmt.executeUpdate();
			out.print("0");
		}catch(Exception e){out.print("1");}	
		
		out.flush();
		out.close();
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
