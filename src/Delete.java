import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import info.*;

public class Delete extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Delete() {
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

		doPost(request,response);
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
		String uid=request.getParameter("uid");
		String tid=request.getParameter("tid");
		String level=request.getParameter("level");
		String complete=request.getParameter("complete");
		SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql=null;
		//System.out.println(uid+","+tid+","+level+","+complete);
		Connection con=LinkToDB.linktodb();
		if(complete.equals("0")){
			String newUrl=request.getHeader("newUrl");
			newUrl = java.net.URLDecoder.decode(newUrl,"utf-8");
			sql="update pkm_tag set parentid=(select tagid from PKM_Tag where userid=? and tagname='回收站') , url=? , edittime=? where userid=? and tagid=? and ilevel=?";
			//System.out.println(newUrl);
			try{
				PreparedStatement pstmt=con.prepareStatement(sql);
				pstmt.setString(1, uid);
				pstmt.setString(2, newUrl);
				pstmt.setTimestamp(3, Timestamp.valueOf(dd.format(new java.util.Date())));
				pstmt.setString(4, uid);
				pstmt.setString(5, tid);
				pstmt.setString(6, level);
				pstmt.executeUpdate();
			}catch(Exception e){e.printStackTrace();}	
		}else if(complete.equals("2")){
			try{
				sql="select oldparentid,oldurl from pkm_tag where userid="+"'"+uid+"'"+" and tagid="+"'"+tid+"'"+" and ilevel="+"'"+level+"'";
				Statement stmt=con.createStatement();
				ResultSet r=stmt.executeQuery(sql);
				r.next();
				String p=r.getString(1);
				String q=r.getString(2);
				
				sql="update pkm_tag set parentid=? , url=? , edittime=? where userid=? and tagid=? and ilevel=?";
				PreparedStatement pstmt=con.prepareStatement(sql);
				pstmt.setString(1, p);
				pstmt.setString(2, q);
				pstmt.setTimestamp(3, Timestamp.valueOf(dd.format(new java.util.Date())));
				pstmt.setString(4, uid);
				pstmt.setString(5, tid);
				pstmt.setString(6, level);
				pstmt.executeUpdate();
			}catch(Exception e){e.printStackTrace();}
		}else{
			try{
				sql=" with temp as(select * from pkm_tag where userid=? and tagid=? and ilevel=? " 
					+" union all select pkm_tag.* from temp, PKM_Tag where temp.tagid=PKM_Tag.ParentID and temp.userid=PKM_Tag.UserID) "
					+" delete from PKM_Tag where exists (select UserID,TagID from temp where temp.tagid=PKM_Tag.TagID and temp.userid=PKM_Tag.UserID)";
				PreparedStatement pstmt=con.prepareStatement(sql);
				pstmt.setString(1, uid);
				pstmt.setString(2, tid);
				pstmt.setString(3, level);
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
