package info;

public class PKM_Schedule {
	
	public PKM_Schedule(){}
	
	private int id;
	private String schedulecontent;
	private String time;
	private String alarmtime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSchedulecontent() {
		return schedulecontent;
	}
	public void setSchedulecontent(String schedulecontent) {
		this.schedulecontent = schedulecontent;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAlarmtime() {
		return alarmtime;
	}
	public void setAlarmtime(String alarmtime) {
		this.alarmtime = alarmtime;
	}
}
