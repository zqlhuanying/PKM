<%@ page language="java" import="java.util.*,info.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String username = request.getParameter("username");
	if(username == null || "".equals(username)) response.sendRedirect("login.jsp");
	else username = new String(username.getBytes("ISO-8859-1"),"UTF-8"); 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>index</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="css/index.css">
	<link rel="stylesheet" type="text/css" href="css/rightContextMenu.css">
	<link rel="stylesheet" type="text/css" href="css/tree.css">
	<link rel="stylesheet" type="text/css" href="css/body.css">
	<link rel="stylesheet" type="text/css" href="css/base/jquery.ui.all.css">
	<link rel="stylesheet" type="text/css" href="js/upload/uploadify.css">
	<link rel="stylesheet" type="text/css" href="js/DateTimePicker/jquery-ui.css">
	<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="js/rightContextMenu.js"></script>
	<script type="text/javascript" src="js/alarm.js"></script>
	<script type="text/javascript" src="js/tree.js"></script>
	<script type="text/javascript" src="js/upload/uploadify.js"></script>
	<script type="text/javascript" src="js/Dialog/jquery.ui.all.js"></script>
	<script type="text/javascript" src="js/DateTimePicker/jquery.ui.datepicker.js"></script>
	<script type="text/javascript" src="js/FlexPaper/flexpaper_flash.js"></script>
	<script type="text/javascript" src="js/index.js"></script>
  </head>
  
  <body>
  <div id="all">
  	<div id="nav">
  		<div class="nl">
  			<ul>
		  		<li><a href="index.jsp?username=<%=username%>">主页</a></li>
		  		<span class="seperate"></span>
		  		<li><a href="schedule.jsp?username=<%=username%>">日记管理</a></li>
	  		</ul>
  		</div>
  		<div class="nr">
  			<ul>
  				<li>
  					<form>
  						<input type="radio" name="sear-if" value="站内" checked="checked"/>站内
  						<input type="radio" name="sear-if" value="全站" />全站
  						<input class="sear-info" type="text" name="sear-info" />
  						<img class="sear" src="img/robin-search-btn.png" />
  						<span class="input-placeholder">搜索你的文件</span>
  					</form>
  				</li>
				<li>
					<span></span>
					<span style="display:none"></span>
				</li>  			
				<li>
					<span>更多</span>
					<img src="img/down-arrow.jpg" />
					<div class="more">
						<img src="img/up-arrow.jpg" />
						<ul>
							<!-- <li>个人资料</li>
							<li>设置</li> -->
							<li>退出</li>
						</ul>
					</div>
				</li>
  			</ul>
  		</div>
  	</div>
	<div id="left">
		<div id="PKM_Tree1">
			<ul>
				<!-- <li><span>PKM</span></li>
				<li><span>PKM</span></li> -->
			</ul>
		</div>
		<div class="DateTimePicker"></div>
	</div>
	<div id="right">
		<div class="header-shaw">
			<div class="location">
				<ul>
					<!-- <li class="l1"><img src="img/upload.jpg" /></li> -->
					<li id="fileQueue">
					<!-- <a href="javascript:$('#fileQueue').uploadifySettings('scriptData',{'url':$('.dir-path').find('li:eq(1)').find('span:last').data('url')});$('#fileQueue').uploadifyUpload()">开始上传</a> -->
					</li>
					<li class="l2"><img src="img/newfold.jpg" /></li>
				</ul>
			</div>
			<div class="dir-path">
				<ul>
					<li></li>
					<li><span>全部文件</span></li>
					<li class="dir-data">已全部加载，共<span></span>个</li>
				</ul>
			</div>
		</div>
		<div class="file">
			<div class="f-header">
				<div class="c1"><span>文件名</span></div>
				<div class="c2"><span>大小</span></div>
				<div class="c3"><span>修改日期</span></div>
			</div>
			<div id="PKM_Tree">
				<!--  <div id="tree">
					<ul class="root_tree_ul">
						<li>
							<div class="tl1"><span class="folder"></span><span>文件名</span></div>
							<div class="tl2"><span>大小</span></div>
							<div class="tl3"><span>修改日期</span></div>
						</li>
					</ul>
				</div> -->
			</div>
		</div>
		 <!-- <div style="position:absolute;left:10px;top:10px;">
	        <a id="viewerPlaceHolder" style="width:660px;height:480px;display:block"></a>
	    </div>  -->
	</div>
  </div>
   <div id="outerwrap">
   		<input type="hidden" value=""/>
   </div>
   <!-- <div id="viewerPlaceHolder" style="position:absolute;display:block;width:800px;height:650px;z-index:1000;visibility:hidden;"></div> --> 
  </body>
  <script type="text/javascript">
  	$(document).ready(function() { 
 		
	});
  </script>
</html>
