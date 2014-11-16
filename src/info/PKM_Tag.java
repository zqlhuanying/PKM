package info;

public class PKM_Tag {
	
	public PKM_Tag(){}
	
	private int id;
	private int isparent;
	private int level;
	private String size;
	private String tagname;
	private String url;
	private String oldurl;
	private String addtime;
	private String edittime;
	private int scheduleid;
	private String schedulecontent;
	private String time;
	private String alarmtime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIsparent() {
		return isparent;
	}
	public void setIsparent(int isparent) {
		this.isparent = isparent;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getSize() {
		return size;
	}
	public void setSize(long size) {
		// Get the size of the file
        long ifileSize = size;
        String suffix = "B";
        if (ifileSize > 1024) {
			ifileSize = Math.round(ifileSize / 1024);
			suffix   = "KB";
        }
		if (ifileSize > 1024) {
			ifileSize = Math.round(ifileSize / 1024);
			suffix   = "MB";
		}
		String sfileSize = String.valueOf(ifileSize);
		if (sfileSize.equals("0")) this.size = null;
		else {
			String[] fileSizeParts = sfileSize.split("\\.");
			sfileSize = fileSizeParts[0];
			if (fileSizeParts.length > 1) {
				sfileSize += '.' + fileSizeParts[1].substring(0,2);
			}
			sfileSize += suffix;
			this.size = sfileSize;
		}
	}
	public String getTagname() {
		return tagname;
	}
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOldurl() {
		return oldurl;
	}
	public void setOldurl(String oldurl) {
		this.oldurl = oldurl;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getEdittime() {
		return edittime;
	}
	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}
	public int getScheduleid() {
		return scheduleid;
	}
	public void setScheduleid(int scheduleid) {
		this.scheduleid = scheduleid;
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
