import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class FileOP extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public FileOP() {
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
		String url = request.getParameter("url");
		String isnew = request.getParameter("isnew");       //区分是重命名还是新建
		String complete = request.getParameter("complete");   //区分是否删除文件
		String download = request.getParameter("download");   //区分是否下载文件
		File fp = new File(url);
		if(download == null){
			if(isnew != null) fp.mkdirs();
			else if(complete == null){
				String oldname = request.getParameter("oldname");
				int i=url.lastIndexOf('\\');
				String path=url.substring(0,i);
				//System.out.println(path + "\\" + oldname);
				File old = new File(path + "\\" + oldname);
				old.renameTo(fp);
			} else if(complete.equals("0")){
				try{
					String recycleUrl = request.getParameter("recycleUrl");
					
					if(url.endsWith(File.separator)){    
						int j = url.lastIndexOf(File.separator);
						url = url.substring(0,j);
					}
					int k = url.lastIndexOf(File.separator);
					String ss = url.substring(k+1);
					recycleUrl = recycleUrl + File.separator + ss;
					
					copyFolder(url,recycleUrl);
					deletefile(url);
					out.print(recycleUrl);
				}catch(Exception e){e.printStackTrace();}	
			} else if(complete.equals("1")) {
				try{
					deletefile(url);
				}catch(Exception e){e.printStackTrace();}
			} else if(complete.equals("2")) {
				try{
					String newUrl = request.getParameter("oldurl");
					copyFolder(url,newUrl);
					deletefile(url);
				}catch(Exception e){e.printStackTrace();}
			}
		}else {
			int i=url.lastIndexOf('\\');
			String fileName=url.substring(i+1);
			String newPath = "C:\\Users\\Administrator\\Desktop\\" + fileName;
			copyFolder(url,newPath);
		}
		out.flush();
		out.close();
	}

	
	
	public void deletefile(String delpath) throws Exception {  
		  try {  		  
			  //System.out.println("delpath:"+delpath);
			  File file = new File(delpath);  
			  // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true  
			  if (!file.isDirectory()) {  
				  file.delete();  
			  } else if (file.isDirectory()) {  
				  String[] filelist = file.list();  
				  for (int i = 0; i < filelist.length; i++) {  
					  File delfile = new File(delpath + "\\" + filelist[i]);  
					  if (!delfile.isDirectory()) {  
						  delfile.delete();   
					  } else if (delfile.isDirectory()) {  
						  deletefile(delpath + "\\" + filelist[i]);  
					  }  
				  }  
				  file.delete();  
			  }  		  
		  	} catch (FileNotFoundException e) {e.printStackTrace();}   
		 }
	
	
	public void copyFolder(String oldPath, String newPath) {      
		try {    
			//System.out.println(oldPath);
			//System.out.println(newPath);
			//System.out.println(File.separator);
			File temp=new File(oldPath);
			if(temp.isFile()){    
				FileInputStream input = new FileInputStream(temp);    
				FileOutputStream output = new FileOutputStream(newPath);    
				byte[] b = new byte[1024 * 5];    
				int len;    
				while ( (len = input.read(b)) != -1) {    
					output.write(b, 0, len);    
				}    
				output.flush();    
				output.close();    
				input.close();  
			}
			if(temp.isDirectory()){//如果是文件夹    
				
				/*if(oldPath.endsWith(File.separator)){    
					int j = oldPath.lastIndexOf(File.separator);
					oldPath = oldPath.substring(0,j);
				}
				int k = oldPath.lastIndexOf(File.separator);
				String ss = oldPath.substring(k+1);
				newPath = newPath + File.separator + ss;*/
				
				(new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
				String[] file=temp.list();
				if(file.length > 0) {
					for (int i = 0; i < file.length; i++) {    
						copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);  
					}
				}
			}  
		} catch (Exception e) {System.out.println("复制整个文件夹内容操作出错");e.printStackTrace();}    
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
