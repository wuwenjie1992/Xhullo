package wo.wocom.xwell;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import wo.wocom.xwell.net.GetHtml2Str;
import wo.wocom.xwell.utility.Util_Notifiy;
import wo.wocom.xwell.utility.XA_util_ADialog;
import wo.wocom.xwell.utility.startACIntent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.10
 * @more 自定义列表模式；联网；解析JASON； 联网、解析的耗时操作使用handler;
 * 			实现天气预报和实时预报;使用Org.json解析
 */

public class XhulooActivity_weatherreport extends Activity {

	private static final String TAG = "WR_Xhuloo";

	myListViewAdapter my_LVA;// 自定义LISTVIEW

	String[][] newtext = new String[7][5];
	String title = "联网数据..";
	String get_url_weaReport = "http://cnweather.herokuapp.com/cnweather?cityid=101020300"; // 未来天气
	String get_url_weaAlarm = "http://cnweather.herokuapp.com/alarm?cityid=101020300";// 天气预警
	String get_url_weaNow = "http://cnweather.herokuapp.com/now?cityid=101020300";// 实时天气
	// 省份代码：http://www.weather.com.cn/data/city3jdata/china.html
	// 城市代码：http://www.weather.com.cn/data/city3jdata/station/1010200.html
	MyHandler myHandler;
	weatherinfo wi; // 声明 天气预报类
	weatherNow wn; // 声明 实时天气类
	int i, j;
	boolean hasnotify = false;

	/* activity生命周期 */
	public void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "WR_onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wr); // 设置主布局

		// 初始化---------------
		wi = new weatherinfo();// 天气预报类
		wn = new weatherNow();// 实时天气类

		// List<String[]>,以输出结果
		for (j = 0; j <= 6; j++) {
			for (i = 0; i <= 4; i++) {
				newtext[j][i] = title;
			}
		}

		myHandler = new MyHandler();
		// 创建新的Handler实例
		// 绑定到当前线程和消息的队列中,开始分发数据
		MyThread m = new MyThread();
		new Thread(m).start();

