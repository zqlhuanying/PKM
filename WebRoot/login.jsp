<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>PKM</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="js/login.js"></script>
	<link rel="stylesheet" type="text/css" href="css/login.css">
	<link rel="stylesheet" type="text/css" href="css/body.css">
  </head>
  
  <body>
    <form id="login" name="login" action="Validate" method="post">
    	<div class="inp"><img src="img/1.png" /><input type="text" name="username" value=""/><span>用户名</span></div>
    	<div class="inp pwd"><img src="img/2.png" /><input type="password" name="password" value=""/><span>请输入密码</span></div>
    	<div class="auto"><input type="checkbox" name="autoLogin"/>下次自动登录 <a href="#">忘记密码?</a></div>
    	<div class="re"><input type="button" name="submit" value="登录"/><span>没有账号，<a href="#">马上注册</a></span></div>
    </form>
    <div id="outerwrap"></div>
  </body>
</html>
