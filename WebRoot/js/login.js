$(function(){
	//聚焦和失去焦点特效
	$("#login .inp").click(function(){
		$(this).find("input").focus();
	});
	$("#login .inp input").focus(function(){
			if($(this).val()=="") $(this).next().css("visibility","hidden");
	});
	$("#login .inp input").blur(function(){
		if($(this).val()=="") $(this).next().css("visibility","visible");
	});
	$("#login .re input[type=button]").click(function(){
		$(".error").remove();
		var action=$("form#login").attr("action");
		var username=$("form#login").find("input[name=username]").val();
		var password=$("form#login").find("input[name=password]").val();
		var formname=$("form#login").attr("name");
		var queryString="formname="+formname+"&username="+username+"&password="+password;
		var err="";
		if(username=="") err="用户名不能为空";
		else if(password=="") err="密码不能为空";
		if(err!="") $("<div class='error'></div>").text(err).insertBefore($("#login"));
		else{
			$.ajax({
				type:"post",
				url:action,
				data:queryString,
				success:function(error){
					if(error!="") $("<div class='error'></div>").text(error).insertBefore($("#login"));
					else location.href="index.jsp?username="+username;
				}
			});
		}
	});
	//弹出注册页面
	$("#login .re a").click(function(){
		$("<div id='outer'><div class='title'><span>用户注册</span><img alt='关闭' title='关闭' src='img/close.png'/></div>"+
    	"<div class='content'><form name='register' action='Validate' method='post'>"+
	    		"<div><span class='text'>用户名：</span><input type='text' name='username'/><input type='hidden' name='h_username' value='' is_submitting='0'/></div>"+
	    		"<div><span class='text'>密码：</span><input type='password' name='password'/><input type='hidden' name='h_password' value='' is_submitting='0'/></div>"+
	    		"<div><span class='text'>邮箱：</span><input type='text' name='email'/><input type='hidden' name='h_email' value='' is_submitting='0'/></div>"+
	    		"<div><span class='text'></span><input type='button' name='register' value='立即注册'/></div></form></div></div>").appendTo("body");
		$("#outerwrap").css("visibility","visible");
		$(window).resize();
		return false;
	});
	//浏览器窗口改变时，始终保持注册页面居中显示
	$(window).resize(function(){
		var left=($(window).width()-$("#outer").width())/2;
		var top=($(window).height()-$("#outer").height())/2;
		$("#outer").css({"visibility":"visible","z-index":"111","position":"absolute","left":left,"top":top});
	});
	//关闭注册页面
	$("#outer .title img").live("click",function(){
		$("#outerwrap").css("visibility","hidden");
		$("#outer").remove();
	});
	$("#outer .title img").live("mouseenter",function(){
		$(this).attr("src","img/close1.gif");
	}).live("mouseleave",function(){
		$(this).attr("src","img/close.png");
	});
	//ajax异步请求
	$("#outer .content input:not([type=button]):not([type=hidden])").live("blur",function(){
		var current=this;
		var error="";
		var name=$(this).attr("name");
		var action=$(this).parents("form").attr("action");
		var queryString="formname="+$(this).parents("form").attr("name");
		var h_input=$(this).nextAll("input[type=hidden]");//判断前后两次输入的内容是否相同，同时用is_submitting属性来标记是否处于提交中,从而取消多次的错误提醒
		var oldU=$.trim(h_input.val());
		var u=$.trim($(this).val());
		if(u==""||oldU!=u){
			$(this).next("span").remove();
			switch(name){
			case "username":
				if(u==""){ 
					error="用户名不能为空";
					break;
				}
				queryString=queryString+"&"+name+"="+u;
				/*$.ajaxSetup({async:false});//将ajax修改成同步，即不会出现多个线程
				$.post(
					action,queryString,function(result){
						//if(result!="") $("<span>"+result+"</span>").insertAfter($(current));
						error=result;
					}	
				);*/
				$.ajax({
					type:"post",
					url:action,	
					data:queryString,
					success:function(result){
						if(result!="") $("<span>"+result+"</span>").insertAfter($(current));
					},
					complete:function(){
						h_input.attr("is_submitting","0");
					}
				});
				h_input.attr("is_submitting","1");
				break;
			case "password":
				if(u==""){ 
					error="密码不能为空";
					break;
				}
				if(u.length<6){
					error="密码长度不能小于6个字符";
					break;
				}
				break;
			case "email":
				var reg=/^[a-zA-Z]([a-zA-Z0-9]*[-_.]?[a-zA-Z0-9]+)+@([\w-]+\.)+[a-zA-Z]{2,}$/;
				if(!reg.test(u)) error="请输入合法的Email地址";
				break;
			}
			if(error!="") $("<span>"+error+"</span>").insertAfter($(this));
			h_input.val(u);
		}
	});
	//对注册按钮做相应的处理，保证提交到后台数据的合法性
	$("#outer .content input[type=button]").live("click",function(){
		var current=this;
		var is_error=false;
		var form=$(this).parents("form");
		var action=form.attr("action");
		var queryString="formname="+form.attr("name")+"&"+$(this).attr("name")+"="+$(this).val()+"&"+form.serialize();
		var h_value=null;
		var span_value=null;
		var values=null;
		$("#outer .content input").each(function(){
			var h_input=$(this).nextAll("input[type=hidden]");
			if(h_input.attr("is_submitting")=="0") $(this).blur();
			if($(this).next("span").text()!="") {return false;};
		});
		h_value=$("#outer .content input[type=hidden]").attr("is_submitting");
		/*while(!is_error){
			var flag=false;
			$.map(h_value,function(value){
				if(value=="1") flag=true;
			});
			if(flag) h_value=$("#outer .content input[type=hidden]").attr("is_submitting");
			else is_error=true;
		}*/
		span_value=$("#outer .content span:not([class=text])").text();
		/*$.map(span_value,function(value){
			if(value!="") is_error=true;
		});*/
		values=[h_value,span_value];
		$.map(values,function(value){
			$.map(value,function(i_value){
				if(i_value!="0"&&i_value!="") is_error=true;
			});
		});
		if(!is_error) {
			$(current).attr("disabled","true");
			$.post(action,queryString,function(){
				$(current).removeAttr("disabled");
				alert("注册成功！");
			});
		}
	});
});