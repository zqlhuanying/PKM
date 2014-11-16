import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import info.*;


public class Validate extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Validate() {
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
		ResultSet result=null;
		Statement stmt=null;
		PreparedStatement pstmt=null;
		Connection conn=LinkToDB.linktodb();
		String error="";
		try{
			stmt=conn.createStatement();
			String userName=request.getParameter("username");
			String formName=request.getParameter("formname");
			String password=request.getParameter("password");
			String email=request.getParameter("email");
			String register=request.getParameter("register");
			String sql="";
			if(formName.equals("register")) {sql="select 1 from PKM_User where username="+"'"+userName+"'";}
			if(formName.equals("login")) {sql="select 1 from PKM_User where username="+"'"+userName+"'"+" and password1="+"'"+password+"'";}
			stmt.executeUpdate("use PKM");
			result=stmt.executeQuery(sql);
			if(formName.equals("register")&&result.next()) error+="该用户已存在";
			if(formName.equals("login")&&!result.next()) error+="该用户不存在或密码错误";
			//用注册按钮的register的属性来标记提交注册请求，从而写入到数据库中
			if(register!=null){
				pstmt=conn.prepareStatement("insert into PKM_User(username,password1,password2) values(?,?,?)");
				pstmt.setString(1, userName);
				pstmt.setString(2, password);
				pstmt.setString(3, password);
				pstmt.executeUpdate();
				
				String s="select @@IDENTITY";
				ResultSet r=stmt.executeQuery(s);
				r.next();
				String uid = r.getString(1);
				
				pstmt=conn.prepareStatement("insert into PKM_Tag (userid,tagname,ilevel,isparent,url,oldURL) values(?,?,?,?,?,?)");
				pstmt.setString(1, uid);
				pstmt.setString(2, "PKM");
				pstmt.setString(3, "1");
				pstmt.setString(4, "1");
				pstmt.setString(5, "F:\\MyEclipse 10\\.metadata\\.me_tcat\\webapps\\PKM\\Work\\"+uid+"\\PKM");
				pstmt.setString(6, "F:\\MyEclipse 10\\.metadata\\.me_tcat\\webapps\\PKM\\Work\\"+uid+"\\PKM");
				pstmt.executeUpdate();
				
				pstmt=conn.prepareStatement("insert into PKM_Tag (userid,tagname,ilevel,isparent,url,oldURL) values(?,?,?,?,?,?)");
				pstmt.setString(1, uid);
				pstmt.setString(2, "保密柜");
				pstmt.setString(3, "1");
				pstmt.setString(4, "1");
				pstmt.setString(5, "F:\\MyEclipse 10\\.metadata\\.me_tcat\\webapps\\PKM\\Work\\"+uid+"\\保密柜");
				pstmt.setString(6, "F:\\MyEclipse 10\\.metadata\\.me_tcat\\webapps\\PKM\\Work\\"+uid+"\\保密柜");
				pstmt.executeUpdate();
				
				pstmt=conn.prepareStatement("insert into PKM_Tag (userid,tagname,ilevel,isparent,url,oldURL) values(?,?,?,?,?,?)");
				pstmt.setString(1, uid);
				pstmt.setString(2, "回收站");
				pstmt.setString(3, "1");
				pstmt.setString(4, "1");
				pstmt.setString(5, "F:\\MyEclipse 10\\.metadata\\.me_tcat\\webapps\\PKM\\Work\\"+uid+"\\回收站");
				pstmt.setString(6, "F:\\MyEclipse 10\\.metadata\\.me_tcat\\webapps\\PKM\\Work\\"+uid+"\\回收站");
				pstmt.executeUpdate();
				
				pstmt=conn.prepareStatement("insert into PKM_Tag (userid,tagname,ilevel,isparent) values(?,?,?,?)");
				pstmt.setString(1, uid);
				pstmt.setString(2, "分享");
				pstmt.setString(3, "1");
				pstmt.setString(4, "1");
				pstmt.executeUpdate();
				
				pstmt=conn.prepareStatement("insert into PKM_Tag (userid,tagname,ilevel,isparent) values(?,?,?,?)");
				pstmt.setString(1, uid);
				pstmt.setString(2, "收藏网址");
				pstmt.setString(3, "1");
				pstmt.setString(4, "1");
				pstmt.executeUpdate();
				
				File fp = new File("F:\\MyEclipse 10\\.metadata\\.me_tcat\\webapps\\PKM\\Work\\"+uid);  fp.mkdirs();
				File fp1 = new File("F:\\MyEclipse 10\\.metadata\\.me_tcat\\webapps\\PKM\\Work\\"+uid+"\\PKM"); fp1.mkdirs();
				File fp2 = new File("F:\\MyEclipse 10\\.metadata\\.me_tcat\\webapps\\PKM\\Work\\"+uid+"\\保密柜"); fp2.mkdirs();
				File fp3 = new File("F:\\MyEclipse 10\\.metadata\\.me_tcat\\webapps\\PKM\\Work\\"+uid+"\\回收站"); fp3.mkdirs();
			}
			//输出ajax响应
			out.print(error);
		}catch(SQLException e){e.printStackTrace();}
		/*out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");*/
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
