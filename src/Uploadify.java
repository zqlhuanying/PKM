import info.LinkToDB;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;


public class Uploadify extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Uploadify() {
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
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();		    
		
        String fileRealPath = "";//文件存放真实地址    
          
        //名称  界面编码 必须 和request 保存一致..否则乱码      
        String savePath = request.getParameter("url");              
        savePath = java.net.URLDecoder.decode(savePath,"utf-8");  
        String firstFileName="";     
        //System.out.println(savePath);
        try {    
        	//File file = new File(savePath);
        	//if(file.exists()){
	            DiskFileItemFactory fac = new DiskFileItemFactory();    
	            ServletFileUpload upload = new ServletFileUpload(fac);    
	            upload.setHeaderEncoding("UTF-8");    
	            // 获取多个上传文件    
	            List fileList =  upload.parseRequest(request);    
	            // 遍历上传文件写入磁盘    
	            Iterator it = fileList.iterator();    
	            while (it.hasNext()) {    
	                Object obit = it.next();  
	                if(obit instanceof DiskFileItem){  
	                    DiskFileItem item = (DiskFileItem) obit;    
	                        
	                    // 如果item是文件上传表单域       
	                    // 获得文件名及路径       
	                    String fileName = item.getName();   
	                    if (fileName != null) {    
	                        firstFileName=item.getName().substring(item.getName().lastIndexOf("\\")+1);   
	                        //String formatName = firstFileName.substring(firstFileName.lastIndexOf("."));//获取文件后缀名    
	                        fileRealPath = savePath + "\\" + firstFileName ;//文件存放真实地址    
	                            
	                        BufferedInputStream in = new BufferedInputStream(item.getInputStream());// 获得文件输入流    
	                        BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(new File(fileRealPath)));// 获得文件输出流    
	                        Streams.copy(in, outStream, true);// 开始把文件写到你指定的上传文件夹                      
	                        

	            			
	                        //上传成功 写入数据库
	                        String replace=request.getParameter("replace");
	                        //System.out.println("replace:"+replace);
	                        String uid=request.getParameter("uid");
	                		String pid=request.getParameter("pid");
	                		String level=request.getParameter("level");
	                		String type="0";	
	                		long fileSize=item.getSize();
	                		SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                		Connection con=LinkToDB.linktodb();
	                		if ((replace == null) || (replace.equals("0"))) {
	                			String sql="insert into pkm_tag(userid,tagname,parentid,oldparentid,ilevel,isparent,size,url,oldurl,edittime,addtime) values(?,?,?,?,?,?,?,?,?,?,?)";
	                			try{
	                				PreparedStatement pstmt=con.prepareStatement(sql);
	                				pstmt.setString(1, uid);
	                				pstmt.setString(2, fileName);
	                				pstmt.setString(3, pid);
	                				pstmt.setString(4, pid);
	                				pstmt.setString(5, level);
	                				pstmt.setString(6, type);
	                				pstmt.setLong(7, fileSize);
	                				pstmt.setString(8, fileRealPath);
	                				pstmt.setString(9, fileRealPath);
	                				pstmt.setTimestamp(10, Timestamp.valueOf(dd.format(new java.util.Date()))); 
	                				pstmt.setTimestamp(11, Timestamp.valueOf(dd.format(new java.util.Date())));
	                				pstmt.executeUpdate();
	                			}catch(Exception e){e.printStackTrace();}
	                		} else {
	                			String sql="update pkm_tag set edittime=? , size=? where userid=? and tagname=? and  ilevel=?";
	                			try{
	                				PreparedStatement pstmt1=con.prepareStatement(sql);
	                				pstmt1.setTimestamp(1, Timestamp.valueOf(dd.format(new java.util.Date())));
	                				pstmt1.setLong(2, fileSize);
	                				pstmt1.setString(3, uid);
	                				pstmt1.setString(4, fileName);
	                				pstmt1.setString(5, level);
	                				pstmt1.executeUpdate();
	                			}catch(Exception e){e.printStackTrace();}
	                		}
	                    }     
	                }  
	            } 
        	//}else{out.write("0");}
        } catch (Exception ex) {  
           ex.printStackTrace();    
           out.write("0");    
           return;    
        }     
        out.write("1");
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
