package info;

import java.sql.*;
import javax.servlet.ServletException;

public class LinkToDB {
	
	public LinkToDB(){}
	
	public static Connection linktodb() throws ServletException{
		String driverClass="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url="jdbc:sqlserver://localhost:1433;DatabaseName=PKM";
		String username="sa";
		String password="sql2008";
		Connection conn=null;
		try{
			Class.forName(driverClass).newInstance();
			conn=DriverManager.getConnection(url, username, password);
		}catch(ClassNotFoundException e){throw new ServletException(" ˝æ›ø‚º”‘ÿ ß∞‹£°");}
		 catch(SQLException e){e.printStackTrace();}
		 catch(IllegalAccessException e){e.printStackTrace();}
		 catch(InstantiationException e){e.printStackTrace();}
		 return conn;
	}
}
