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
  	//var timer;
	//绑定右键事件
	$(".recycle li").rightContextMenu({items:[{text:"还原此项目",action:function(current){restore(current);}},{text:"彻底删除",action:function(current){deleteDone(current);}}]});  //为列表下的li添加功能
	$(".nonrecycle li").rightContextMenu({items:[{text:"下载",action:function(current){download(current);}},{text:"刷新",action:function(){refresh();}},{text:"分享",action:function(current){share(current);}},{text:"重命名",action:function(current){reName(current);}},{text:"删除",action:function(current){deleteContent(current);}}]});  //为列表下的li添加功能
	$(".share li").rightContextMenu({items:[{text:"下载",action:function(current){download(current);}},{text:"刷新",action:function(){refresh();}},{text:"取消分享",action:function(current){cancelShare(current);}},{text:"删除",action:function(current){deleteContent(current);}}]});  //为列表下的li添加功能，取消重命名功能，若需使用，则要更改数据库中分享记录的url字段即可
	

	alarm(username);   //调用alarm.js中的函数
	
	
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
	
	//显示日期
	$(".DateTimePicker").datepicker({
		"dateFormat" : "yy-mm-dd",
		'onSelect' : function(dataText , inst){
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
			  	  							alarm();
			  	  						}
			  	  					});
			  	  				}
			  	  			}
			  	  		};
			cur.dialog(dialogOpts);
		}
	});
	
	/******************Ajax异步请求获取数据,用于初始化树形菜单**********************/
  	$.ajax({
  		url:"Json",
  		type:"post",
  		dataType:"json",
  		data : {
  			"uid" : username
  		},
  		success:function(json){
  			$.each(json,function(i,value){
  				var childLi=$("<li></li>");
  				var childSpan=$("<span></span>");
  				childSpan.appendTo(childLi);
  				childLi.appendTo($("#PKM_Tree1").find("ul"));
  				childSpan.text(value.tagname);
  				childLi.data("treeid",value.id);
  				childLi.data("ilevel",value.level);
  				childLi.data("type",value.isparent);
  				childLi.data("url",value.url);
  				childLi.data("oldurl",value.oldurl);  					
  			});
  			$("#PKM_Tree1").find("li:first").click();
  		}
  	});
  	  	 
  	/******************当失去焦点时对文件夹或文件的名字合法性进行验证，以防止名字重复**********************/
  	$("#tree input").live("blur",function(){ 		
  		var curr_pt=this;
  		var currInfo=$(this).val();
  		var h_currInfo=$(this).next().val();
  		var dialogMessage="";
  		var error=false;
  		var currClosest=$(this).closest("li");
  		var currClosest_clone=currClosest.clone();
  		var b_curr=currClosest.dyTree.isParent(currClosest);
  		if(currInfo=="") {
  			dialogMessage="必须键入文件名";
  			error=true;
  		}else{
  			$.each(currClosest.siblings(),function(i,value){
  				var b_sibling=$(value).dyTree.isParent($(value));
  				var t_sibling=$(value).find("div.tl1").find("span:last").text();
  				if((b_curr && b_sibling)||(!b_curr && !b_sibling)){
  					if(t_sibling==currInfo){
  						dialogMessage="此文件已存在，是否要覆盖？";
  						error=true;
  					}
  				}
  			});
  		}
  		
  		if(error){
  			var dialogOpts="";
  			if(currInfo==""){
  				dialogOpts={
  		  				title:"命名",
  		  				modal:true,
  		  				open:function(){
  		  					$("div.ui-dialog .ui-dialog-content").html("<span>"+dialogMessage+"<span>");
  		  				},
  		  	  			buttons:{
  		  	  				"确定":function(){
  			  	  				currClosest_clone.dialog("destroy");
  			  	  				$(curr_pt).val(h_currInfo);
  			  	  				$(curr_pt).select();
  		  	  				}
  		  	  			}
  		  	  		};
  			}else{
  				dialogOpts={
  		  				title:"命名",
  		  				modal:true,
  		  				open:function(){
  		  					$("div.ui-dialog .ui-dialog-content").html("<span>"+dialogMessage+"<span>");
  		  				},
  		  	  			buttons:{
  		  	  				"是":function(){},
  		  	  				"否":function(){
  		  	  					currClosest_clone.dialog("destroy");
  		  	  					$(curr_pt).val(h_currInfo);
  		  	  					$(curr_pt).select();
  		  	  				}
  		  	  			}
  		  	  		};
  			}
  			currClosest_clone.dialog(dialogOpts);
  		}                               //若出现错误则弹出警告框，否则命名成功
  		else{
  			//$(this).parent().text(currInfo);
  			save(currClosest);  			
  		}
  	}); 
  	
  	/******************保存节点到数据库**********************/
  	function save(current){
  		var curParent=$(".dir-path").find("li:eq(1)").find("span:last");
  		var text=$(current).find("div.tl1").find("span:last").find("input[type='text']").val();
  		var oldtext=$(current).find("div.tl1").find("span:last").find("input[type='hidden']").val();
  		var isnew=$(current).data("isnew");
  		var type=$(current).data("type");
  		var tid=$(current).data("treeid");
  		var pid=curParent.data("treeid");
  		var level=curParent.data("ilevel")+1;
  		var url=curParent.data("url")+"\\"+text;
  		$.ajax({
  			//async:false,
  			type : "post",
  			url : "Save",
  			data:{
  				"text" : text,
  				"isnew" : isnew,
  				"type" : type,
  				"tid" : tid,
  				"pid" : pid,
  	  			"level" : level,
  	  			//"time" : time,
  	  			"url" : url,
  	  			"uid" : username
  			},
  			beforeSend : function(){
  				$.ajax({
  					async:false,
  					url : "FileOP",
  					type : "post",
  					data : {
  						"url" : url,
  						"isnew" : isnew,
  						"oldname" : oldtext
  					}
  				});
  			},
  			success : function(value){
  				$(current).data("treeid",value);
  				$(current).data("ilevel",curParent.data("ilevel")+1);
  				$(current).removeData("isnew");
  				//$(current).find("div.tl1").find("span:last").text(text);         
  				refresh();                                                      //保存成功之后。在页面显示信息
  			}
  		});
  	}
  	
  	/******************下载**********************/
  	function download(current){
  		current=$(current).closest("li");       //此操作后确保current是li元素
  		$.ajax({
  			url:"FileOP",
  	  		type:"post",
  	  		data : {
  	  			"url" : current.data("url"),
  	  			"download" : 1
  	  		}
  		});
  	}
  	
  	/******************分享**********************/
  	function share(current){
  		current=$(current).closest("li");       //此操作后确保current是li元素
  		$.ajax({
  			url:"Share",
  	  		type:"post",
  	  		data : {
  	  			"uid" : username,
  	  			"tid" : current.data("treeid"),
  	  			"level" : current.data("ilevel"),
  	  			"cancel" : "0"
  	  		},
  	  		success : function(data){
  	  			if(data != 1) alert("分享成功！");
  	  		}
  		});
  	}
  	
  	/******************取消分享**********************/
  	function cancelShare(current){
  		current=$(current).closest("li");       //此操作后确保current是li元素
  		$.ajax({
  			url:"Share",
  	  		type:"post",
  	  		data : {
  	  			"uid" : username,
  	  			"tid" : current.data("treeid"),
  	  			"level" : current.data("ilevel"),
  	  			"cancel" : "1"
  	  		},
  	  		success : function(data){
  	  			if(data != 1) {
  	  				alert("取消成功！");
  	  				refresh();
  	  			}
  	  		}
  		});
  	}
  	
  	/******************刷新**********************/
  	function refresh(){
  		var text = $("#PKM_Tree1 li.clicking").text();
  		var queryString;
  		if(text == "分享") queryString="uid="+username+"&tid="+$(".dir-path").find("li:eq(1)").find("span:last").data("treeid")+"&text="+text;
  		else queryString="uid="+username+"&tid="+$(".dir-path").find("li:eq(1)").find("span:last").data("treeid");
  		$.ajax({
  			url:"Json",
  	  		type:"post",
  	  		dataType:"json",
  	  		data:queryString,
  	  		success:function(json){
  	  			$("#PKM_Tree").children().remove();
				if(json!=""){
	  				var setting={};
	  				$("#PKM_Tree").dyTree.init(setting,$("#PKM_Tree"),json);
				}else{
					$("<span>暂无数据</span>").appendTo($("#PKM_Tree"));
				}
				$(".dir-path .dir-data").find("span").text($("#PKM_Tree").find("li").size());
  	  		}
  		});
  	}
  	
  	/******************重命名**********************/
  	function reName(current){
  		current=$(current).closest("li").find("div.tl1").find("span:last");
  		var currInfo=$(current).text();
  		$(current).text("");
  		$("<input type='text' value='' name='tagName' /><input type='hidden' value='' />").appendTo($(current));
  		$(current).children().val(currInfo).select();
  	}
  	  	
  	/******************删除**********************/
  	function deleteContent(current){
  		var dialogMessage="";
  		current=$(current).closest("li");       //此操作后确保current是li元素
  		var current_clone=current.clone();
  		if(current.dyTree.isParent(current)) dialogMessage="确定删除此文件夹及其下所有的文件？";
  		else dialogMessage="确定删除此文件？";
  		var dialogOpts={
  				title:"删除",
  				modal:true,
  				open:function(){
  					$("div.ui-dialog .ui-dialog-content").html("<span>"+dialogMessage+"<span>");
  				},
  	  			buttons:{
  	  				"确定":function(){
  	  					$.ajax({
  	  						url : "Delete",
  	  						type : "post",
  	  						data : {
  	  							"complete" : "0",
  	  							"tid" : $(current).data("treeid"),
  	  							"uid" : username,
  	  							"level" : $(current).data("ilevel")
  	  						},
	  	  					beforeSend : function(xhr){
			  	  				$.ajax({
			  	  					async:false,
			  	  					url : "FileOP",
			  	  					type : "post",
			  	  					data : {
			  	  						"url" : $(current).data("url"),
			  	  						"recycleUrl" : $("#PKM_Tree1").find("li:eq(2)").data("url"),
			  	  						"complete" : "0",
			  	  					},
			  	  					success : function(data){
			  	  						xhr.setRequestHeader("newUrl",encodeURI(data));
			  	  					}
			  	  				});
		  	  				},
		  	  				success : function(){
		  	  					//$(current).remove();
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
  	
  	/******************还原此项目**********************/
  	function restore(current){
  		current=$(current).closest("li");       //此操作后确保current是li元素
  		$.ajax({
				url : "Delete",
				type : "post",
				data : {
					"complete" : "2",
					"tid" : $(current).data("treeid"),
					"uid" : username,
					"level" : $(current).data("ilevel")
				},
				beforeSend : function(){
  	  				$.ajax({
  	  					async:false,
  	  					url : "FileOP",
  	  					type : "post",
  	  					data : {
  	  						"url" : $(current).data("url"),
  	  						"oldurl" : $(current).data("oldurl"),
  	  						"complete" : "2",
  	  					}
  	  				});
	  			},
				success : function(){
  	  				//$(current).remove();
					refresh();
				}
			});
  	}
  	
  	/******************彻底删除**********************/
  	function deleteDone(current){
  		current=$(current).closest("li");       //此操作后确保current是li元素
  		var current_clone=current.clone();
  		var dialogOpts={
  				title:"删除",
  				modal:true,
  				open:function(){
  					$("div.ui-dialog .ui-dialog-content").html("<span>确定删除吗？(此操作不可恢复)<span>");
  				},
  	  			buttons:{
  	  				"确定":function(){
	  	  				$.ajax({
	  	  				url : "Delete",
	  	  				type : "post",
	  	  				data : {
	  	  					"complete" : "1",
	  	  					"tid" : $(current).data("treeid"),
	  	  					"uid" : username,
	  	  					"level" : $(current).data("ilevel")
	  	  				},
	  	  				beforeSend : function(){
		  	  				$.ajax({
		  	  					async:false,
		  	  					url : "FileOP",
		  	  					type : "post",
		  	  					data : {
		  	  						"url" : $(current).data("url"),
		  	  						"complete" : "1",
		  	  					}
		  	  				});
	  	  				},
	  	  				success : function(){
	  	    	  			//$(current).remove();
	  	  					refresh();
	  	  	  	    		current_clone.dialog("destroy");
	  	  				}
	  	  			});
  	  				},
  	  				"取消":function(){current_clone.dialog("destroy");}
  	  			}
  	  		};
  		current_clone.dialog(dialogOpts);
  	}
  	  	  	
  	/******************#PKM_TREE1处效果**********************/
  	$("#PKM_Tree1 li:not('.clicking')").live({
  		mouseenter:function(){
  			$(this).css({"background":"#E3E3E3","cursor":"pointer"});
  		},
  		mouseleave:function(){
  			$(this).css({"background":"#F0F0F0"});
  		}
  	});
  	
  	$("#PKM_Tree1 li").live("click",function(){
  		$("#PKM_Tree1 li").css({"background":"#F0F0F0"}).removeClass("clicking");
  		$(this).css({"background":"#E3E3E3"}).addClass("clicking");
  		if($(this).text() != "收藏网址"){
  			$(".location li").removeClass("cl2").addClass("l2");
  			$(".f-header .c1 span").text("文件名");
  			$(".f-header .c2 span").text("大小");
  			$(".f-header .c3 span").text("修改时间");
	  		var treeid=$(this).data("treeid");
	  		var ilevel=$(this).data("ilevel");
	  		var type=$(this).data("type");
	  		var url=$(this).data("url");
	  		var oldurl=$(this).data("oldurl");
	  		var queryString="uid="+username+"&tid="+treeid+"&text="+$(this).text();
	  		var all=$(".dir-path").find("li:eq(1)");
	  		var allSpan=all.find("span:first");
	  		$.ajax({
	  	  		url:"Json",
	  	  		type:"post",
	  	  		dataType:"json",
	  	  		data:queryString,
	  	  		success:function(json){
	  	  			$("#PKM_Tree").children().remove();
					if(json!=""){
						var setting={};
						$("#PKM_Tree").dyTree.init(setting,$("#PKM_Tree"),json);
					}else{
						$("<span>暂无数据</span>").appendTo($("#PKM_Tree"));
					}
	  	  			$(".dir-path .dir-data").find("span").text($("#PKM_Tree").find("li").size());
	  	  		}
	  	  	});
	  		allSpan.removeClass("b-no-in").css("text-decoration","none");
	  		all.html(allSpan);
	  		$(".dir-path").find("li:first").children().remove();
	  		allSpan.data("treeid",treeid);	
	  		allSpan.data("ilevel",ilevel);
	  		allSpan.data("type",type);
	  		allSpan.data("url",url);
	  		allSpan.data("oldurl",oldurl);
  		}else{
  			$(".location li").removeClass("l2").addClass("cl2");
  			$(".f-header .c1 span").text("网址");
  			$(".f-header .c2 span").text("名称");
  			$(".f-header .c3 span").text("收藏时间");
  			$.ajax({
	  	  		url:"Collection",
	  	  		type:"post",
	  	  		dataType:"json",
	  	  		data : {
	  	  			"uid" : username,
	  	  			"cindex" : "0"
	  	  		},
	  	  		success:function(json){
	  	  			$("#PKM_Tree").children().remove();
					if(json!=""){
						var setting={scope : "collection"};
						$("#PKM_Tree").dyTree.init(setting,$("#PKM_Tree"),json);
					}else{
						$("<span>暂无数据</span>").appendTo($("#PKM_Tree"));
					}
	  	  			$(".dir-path .dir-data").find("span").text($("#PKM_Tree").find("li").size());
	  	  		}
	  	  	});
  		}
  		
  		if ($(this).text() == "回收站") {
  			$(".location").css("display","none");
	  		$("#PKM_Tree").removeClass("nonrecycle").removeClass("share").addClass("recycle");
	  	} else if($(this).text() == "分享"){
	  		$(".location").css("display","none");
	  		$("#PKM_Tree").removeClass("nonrecycle").removeClass("recycle").addClass("share");
	  	} else if($(this).text() == "收藏网址"){
	  		$(".location").css("display","block");
	  		$(".location div").css("display","none");
	  		//if($(".location .cl2").children().size() < 2) $("<img src='img/newcol.jpg' />").appendTo($(".location .cl2")).css("margin-left","10px");
	  		$("#PKM_Tree").removeClass("share").removeClass("recycle").removeClass("nonrecycle");
	  	} else{
	  		$(".location").css("display","block");
	  		$(".location div").css("display","block");
	  		//if($(".location .l2").children().size() >= 2) $(".location .l2 img:last").remove();
	  		$("#PKM_Tree").removeClass("share").removeClass("recycle").addClass("nonrecycle");
	  	}
  	});
  	
  	/******************返回上一级处效果**********************/
  	$(".dir-path .b-no-in").live({
  		mouseenter:function(){
  			$(this).css({"text-decoration":"underline","cursor":"pointer"});
  		},
  		mouseleave:function(){
  			$(this).css({"text-decoration":"none"});
  		}
  	});
  	
  	$(".dir-path .b-no-in").live("click",function(){
  		var text=$(this).text();
  		var text_1=$("#PKM_Tree1 li.clicking").text();
  		var q=null;
  		var treeid=null;
  		var queryString = null;
  		if(text=="返回上一级"){
  			q=$(".dir-path").find("li:eq(1)").find("span:last").prev().prev();
  		}else{
  			q=$(this);
  		}
  		treeid=q.data("treeid");
  		if(text_1 == "分享") queryString = "uid="+username+"&tid="+treeid + "&text=" + text_1;
  		else queryString="uid="+username+"&tid="+treeid;
  		$.ajax({
	  			url:"Json",
	  			type:"post",
	  			dataType:"json",
	  			data:queryString,
	  			success:function(json){
	  				$("#PKM_Tree").children().remove();
	  				if(json!=""){
	  					var setting={};
	  					$("#PKM_Tree").dyTree.init(setting,$("#PKM_Tree"),json);
	  				}else{
	  					$("<span>暂无数据</span>").appendTo($("#PKM_Tree"));
	  				}
	  				$(".dir-path .dir-data").find("span").text($("#PKM_Tree").find("li").size());
	  				if(q.text()=="全部文件") $(".dir-path").find("li:first").children().remove();
	  				q.removeClass("b-no-in").css("text-decoration","none").nextAll().remove();
	  			}
	  		});
  	});
  	
  	/******************当点击新建文件夹按钮时**********************/
  	$(".header-shaw .l2").live("click",function(){
  		var setting={
  			type:1,
  			isnew:1,
  			success:function(obj){
  		  		reName($(obj));
  			}
  		};
  		$("#PKM_Tree").dyTree.addNode(setting,$("#PKM_Tree"));
  	});
  	
  	$(".header-shaw .cl2 img").live("click",function(){
  		var collection = $("<div id='collection-form' class='ui-widget ui-widget-content ui-corner-all'>" +
  							"<form id='col-form'>" +
  							"<fieldset>" +
  							"<label>网址</label><input type='text' id='caddr' class='text ui-widget-content ui-corner-all' />" +
  							"<label>名称</label><input type='text' id='cname' class='text ui-widget-content ui-corner-all' />" +
  							"</fieldset>" +
  							"</form>" +
  							"</div>");
  		
  		var dialogOpts={
  	  				title:"收藏网址",
  	  				modal:true,
  	  	  			buttons:{
  	  	  				"确定":function(){
  	  	  					//修改外链
	  	  	  		  		var httpAddr = $("#collection-form #caddr").val();
	  	  	  		  		var i = httpAddr.indexOf("http://");
	  	  	  		  		if( i != 0 ) httpAddr = "http://" + httpAddr; 
	  	  	  		  		
  	  	  					$.ajax({
  	  	  						url : "Collection",
  	  	  						type : "post",
  	  	  						data : {
  	  	  							"uid" : username,
  	  	  							"caddr" : httpAddr,
  	  	  							"cname" : $("#collection-form #cname").val(),
  	  	  							"ctime" : formatDate(new Date()),
  	  	  							"cindex" : "1"
  	  	  						},
  	  	  						success : function(data){
  	  	  							if(data != 1) {
  	  	  								collection.dialog("destroy");
  	  	  								alert("收藏成功");
  	  	  								$("#PKM_Tree1 li:last").click();
  	  	  							}
  	  	  						}
  	  	  					});
  	  	  				},
  						"取消":function(){collection.dialog("destroy");}
  	  	  			}
  	  	  		};
  		collection.dialog(dialogOpts);
  	});
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
  		var sear_if   = $(this).parent().children("input[type=radio]:checked").val();
  		var error = "";
  		if(sear_info == "") error = "搜索的内容不能为空";
  		if(error == ""){
  			$.ajax({
  	  			type : "post",
  	  			url  : "Search",
  	  			dataType : "json",
  	  			data : {
  	  				"uid" : username,
  	  				"sear_con" : sear_info,
  	  				"sear_if"  : sear_if
  	  			},
  				success : function(json){					
  					$("#PKM_Tree").children().remove();
  	  				if(json!=""){
	  	  				var setting={};
	  	  				$("#PKM_Tree").dyTree.init(setting,$("#PKM_Tree"),json);
  	  				}else{
  	  					$("<span>暂无数据</span>").appendTo($("#PKM_Tree"));
  	  				}
  	  				$(".dir-path .dir-data").find("span").text($("#PKM_Tree").find("li").size());
  	  				/*var newSpan=$("<span></span>");
  	  				newSpan.data("treeid",treeid);
  	  				newSpan.data("ilevel",ilevel);
  	  				newSpan.data("type",type);
  	  				newSpan.data("url",url);
  	  				newSpan.data("oldurl",oldurl);
  	  				newSpan.text(currinfo).appendTo($(".dir-path").find("li:eq(1)"));
  	  				newSpan.prev().prev().addClass("b-no-in");*/ 	  				
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
  	
  	/******************PKM_TREE处效果**********************/
  	$("#tree li").live({
  		mouseenter:function(){
  			$(this).css({"background":"#F2F7FF"});
  		},
  		mouseleave:function(){
  			$(this).css({"background":"#FFFFFF"});
  		}
  	});
  	
  	$("#tree li").live("click",function(){
  		if(!$(this).find("input").size()){
	  		var treeid=$(this).data("treeid");
	  		var ilevel=$(this).data("ilevel");
	  		var type=$(this).data("type");
	  		var url=$(this).data("url");
	  		var oldurl=$(this).data("oldurl");
	  		//var isnew=$(this).data("isnew");
	  		var queryString="uid="+username+"&tid="+treeid;
	  		//var cAjax=$(this).data("cAjax");
	  		var currinfo=$(this).find("div.tl1").find("span:last").text();
	  		var ptext = $("#PKM_Tree1 li[class='clicking']").text();
	  		if (ptext != "收藏网址" && ptext != "回收站"){
	  			if(type != 0){
		  			//if(!cAjax){
			  			$.ajax({
			  				//async:false,
			  	  			url:"Json",
			  	  			type:"post",
			  	  			dataType:"json",
			  	  			data:queryString,
			  	  			//beforeSend:function(){
			  	  				//currSpan.text("正在展开...");
			  	  			//},
			  	  			success:function(json){
			  	  				//currSpan.text(currInfo);
			  	  				//$(this).data("cAjax","1");
			  	  				$("#PKM_Tree").children().remove();
			  	  				if(json!=""){
				  	  				var setting={};
				  	  				$("#PKM_Tree").dyTree.init(setting,$("#PKM_Tree"),json);
			  	  				}else{
			  	  					$("<span>暂无数据</span>").appendTo($("#PKM_Tree"));
			  	  				}
			  	  				$(".dir-path .dir-data").find("span").text($("#PKM_Tree").find("li").size());
			  	  				if(!$(".dir-path").find("li:first").children().size()) $("<span class='b-no-in'>返回上一级</span><span> |</span>").appendTo($(".dir-path").find("li:first"));
			  	  				$("<span>  >  </span>").appendTo($(".dir-path").find("li:eq(1)"));
			  	  				var newSpan=$("<span></span>");
			  	  				newSpan.data("treeid",treeid);
			  	  				newSpan.data("ilevel",ilevel);
			  	  				newSpan.data("type",type);
			  	  				newSpan.data("url",url);
			  	  				newSpan.data("oldurl",oldurl);
			  	  				//newSpan.data("isnew",isnew);
			  	  				newSpan.text(currinfo).appendTo($(".dir-path").find("li:eq(1)"));
			  	  				newSpan.prev().prev().addClass("b-no-in");
			  	  			}
			  	  		});
			  		//}
	  			}else{
	  				var i = currinfo.lastIndexOf(".");
	  				var suffix = currinfo.substr(i + 1);
	  				if(suffix == "doc" || suffix == "xls" || suffix == "ppt" || suffix == "pdf"){
	  					$.ajax({
	  						async : false,
	  						type  : "post",
	  						url   : "Convert",
	  						data  : {
	  							"url" : url
	  						},
	  						success : function(data){
	  							var viewerPlaceHolder = $("<div id='viewerPlaceHolder' style='position:absolute;display:block;width:800px;height:650px;z-index:1000;visibility:hidden;'></div>");
	  							var left=($(window).width()-viewerPlaceHolder.width())/2;
	  							var top=($(window).height()-viewerPlaceHolder.height())/2;
	  							viewerPlaceHolder.css({"left":left,"top":top});
	  							viewerPlaceHolder.appendTo(document.body);
	  							$("#outerwrap input").val(data);
	  							//setTimeout(function(){
	  								var i = data.lastIndexOf("\\");
		  							var swfFilePath = data.substr(i + 1);
	  								new FlexPaperViewer(	
		  									 'js/FlexPaper/FlexPaperViewer',
		  									 'viewerPlaceHolder', { config : {
		  									 SwfFile : escape("js/FlexPaper/" + swfFilePath),
		  									 Scale : 0.6, 
		  									 ZoomTransition : 'easeOut',
		  									 ZoomTime : 0.5,
		  									 ZoomInterval : 0.2,
		  									 FitPageOnLoad : true,
		  									 FitWidthOnLoad : false,
		  									 PrintEnabled : true,
		  									 FullScreenAsMaxWindow : false,
		  									 ProgressiveLoading : false,
		  									 MinZoomSize : 0.2,
		  									 MaxZoomSize : 5,
		  									 SearchMatchAll : false,
		  									 InitViewMode : 'Portrait',
		  									 
		  									 ViewModeToolsVisible : true,
		  									 ZoomToolsVisible : true,
		  									 NavToolsVisible : true,
		  									 CursorToolsVisible : true,
		  									 SearchToolsVisible : true,
		  			  						
		  			  						 localeChain: 'en_US'}
		  									 });
		  							$("#viewerPlaceHolder").css("visibility","visible");
		  							$("#outerwrap").css("visibility","visible");
	  							//},'1000');
	  							
	  						}
	  					});
	  				}
	  			}
	  		}
  		}
  	});
  	
  	
  	/******************outerwrap处效果**********************/
  	$("#outerwrap").click(function(){
  		$("#viewerPlaceHolder").remove();
		$("#outerwrap").css("visibility","hidden");
		$.ajax({
			type : "post",
			url  : "Del_temp",
			data : {
				"url" : $("#outerwrap input").val()
			}
		});
  	});
  	
  	/******************文件上传功能**********************/
  	$("#fileQueue").uploadify({ 
  		'swf': 'js/upload/uploadify.swf', 
  		'uploader': 'Uploadify', 
  		'queueID': 'fileQueue', 
  		'height' : '29',
  		'width' : '109',
  		'auto': true, 
  		'multi': false, 
  		'buttonText':'上传文件',
  		'method':'get',
  		'checkExisting' : 'Check_exist',
  		'onUploadStart' : function(file){
  			$("#fileQueue").uploadify("settings", "formData", { 
  				"url": encodeURI($(".dir-path").find("li:eq(1)").find("span:last").data("url")), 
  				"uid": username,
  				"pid": $(".dir-path").find("li:eq(1)").find("span:last").data("treeid"),
  				"level": $(".dir-path").find("li:eq(1)").find("span:last").data("ilevel") + 1,
  				});
  			},
  		'onUploadSuccess' : function(){
  			$("#fileQueue").uploadify("settings", "formData", { 
  				"replace" : "0"                                 //区分是否要替换
  				});
  			refresh();
  		}
  	});
  	
});