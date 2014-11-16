package info;

import java.sql.*;
import java.text.*;

import net.sf.json.JSONArray;

public class PJsonArray {
	
	private JSONArray json=null;
	
	public PJsonArray(){}
	
	public JSONArray getJSON(String sql,String scope){
		try{
			Connection coon=LinkToDB.linktodb();
		  	Statement stmt=coon.createStatement();
		  	//String sql="select * from PKM_Tag where ilevel="+"'"+"1"+"'";
		  	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		  	stmt.execute("use PKM");
		  	ResultSet result=stmt.executeQuery(sql);
		  	json=new JSONArray();
		  	int i=0;
		  	if(scope!=null){
		  		if("schedule".equals(scope)){
		  			while(result.next()){
		  				PKM_Schedule schedule=new PKM_Schedule();
		  				schedule.setId(result.getInt("scheduleid"));
		  				schedule.setSchedulecontent(result.getString("schedulecontent"));
		  				schedule.setTime(result.getString("time"));
		  				schedule.setAlarmtime(result.getString("alarmtime"));
				  		json.add(i, schedule);
				  		i++;
				  	}
		  		}else if("collection".equals(scope)){
		  			while(result.next()){
		  				PKM_Collection collection=new PKM_Collection();
		  				collection.setId(result.getInt("CollectionID"));
		  				collection.setCollectionaddress(result.getString("CollectionAddress"));
		  				collection.setCollectionname(result.getString("CollectionName"));
		  				collection.setCollectiontime(result.getString("CollectionTime"));
				  		json.add(i, collection);
				  		i++;
				  	}
		  		}
		  	}else{
		  		while(result.next()){
			  		PKM_Tag tag=new PKM_Tag();
			  		tag.setId(result.getInt("tagid"));
			  		tag.setIsparent(result.getInt("isparent"));
			  		tag.setLevel(result.getInt("ilevel"));
			  		tag.setSize(result.getInt("size"));
			  		tag.setTagname(result.getString("tagname"));
			  		tag.setUrl(result.getString("url"));
			  		tag.setOldurl(result.getString("oldurl"));
			  		Timestamp ss=result.getTimestamp("edittime");
			  		if(ss!=null) tag.setEdittime(sdf.format(ss));
			  		json.add(i, tag);
			  		i++;
			  	}
		  	}
		}catch(Exception e){e.printStackTrace();}
		return json;
	}
}
