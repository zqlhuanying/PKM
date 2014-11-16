(function($){
	var _setting = {
		treeid:null,
		text:null,
		time:null,
		alarmtime:null,
		type:0,
		isnew:0,
		scope:"home",
		addr:null,
		size:null,
		level:null,
		url:null,
		oldurl:null,
		success:null
	};  //参数设置
	
	var tools = {
		/*reAjust : function(){
			$("#tree ul li:last-child").addClass("li_background");
	  		$("#tree ul li:not(:last-child)").removeClass("li_background");
		},*/
		defaultText : function(current){
			var i=0;
			var m = [];
			var text=null;
			var b_curr=$(current).dyTree.isParent($(current));
			if(b_curr) text="知识夹";
			else text="新建文章";
			$.each($(current).siblings(),function(j,value){
				var t_siblings=$(value).text();
				var b_siblings=$(value).dyTree.isParent($(value));
				if(b_curr && b_siblings){
					if(t_siblings.indexOf(text)>-1) {
						var k = t_siblings.indexOf("(");
						var l = t_siblings.indexOf(")");
						if( k < 0 ) m[i] = 0;
						else m[i] = t_siblings.substr(k + 1,l - k -1);
						i++;
					}
				}
				if(!b_curr && !b_siblings){
					if(t_siblings.indexOf(text)>-1) {
						if(t_siblings.indexOf(text)>-1) {
							var k = t_siblings.indexOf("(");
							var l = t_siblings.indexOf(")");
							if( k < 0 ) m[i] = 0;
							else m[i] = t_siblings.substr(k + 1,l - k -1);
							i++;
						}
					}
				}
			});
			i = 0;
			m.sort(function(a,b){return a-b;});
			$.each(m,function(j,value){
				if( i != value) return;
				i++;
			});
			if(i==0) return text;
			else return text+"("+i+")";
		}
	};
	$.fn.dyTree = {
		/***********初始化**********/
		init : function(setting,current,json){
			var treediv=$("<div id='tree'></div>");
			var rootul=$("<ul class='root_tree_ul'></ul>");
			rootul.appendTo(treediv);
			treediv.appendTo($(current));
			$.fn.dyTree.appendNodes(setting,treediv,json);
			return treediv;
		},
		/************增加子节点****************/
		addNode : function(setting,current){         //type标记增加的是父节点还是叶子节点
			var vsetting={};
			$.extend(vsetting,_setting);
			$.extend(vsetting,setting);
			//var child=$("<li><span></span></li>"); 
			var child=$("<li></li>");
			var chdiv1=$("<div class='tl1'><span></span><span></span></div>");
			var chdiv2=$("<div class='tl2'><span></span></div>");
			var chdiv3=$("<div class='tl3'><span></span></div>");
			chdiv1.appendTo(child);
			chdiv2.appendTo(child);
			chdiv3.appendTo(child);
			if(!$(current).find("ul").children().size()){
				$(current).children().remove();
				var treediv=$("<div id='tree'></div>");
				var rootul=$("<ul class='root_tree_ul'></ul>");
				rootul.appendTo(treediv);
				treediv.appendTo($(current));
			}
			child.appendTo($(current).find("ul"));
			//把重要的信息保存在节点上方便后续操作
			/*if(vsetting.scope=="schedule"){
				child.data("treeid",vsetting.treeid);
			}else{
				child.data("treeid",vsetting.treeid);
				child.data("ilevel",vsetting.level);
				child.data("type",vsetting.type);
				child.data("url",vsetting.url);
				child.data("oldurl",vsetting.oldurl);
				if(vsetting.isnew=="1") child.data("isnew",vsetting.isnew);
			}*/
			child.data("treeid",vsetting.treeid);
			if(vsetting.scope=="home"){
				child.data("ilevel",vsetting.level);
				child.data("type",vsetting.type);
				child.data("url",vsetting.url);
				child.data("oldurl",vsetting.oldurl);
				if(vsetting.isnew=="1") child.data("isnew",vsetting.isnew);
			}
			
			if(vsetting.type==1) {
				/*var child_div=$("<div></div>");
			  	var child_ul=$("<ul></ul>");
			  	child_div.addClass("hitarea");
			  	child_div.addClass("hitarea_bg_fold");
			  	child_div.insertBefore(child.find("span"));
			  	child_ul.appendTo(child);
			  	child.addClass("folder");*/
				chdiv1.find("span:first").addClass("folder");
			}
		  	if(vsetting.type==0) chdiv1.find("span:first").addClass("file");;
			/*if(!$.fn.dyTree.isParent(current)){
			  	child.appendTo($(current).children("ul"));
			}                       //如果没有子节点，则需将此节点修改为父节点，再在此节点下新增节点
			else{
			  	child.appendTo($(current).children("ul"));
			}*/
		  	if(vsetting.scope=="schedule"){
		  		chdiv1.find("span:last").text(vsetting.text);
				chdiv2.find("span").text(vsetting.alarmtime);
				chdiv3.find("span").text(vsetting.time);
		  	}else if(vsetting.scope=="collection"){
		  		chdiv1.find("span:last").html("<a href='" + vsetting.addr + "' target='_blank' >" + vsetting.addr + "</a>");
				chdiv2.find("span").text(vsetting.text);
				chdiv3.find("span").text(vsetting.time);
		  	}else{
		  		if(!vsetting.text) vsetting.text=tools.defaultText(child);
				chdiv1.find("span:last").text(vsetting.text);
				if(!vsetting.size) vsetting.size="-";
				chdiv2.find("span").text(vsetting.size);
				if(!vsetting.time) vsetting.time="-";
				chdiv3.find("span").text(vsetting.time);
		  	}
			//chdiv3.find("span").text(vsetting.);
			//child.find("span").addClass("span_bg_f_fold").text(vsetting.text);
			//if(vsetting.treeid) child.data("id",vsetting.treeid);
			//$(current).children("div.hitarea").removeClass("hitarea_bg_fold").addClass("hitarea_bg_unfold");  
			//$(current).children("span").removeClass("span_bg_f_fold").addClass("span_bg_f_unfold");
			//tools.reAjust();
			if(vsetting.success) vsetting.success(child);
			return child;
		},
		/*addChild : function(setting,current,type){         //type标记增加的是父节点还是叶子节点
			var vsetting={};
			$.extend(vsetting,_setting);
			$.extend(vsetting,setting);
			var child_ul=$("<ul></ul>");
		  	var child=$("<li><span></span></li>");  
		  	if(type==1) child.addClass("folder");
	  		if(type==0) child.addClass("file");
		  	if(!$.fn.dyTree.isParent(current)){
		  		var currInfo=$(current).text();
		  		var updateStru=$("<div></div>");
		  		var updateStru_span=$("<span></span>");
		  		updateStru_span.text(currInfo);
		  		$(current).text("");
		  		updateStru.addClass("hitarea");
		  		updateStru.appendTo($(current));
		  		updateStru_span.appendTo($(current));
		  		child.appendTo(child_ul);
		  		child_ul.appendTo($(current));
		  	}                       //如果没有子节点，则需将此节点修改为父节点，再在此节点下新增节点
		  	else{
		  		child.appendTo($(current).children("ul"));
		  	}
		  	if(!vsetting.text) vsetting.text=tools.defaultText(child);
		  	child.find("span").text(vsetting.text);
		  	tools.reAjust();
		},*/	
		/***********追加节点**********/
		appendNodes : function(setting,current,json){
			if(setting.scope=="schedule"){
				$.each(json,function(i,value){
					setting.text=value.schedulecontent;
					setting.treeid=value.id;
					setting.time=value.time;
					setting.alarmtime=value.alarmtime;
					$.fn.dyTree.addNode(setting,current);
				});
			}else if(setting.scope == "collection"){
				$.each(json,function(i,value){
					setting.treeid=value.id;
					setting.text=value.collectionname;
					setting.addr=value.collectionaddress;
					setting.time=value.collectiontime;
					$.fn.dyTree.addNode(setting,current);
				});
			}else{
				$.each(json,function(i,value){
					setting.text=value.tagname;
					setting.type=value.isparent;
					setting.treeid=value.id;
					setting.level=value.level;
					setting.size=value.size;
					setting.url=value.url;
					setting.oldurl=value.oldurl;
					setting.time=value.edittime;
					$.fn.dyTree.addNode(setting,current);
				});
			}
			
			if(setting.success) setting.success(current);
			return current;
		},
		/***********删除节点**********/
		/*deleteNode : function(current){
			$(current).remove();
			if(setting.success) setting.success(current);
		},*/
		/***********判断当前节点是否为父节点**********/
		isParent : function(current){
			if($(current).data("type")=="1") return true;
			else return false;
		},
		/******************获取level**********************/
		/*getLevel : function(current){
			var i=0;
			var currParent=current;
			do{
				i++;
				currParent=$(currParent).parent().closest("ul");
				currClass=$(currParent).attr("class") ? $(currParent).attr("class") : "";
			}while(currClass=="")
			return i;
		},*/
	};
})(jQuery);