		showMyLV(true);

	}// oncreate

	public void showMyLV(boolean i) {
		ListView listView = (ListView) this.findViewById(R.id.wr_lv);
		my_LVA = new myListViewAdapter(7); // 自定义的列表试图适配器
		listView.setAdapter(my_LVA);
		if (i)
			my_LVA.notifyDataSetChanged();// 通知数据改变，反应该刷新视图
	}

	// 自定义LISTVIEW适配器
	public class myListViewAdapter extends BaseAdapter {

		View[] itemViews;

		public myListViewAdapter(int j) {

			itemViews = new View[j]; // 有jjjj个图

			for (i = 0; i < j; i++) { // j==6

				itemViews[i] = makeItemView(newtext[i][0], newtext[i][1],
						newtext[i][2], newtext[i][3], newtext[i][4]);

			} // 调用makeItemView自定义函数，来组织界面，返回对象
		}

		public int getCount() {
			return itemViews.length;
		}

		public View getItem(int position) {
			return itemViews[position];
		}

		public long getItemId(int position) {
			return position;
		}

		private View makeItemView(String strweek, String strweather,
				String temp, String wind, String advise) {

			LayoutInflater inflater = (LayoutInflater) XhulooActivity_weatherreport.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// 使用View的对象itemView与list_item.xml关联
			View itemView = inflater.inflate(R.layout.list_item, null);

			// 通过findViewById()方法实例R.layout.item内各组件
			TextView strweek_tv = (TextView) itemView
					.findViewById(R.id.strweek_tv);
			strweek_tv.setText(strweek);

			TextView strweather_tv = (TextView) itemView
					.findViewById(R.id.strweather_tv);
			strweather_tv.setText(strweather);

			TextView temp_tv = (TextView) itemView.findViewById(R.id.temp_tv);
			temp_tv.setText(temp);

			TextView wind_tv = (TextView) itemView.findViewById(R.id.wind_tv);
			wind_tv.setText(wind);

			TextView advise_tv = (TextView) itemView
					.findViewById(R.id.advise_tv);
			advise_tv.setText(advise);

			return itemView;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				return itemViews[position];
			return convertView;
		}

	} // myListViewAdapter

	// //////////////////////////////

	// 接收,处理消息,Handler与当前主线程一起运行
	public class MyHandler extends Handler {

		public MyHandler() {
		}

		public MyHandler(Looper L) {
			super(L);
		}

		// 子类必须重写此方法,接受数据
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			// 传递信息以更新UI
			Bundle b = msg.getData();

			switch (b.getInt("DONE")) {
			case 0:
				showDialog("问题", "网络不达", "好的");
				break;

			case 1:

				// weatherNow------------------------------
				showDialog(
						wn.getcity() + "实时天气 " + "发布于" + wn.gettime(),
						"气温：" + wn.gettemp() + "\n风向：" + wn.getwind()
								+ "\n风力：" + wn.getwindPower() + "\n相对湿度："
								+ wn.getRelativeHumidity(), "知道了");

				// weatherReport------------------------------
				title = wi.getcity() + " " + wi.getdate_y() + "\t" + "发布于"
						+ wi.getfchh() + ":00";
				XhulooActivity_weatherreport.this.setTitle(title);// 设置标题

				// 顺序以list_item.xml为准，设置温度，天气,风
				for (i = 0; i <= 6; i++) {
					newtext[i][0] = wi.getweek(i);// 获得星期
					newtext[i][2] = wi.gettemp(i);
					newtext[i][1] = wi.getweather(i);
					newtext[i][3] = wi.getwind(i);
					newtext[i][4] = wi.getadvise();// 获得建议
				}

				showMyLV(true);// notify changed 刷新视图

				break;
			}// SWITCH end

			// 预警信息的UI革新
			if (hasnotify) {
				Util_Notifiy nf = new Util_Notifiy(
						XhulooActivity_weatherreport.this);

				nf.Util_simpleNotifiy(XhulooActivity_weatherreport.this,
						XhulooActivity_weatherreport.class, "预警",
						get_url_weaAlarm);
			}
		}// handleMessage end

	}// myHandler end

	public class MyThread implements Runnable {
		public void run() {
			Log.i(TAG, "MyThread_start");
			Looper.prepare();
			String html_s = null; // 网页字符串
			// 向Handler发送消息,更新UI
			Message msg = new Message();
			Bundle b = new Bundle();// 存放数据

			JSONObject info;
			html_s = GetHtml2Str.reStr(html_s, get_url_weaNow,
					XhulooActivity_weatherreport.this);
			// 联网处理数据,返回字符串

			try {
				info = new JSONObject(html_s);

				if (html_s != null) {
					// weatherNow------------------------------

					JSONObject info_ja = info.getJSONObject("result")
							.getJSONObject("sk_info");

					wn.setcity(info_ja.getString("cityName"));
					wn.setRelativeHumidity(info_ja.getString("sd"));
					wn.settemp(info_ja.getString("temp"));
					wn.settime(info_ja.getString("time"));
					wn.setwind(info_ja.getString("wd"));
					wn.setwindPower(info_ja.getString("ws"));

					// weatherReport------------------------------
					html_s = GetHtml2Str.reStr(html_s, get_url_weaReport,
							XhulooActivity_weatherreport.this);// 联网处理数据,返回字符串

					info = new JSONObject(html_s);

					wi.setcity(info.getString("cityname"));
					wi.setdate_y(info.getString("publish_date").substring(0, 8));

					wi.setfchh(info.getJSONObject("date_detail").getString(
							"timehours"));
					wi.setadvise("");

					// 初始化,ArrayList<String> temp;
					for (i = 0; i <= 6; i++) {
						wi.addweek("temp" + i);
						wi.addtemp("temp" + i);
						wi.addweather("wea" + i);
						wi.addwind("win" + i);
					}

					// 处理气温，天气，风
					for (i = 0; i <= 6; i++) {
						wi.setweek(info.getJSONArray("weekArr").getString(i), i);
						wi.settemp(info.getJSONArray("temp1").getString(i)
								+ "->"
								+ info.getJSONArray("temp2").getString(i), i);
						wi.setweather(
								info.getJSONArray("weather").getString(i), i);
						wi.setwind(info.getJSONArray("wind").getString(i), i);
					}

					// weather alarm---------------------------
					html_s = null; // 置空
					html_s = GetHtml2Str.reStr(html_s, get_url_weaAlarm,
							XhulooActivity_weatherreport.this);// 联网处理数据,返回字符串
					info = new JSONObject(html_s);

					if (info.getInt("code") == 0) {
						hasnotify = false;
					} else {
						hasnotify = true;
						get_url_weaAlarm = html_s;
					}

					// 向Handler发送消息,更新UI
					b.putInt("DONE", 1);// 1表示成功；可用putString
					msg.setData(b);

					XhulooActivity_weatherreport.this.myHandler
							.sendMessage(msg);

				}// if end
				else {
					// 向Handler发送消息,更新UI
					b.putInt("DONE", 0);// 0表示不成功
					msg.setData(b);
					XhulooActivity_weatherreport.this.myHandler
							.sendMessage(msg);
				}

			} catch (JSONException e) {
				// 向Handler发送消息,更新UI
				b.putInt("DONE", 0);// 0表示不成功
				msg.setData(b);
				XhulooActivity_weatherreport.this.myHandler.sendMessage(msg);
				e.printStackTrace();
			}

		}// run end
	} // my thread end

	/**
	 * @see XA_util_ADialog
	 * @param ti
	 * @param msg
	 * @param bnt
	 */
	public void showDialog(String ti, String msg, String bnt) {
		XA_util_ADialog alog = new XA_util_ADialog(
				XhulooActivity_weatherreport.this);
		alog.show1ADialog(ti, msg, bnt);
	}

	// ////////////////////////
	// 自定义 weatherAlarm

	// 自定义 weatherNow
	public class weatherNow {

		private String city;
		private String time;
		private String temp;
		private String wind;
		private String windPower;
		private String RelativeHumidity;

		public String getcity() {
			return city;
		}

		public void setcity(String ci) {
			this.city = ci;
		}// city

		public String gettime() {
			return time;
		}

		public void settime(String ti) {
			this.time = ti;
		}// time

		public String gettemp() {
			return temp;
		}

		public void settemp(String te) {
			this.temp = te;
		}// temp

		public String getwind() {
			return wind;
		}

		public void setwind(String wi) {
			this.wind = wi;
		}// wind

		public String getwindPower() {
			return windPower;
		}

		public void setwindPower(String wp) {
			this.windPower = wp;
		}// windPower

		public String getRelativeHumidity() {
			return RelativeHumidity;
		}

		public void setRelativeHumidity(String rh) {
			this.RelativeHumidity = rh;
		}// RelativeHumidity

	}

	// 自定义 weatherinfo 类,天气预报
	public class weatherinfo {

		private String date_y;// 日期,设置到title
		private String city; // 城市
		private ArrayList<String> week = new ArrayList<String>();; // 星期几
		private String fchh; // 发布时间，设置到title
		private ArrayList<String> weather = new ArrayList<String>();// 天气
		private ArrayList<String> temp = new ArrayList<String>();// 气温
		private ArrayList<String> wind = new ArrayList<String>();// 风
		// private ArrayList<Integer> img; //图片

		private String advise;// 建议

		public String getdate_y() {
			return date_y;
		}

		public void setdate_y(String date_y) {
			this.date_y = date_y;
		}

		public String getcity() {
			return city;
		}

		public void setcity(String city) {
			this.city = city;
		}

		public String getweek(int index) {
			return week.get(index);
		}

		public void setweek(String week, int index) {
			this.week.set(index, week);
		}

		public void addweek(String week) {
			this.week.add(week);
		}

		public String getfchh() {
			return fchh;
		}

		public void setfchh(String fchh) {
			this.fchh = fchh;
		}

		public String getweather(int index) {
			return weather.get(index);
		}

		public void setweather(String weather, int index) {
			this.weather.set(index, weather);
		}

		public void addweather(String wea) {
			this.weather.add(wea);
		}

		public String gettemp(int index) {
			return temp.get(index);
		}

		public void settemp(String tmp, int index) {
			this.temp.set(index, tmp);
		}

		public void addtemp(String tmp) {
			this.temp.add(tmp);
		}

		public String getwind(int index) {
			return wind.get(index);
		}

		public void setwind(String wind, int index) {
			this.wind.set(index, wind);
		}

		public void addwind(String win) {
			this.wind.add(win);
		}

		public String getadvise() {
			return advise;
		}

		public void setadvise(String advise) {
			this.advise = advise;
		}

		// public Integer getImg(int index) {
		// return img.get(index);
		// }

		// public void setImg(ArrayList<Integer> img) {
		// this.img = img;
		// }

	}

	/* 菜单制作 */
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "返回").setIcon(

		android.R.drawable.ic_menu_revert);

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "空气").setIcon(

		R.drawable.pm2_5);

		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case Menu.FIRST + 1:

			XhulooActivity_weatherreport.this.finish();
			break;
		case Menu.FIRST + 2:

			startACIntent.stAcIntent(XhulooActivity_weatherreport.this,
					XA_AirQuality.class);
			break;

		}

		return false;

	}

}

