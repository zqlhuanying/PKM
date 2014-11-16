(function($){
	$.fn.rightContextMenu=function(options){
		var setting=$.extend({
			offsetX:0,
			offsetY:0,
			childSelector:this,
			items:[]
		},options||{});  //参数设置
		
		function createMenu(e){
			if(setting.items!=""){
				var menus=$("<div class='rcm_outer'><div class='rcm_middle'><div class='rcm_inner'><ul class='rcontextmenu'></ul></div></div></div>").appendTo(document.body);
				$.each(setting.items,function(i,item){
					if(item){
						var menu=$("<li></li>").appendTo(menus.find("ul"));
						item.text ? menu.text(item.text) : "";
						if(item.action){
							menu.click(function(){
								item.action(e.target);
								menus.remove();
							});
						}
					}
				});
				return menus;
			}
			else{return null;}
		}    //创建菜单

		//live方法不适合链式写法，在有些地方此插件便实现不了，故改成delegate方法，不过可能jquery的版本要稍微高点才可以使用
		/*$(document.body).delegate(setting.childSelector,"contextmenu",function(e){
			$(document.body).click();
			var m=createMenu(e);
			var left=e.pageX+setting.offsetX;
			var top=e.pageY+setting.offsetY;
			left=left+m.width()>$(window).width() ? (e.pageX-m.width()) : left;
			top=top+m.height()>$(window).height() ? (e.pageY-m.height()) : top;  //当菜单超出窗口边界时处理
			m.css({"position":"absolute","left":left,"top":top,"z-index":"1000"});

			m.find("li").hover(function(){
				$(this).css({"background":"#E3F4FC","cursor":"pointer"});
			},function(){
				$(this).css("background","none");
			});//鼠标在li上的hover事件
			
			$(document.body).bind("click contextmenu",function(){
				m.remove();
			});
			
			return false;
		});*/     //绑定右键事件
		this.live('contextmenu',function(e){
			var m=createMenu(e);
			if(m!=null){
				var left=e.pageX+setting.offsetX;
				var top=e.pageY+setting.offsetY;
				left=left+m.width()>$(window).width() ? (e.pageX-m.width()) : left;
				top=top+m.height()>$(window).height() ? (e.pageY-m.height()) : top;  //当菜单超出窗口边界时处理
				m.css({"position":"absolute","left":left,"top":top,"z-index":"1000"});
				
				m.find("li").hover(function(){
					$(this).css({"background":"#E3F4FC","cursor":"pointer"});
				},function(){
					$(this).css("background","none");
				});//鼠标在li上的hover事件
			
				$(window).bind("click contextmenu",function(){
					m.remove();
				});
			}
			return false;
		});  //绑定右键事件
		
		return this;   //返回包装集
	};
})(jQuery);