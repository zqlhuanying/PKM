package info;

import java.sql.*;
import javax.naming.*;
import javax.servlet.*;
import javax.sql.*;

public class LinkToDB1 {
	public static Connection linktodb() throws ServletException{
		Connection conn=null;
		try{
			Context c = new InitialContext();
			DataSource ds = (DataSource)c.lookup("java:comp/env/jdbc/PKM");
			conn = ds.getConnection();
		}catch(Exception e){e.printStackTrace();}
		return conn;
	}
}
