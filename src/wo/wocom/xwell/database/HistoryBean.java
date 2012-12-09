package wo.wocom.xwell.database;

/**
 * @author unknown
 * @improve wuwenjie wuwenjie.tk
 * @version 1.3.1
 * @more HistoryBean自定义，描述游览历史
 */
public class HistoryBean {
	public static final String ID = "_id";
	public static final String URL = "url";
	public static final String TIME = "time";
	public static final String NAME = "name";

	private String id;
	private String name;
	private String url;
	private int time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
