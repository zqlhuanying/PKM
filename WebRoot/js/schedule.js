$(function(){	
	//var username="101";                              //获取用户名
	var url = location.href;
	var i   = url.indexOf("?");
	var j   = url.indexOf("=", i + 1);
	var username = decodeURI(url.substring(j + 1));
	$("#nav .nr li:eq(1) span:first").text(username);
	$.ajax({
		type : "post",
		url  : "FindUI",
		async: false,
		data : {
			"username" : username,
		},
		success : function(data){
			username = data;
			$("#nav .nr li:eq(1) span:last").text(username);
		}
	});
	
	
	//绑定右键事件
	$("#PKM_Tree li").rightContextMenu({items:[{text:"修改",action:function(current){sch_update(current);}},{text:"删除",action:function(current){sch_deleteDone(current);}}]});  //为列表下的li添加功能

	alarm(username);   //调用alarm.js中的函数
	
	//显示日期
	$(".DateTimePicker").datepicker({
		"dateFormat" : "yy-mm-dd",
		'onSelect' : function(dataText , inst){
			$.ajax({
		  		url:"Json",
		  		type:"post",
		  		dataType:"json",
		  		data : {
		  			"scope" : "schedule",
		  			"uid" : username,
		  			"scheduletime" : dataText
		  		},
		  		success:function(json){
		  			$("#PKM_Tree").children().remove();
					if(json!=""){
						var setting={scope:"schedule"};
						$("#PKM_Tree").dyTree.init(setting,$("#PKM_Tree"),json);
					}else{
						$("<span>暂无数据</span>").appendTo($("#PKM_Tree"));
					}
			  			$(".dir-path .dir-data").find("span").text($("#PKM_Tree").find("li").size());
		  		}
		  	});
		}
	});
	
	/******************Ajax异步请求获取数据,用于初始化树形菜单**********************/
  	$(".ui-state-highlight").click();
  	
  	
  	
  	$(".location .l2").click(function(){
  		var dataText = formatDate(new Date());
  		var cur = $("<textarea name='schedule' cols=38 rows=5 id='schedule'></textarea>");	
		var dialogOpts={
		  				title:"新建日记",
		  				modal:true,
		  				scope : "schedule",
		  				open : function(){
		  					alterDialog();
		  				},
		  	  			buttons:{
		  	  				"保存":function(){
		  	  					var hourSelected = $(".ui-dialog-buttonpane .hourSelect").val();
		  	  					var minSelected  = $(".ui-dialog-buttonpane .minSelect").val();
		  	  					$.ajax({
		  	  						type : "post",
		  	  						url  : "Schedule",
		  	  						data : {
		  	  							change : "0",
		  	  							uid : username,
		  	  							content : $(".ui-dialog #schedule").val(),
		  	  							time : dataText,
		  	  							alarmtime : hourSelected + ":" + minSelected
		  	  						},
		  	  						success : function(){
		  	  							cur.dialog("destroy");
		  	  							clearInterval(timer);
		  	  							alarm(username);
		  	  							refresh();
		  	  						}
		  	  					});
		  	  				}
		  	  			}
		  	  		};
		cur.dialog(dialogOpts);
  	});
  	
  	function alterDialog(){
  		var select = $("<div class='schedule_div'></div>");
		var alarmText = $("<span>提醒设置：&nbsp;&nbsp;</span>");
		var hourSelect = $("<select class='hourSelect'></select>");
		var minSelect = $("<select class='minSelect'></select>");
			
		for(var i=0;i<24;i++){
			if(i<10) value="0"+i;
			else value=i;
			$("<option>"+value+"</option>").appendTo(hourSelect);
		}
		for(var i=0;i<60;i++){
			if(i<10) value="0"+i;
			else value=i;
			$("<option>"+value+"</option>").appendTo(minSelect);
		}
			
		alarmText.appendTo(select);
		hourSelect.appendTo(select);
		$("<span>:&nbsp;&nbsp;</span>").appendTo(select);
		minSelect.appendTo(select);
		select.appendTo( $(".ui-dialog-buttonpane") );
  	}
  	
  	function refresh(){
  		var dataText = formatDate(new Date());
  		$.ajax({
	  		url:"Json",
	  		type:"post",
	  		dataType:"json",
	  		data : {
	  			"scope" : "schedule",
	  			"uid" : username,
	  			"scheduletime" : dataText
	  		},
	  		success:function(json){
	  			$("#PKM_Tree").children().remove();
				if(json!=""){
					var setting={scope:"schedule"};
					$("#PKM_Tree").dyTree.init(setting,$("#PKM_Tree"),json);
				}else{
					$("<span>暂无数据</span>").appendTo($("#PKM_Tree"));
				}
		  			$(".dir-path .dir-data").find("span").text($("#PKM_Tree").find("li").size());
	  		}
	  	});
  	}
  	
  	function sch_update(current){
  		current=$(current).closest("li");       //此操作后确保current是li元素
  		var dataText = formatDate(new Date());
  		var cur = $("<textarea name='schedule' cols=38 rows=5 id='schedule'></textarea>");	
		var dialogOpts={
		  				title:"更新日记",
		  				modal:true,
		  				scope : "schedule",
		  				open : function(){
		  					alterDialog();
		  					$(".ui-dialog #schedule").val(current.find("div.tl1").text());
		  					var alarmtime = current.find("div.tl2").text().split(":");
		  					$(".hourSelect").val(alarmtime[0]);
		  					$(".minSelect").val(alarmtime[1]);
		  				},
		  	  			buttons:{
		  	  				"保存":function(){
		  	  					var hourSelected = $(".ui-dialog-buttonpane .hourSelect option:selected").val();
		  	  					var minSelected  = $(".ui-dialog-buttonpane .minSelect option:selected").val();
		  	  					$.ajax({
		  	  						type : "post",
		  	  						url  : "Schedule",
		  	  						data : {
		  	  							change : "1",
		  	  							uid : username,
		  	  							sid : $(current).data("treeid"),
		  	  							content : $(".ui-dialog #schedule").val(),
		  	  							time : dataText,
		  	  							alarmtime : hourSelected + ":" + minSelected
		  	  						},
		  	  						success : function(){
		  	  							cur.dialog("destroy");
		  	  							clearInterval(timer);
		  	  							alarm(username);
		  	  							refresh();
		  	  						}
		  	  					});
		  	  				}
		  	  			}
		  	  		};
		cur.dialog(dialogOpts);
  	}
  	
  	/******************删除**********************/
  	function sch_deleteDone(current){
  		var dialogMessage="确定删除此日记？";
  		current=$(current).closest("li");       //此操作后确保current是li元素
  		var current_clone=current.clone();
  		var dialogOpts={
  				title:"删除日记",
  				modal:true,
  				open:function(){
  					$("div.ui-dialog .ui-dialog-content").html("<span>"+dialogMessage+"<span>");
  				},
  	  			buttons:{
  	  				"确定":function(){
  	  					$.ajax({
  	  						url : "Schedule",
  	  						type : "post",
  	  						data : {
  	  							"change" : "2",
  	  							"sid" : $(current).data("treeid"),
  	  							"uid" : username,
  	  						},
		  	  				success : function(){
		  	  					refresh();
		  	  				}
  	  					});
		  	    		current_clone.dialog("destroy");
  	  				},
  	  				"取消":function(){current_clone.dialog("destroy");}
  	  			}
  	  		};
  		current_clone.dialog(dialogOpts);
  	}
  	
  	/******************菜单处效果**********************/
  	$("#nav .nl li").hover(function(){
  		$(this).css({"background":"#0F0F0F"});
  	},function(){
  		$(this).css({"background":"#252525"});
  	});
  	
  	$("#nav .nr .more").parent().hover(function(){
  		$("#nav .nr .more").css({"display":"block"});
  	});
  	$("#nav .nr .more").parent().mouseleave(function(){
  		$("#nav .nr .more").css({"display":"none"});
  	});
  	
  	$("#nav .nr .more li").hover(function(){
  		$(this).css({"background":"#E3F4FC"});
  	},function(){
  		$(this).css({"background":"#F6F6F9"});
  	});
  	
  	$("#nav .nr input.sear-info").focus(function(){
  		$(this).nextAll("span").css("display","none");
  	});
  	
  	$("#nav .nr span.input-placeholder").click(function(){
  		$(this).prevAll("input.sear-info").focus();
  	});
  	
  	$("#nav .nr input.sear-info").blur(function(){
  		$(this).val() ? $(this).val() : $(this).nextAll("span").css("display","block");
  	});
  	
  	$("#nav .nr div.more li").click(function(){
  		window.location.href = "login.jsp";
  	});
  	
  	$("#nav .nr img.sear").click(function(){
  		var sear_info = $(this).prev().val();
  		var error = "";
  		if(sear_info == "") error = "搜索的内容不能为空";
  		if(error == ""){
  			$.ajax({
  	  			type : "post",
  	  			url  : "Search",
  	  			dataType : "json",
  	  			data : {
  	  				"uid"      : username,
  	  				"sear_con" : sear_info,
  	  				"scope"    : "schedule"
  	  			},
  				success : function(json){
  					$("#PKM_Tree").children().remove();
  	  				if(json!=""){
  	  					var setting={scope:"schedule"};
	  	  				$("#PKM_Tree").dyTree.init(setting,$("#PKM_Tree"),json);
  	  				}else{
  	  					$("<span>暂无数据</span>").appendTo($("#PKM_Tree"));
  	  				}
  	  				$(".dir-path .dir-data").find("span").text($("#PKM_Tree").find("li").size());
  				}
  	  		});
  		}else{
  			var dialog = $("<span>"+error+"</span>");
  			var dialogOpts={
  	  				title:"提示框",
  	  				modal:true,
  	  	  			buttons:{
  	  	  				"确定":function(){dialog.dialog("destroy");}
  	  	  			}
  	  	  		};
  			dialog.dialog(dialogOpts);
  		}
  		
  	});
});