//$(function(){
	//var username="101";                              //获取用户名
	var timer;
	
	function formatDate(date) {
        var ret = date.getFullYear() + "-";
        ret += ("00" + (date.getMonth() + 1)).slice(-2) + "-";
        ret += ("00" + date.getDate()).slice(-2);
        return ret;
	}
	 function formatTime(date) {
	 	var ret = ("00" + date.getHours()).slice(-2) + ":";
	     ret += ("00" + date.getMinutes()).slice(-2);
	     return ret;
	 }
	var alarm = function(username){
		$.ajax({
			url : "Alarm",
			type : "post",
			data : {
				"uid" : username,
				"time" : formatDate(new Date()),
				"alarmtime" : formatTime(new Date())
			},
			success : function(data){
				if(data){
					var time = function() {
						if(formatTime(new Date()) == data){
							var cur = $("<span id='alarm'>您今天还有事情没有完成哦！<a href='schedule.jsp?username="+$('#nav .nr li:eq(1) span:first').text()+"'>点击查看</a></span>");
							var dialogOpts={
					  				title:"提醒"
					  	  		};
							cur.dialog(dialogOpts);
							clearInterval(timer);
							alarm(username);
						}
					};
					timer = setInterval(time,"1000");
				}
			}
		});
	};
	//alarm();
  	
//});