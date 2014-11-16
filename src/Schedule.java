import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import info.LinkToDB;

public class Schedule extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Schedule() {
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
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String change = request.getParameter("change");    //标记状态
		String uid = request.getParameter("uid");
		String sid = request.getParameter("sid");
		String scontent = request.getParameter("content");
		String time = request.getParameter("time");
		String atime = request.getParameter("alarmtime");
		//System.out.println(scontent+":"+time+":"+atime);
		Connection con = LinkToDB.linktodb();
		if(change.equals("0")){
			String sql = "insert into PKM_Schedule(userid,schedulecontent,time,alarmtime) values(?,?,?,?)";
			try{
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, uid);
				pstmt.setString(2, scontent);
				pstmt.setString(3, time);
				pstmt.setString(4, atime);
				pstmt.executeUpdate();
			}catch(Exception e){e.printStackTrace();}
		}else if(change.equals("1")){
			String sql = "update PKM_Schedule set schedulecontent=? , alarmtime=? where userid=? and scheduleid=?";
			try{
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, scontent);
				pstmt.setString(2, atime);
				pstmt.setString(3, uid);
				pstmt.setString(4, sid);
				pstmt.executeUpdate();
			}catch(Exception e){e.printStackTrace();}
		}else if(change.equals("2")) {
			String sql = "delete from PKM_Schedule where userid=? and scheduleid=?";
			try{
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, uid);
				pstmt.setString(2, sid);
				pstmt.executeUpdate();
			}catch(Exception e){e.printStackTrace();}
		}
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
