import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import info.*;

public class Save extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Save() {
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
		String isnew=request.getParameter("isnew");
		String text=request.getParameter("text");
		String uid=request.getParameter("uid");
		String pid=request.getParameter("pid");
		String tid=request.getParameter("tid");
		String level=request.getParameter("level");
		//String size=request.getParameter("size");
		String type=request.getParameter("type");
		String url=request.getParameter("url");	
		SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Connection con=LinkToDB.linktodb();
		if(isnew!=null){					
			String sql="insert into pkm_tag(userid,tagname,parentid,oldparentid,ilevel,isparent,url,oldurl,edittime,addtime) values(?,?,?,?,?,?,?,?,?,?)";
			try{
				PreparedStatement pstmt=con.prepareStatement(sql);
				pstmt.setString(1, uid);
				pstmt.setString(2, text);
				pstmt.setString(3, pid);
				pstmt.setString(4, pid);
				pstmt.setString(5, level);
				pstmt.setString(6, type);
				pstmt.setString(7, url);
				pstmt.setString(8, url);
				pstmt.setTimestamp(9, Timestamp.valueOf(dd.format(new java.util.Date()))); 
				pstmt.setTimestamp(10, Timestamp.valueOf(dd.format(new java.util.Date())));
				pstmt.executeUpdate();
				String s="select @@IDENTITY";
				Statement stmt=con.createStatement();
				ResultSet r=stmt.executeQuery(s);
				r.next();
				out.print(r.getString(1));
			}catch(Exception e){e.printStackTrace();}
		}else{
			try{
				String sql1=" with temp as(select * from pkm_tag where userid= " + "'" + uid + "'" + " and tagid= " + "'" + tid + "'" + " and ilevel= " + "'" + level + "'"  
						+" union all select pkm_tag.* from temp, PKM_Tag where temp.tagid=PKM_Tag.ParentID and temp.userid=PKM_Tag.UserID) "
						+" select * into #temp  from temp "
						+" select * from #temp ";
				int i = url.lastIndexOf(File.separator);
				String ss = url.substring(0,i);
				//url = ss + File.separator + text;
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql1);
				while(rs.next()) {
					String turl = rs.getString("url");
					String tuid = rs.getString("userid");
					String ttid = rs.getString("tagid");
					//System.out.println(turl);
					int j = ss.length();
					String tss1 = turl.substring(0,j+1);
					int k = turl.indexOf(File.separator, j+1);
					if(k > 0) {
						String tss2 = turl.substring(k);
						turl = tss1 + text + tss2;
					} else {turl = tss1 + text;}
					//System.out.println(turl);
					String sql2 = " update PKM_Tag set url=? , oldurl=? where userid=? and tagid=? ";
					PreparedStatement pstmt2=con.prepareStatement(sql2);
					pstmt2.setString(1, turl);
					pstmt2.setString(2, turl);
					pstmt2.setString(3, tuid);
					pstmt2.setString(4, ttid);
					pstmt2.executeUpdate();
				}
				String sql3 = "update pkm_tag set tagname=? , edittime=? where userid=? and tagid=? and  ilevel=?";
				PreparedStatement pstmt3=con.prepareStatement(sql3);
				pstmt3.setString(1, text);
				pstmt3.setTimestamp(2, Timestamp.valueOf(dd.format(new java.util.Date())));
				pstmt3.setString(3, uid);
				pstmt3.setString(4, tid);
				pstmt3.setString(5, level);
				pstmt3.executeUpdate();
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