/*
 * 
 * 
 * 
 * 
 * public class HttpURLConnectionActivity extends Activity {
 * 
 * private ImageView imageView;
 * 
 * protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState); setContentView(R.layout.simple1);
 * 
 * imageView=(ImageView) this.findViewById(R.id.imageView1); //传入网络图片地址 try {
 * URL url = new
 * URL("http://news.xinhuanet.com/photo/2012-02/09/122675973_51n.jpg");
 * 
 * HttpURLConnection conn= (HttpURLConnection) url.openConnection();
 * 
 * conn.setRequestMethod("GET"); conn.setConnectTimeout(5*1000);
 * 
 * conn.connect(); InputStream in=conn.getInputStream();
 * 
 * ByteArrayOutputStream bos=new ByteArrayOutputStream(); byte[] buffer=new
 * byte[1024]; int len = 0; while((len=in.read(buffer))!=-1){
 * bos.write(buffer,0,len); } byte[] dataImage=bos.toByteArray(); bos.close();
 * in.close(); Bitmap bitmap=BitmapFactory.decodeByteArray(dataImage, 0,
 * dataImage.length); //Drawable drawable=BitmapDrawable.
 * imageView.setImageBitmap(bitmap); }
 * 
 * catch (Exception e) { e.printStackTrace();
 * Toast.makeText(getApplicationContext(), "图片加载失败", 1).show(); }
 * 
 * } }
 * 
 * 
 * 
 * 
 * void downFile(final String paramString) { this.pBar.show(); new Thread() {
 * public void run() { File localFile1 = new File("/sdcard/update"); if
 * (!localFile1.exists()) localFile1.mkdir(); File localFile2 = new
 * File("/sdcard/update/SH_AQI.apk"); try { URL localURL = new URL(paramString);
 * try { HttpURLConnection localHttpURLConnection = (HttpURLConnection) localURL
 * .openConnection(); localHttpURLConnection.connect(); InputStream
 * localInputStream = localHttpURLConnection .getInputStream(); FileOutputStream
 * localFileOutputStream = new FileOutputStream( localFile2); byte[] arrayOfByte
 * = new byte[256]; if (localHttpURLConnection.getResponseCode() >= 400)
 * Toast.makeText(SH_AQIActivity.this, "连接超时", 0) .show(); while (true) {
 * localHttpURLConnection.disconnect(); localFileOutputStream.close();
 * localInputStream.close(); SH_AQIActivity.this.down(); return; if
 * (localInputStream != null) { int i = localInputStream.read(arrayOfByte); if
 * (i > 0) { localFileOutputStream.write(arrayOfByte, 0, i); if (0.0D <= 100.0D)
 * break; } } } } catch (IOException localIOException) {
 * SH_AQIActivity.this.pBar.cancel(); localIOException.printStackTrace(); } }
 * catch (MalformedURLException localMalformedURLException) {
 * SH_AQIActivity.this.pBar.cancel();
 * localMalformedURLException.printStackTrace(); } } }.start(); }
 */
