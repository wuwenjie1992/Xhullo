package wo.wocom.xwell;

import java.util.ArrayList;

import wo.wocom.xwell.net.GetHtml2Str;
import wo.wocom.xwell.utility.XA_util_ADialog;
import wo.wocom.xwell.utility.XA_util_readStrByregEx;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.10.3.8：3
 * @more 空气质量，semcs,haqi网页太大,使用php抓取
 * @Notice 代码依赖于网页信息，网页改变可能导致crash
 */
public class XA_AirQuality extends Activity {

	String TAG = "XA_AirQuality";
	String title = "正在联网获取数据....";
	String[] list_s = new String[11];
	int i;
	MyHandler2 myHandler2;
	ArrayAdapter<String> lv_ArrayAd;
	String url = "http://wuwenjie.tk/o/shapi.php"; // AQI
	String url2 = "http://wuwenjie.tk/o/shweaStationInfo.php";// 站点信息
	String url3 = "http://wuwenjie.tk/o/foreignAQI.php";// 外国检测结果
	// http://www.aqicn.info/?city=Shanghai/USconsulate&size=xlarge&lang=cn
	AirQuality aq;

	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wr); // 设置主布局

		// 初始化---------------
		aq = new AirQuality();
		for (i = 0; i <= 10; i++) {
			list_s[i] = title;
		}

		myHandler2 = new MyHandler2();
		// 创建新的Handler实例
		// 绑定到当前线程和消息的队列中,开始分发数据
		MyThread m = new MyThread();
		new Thread(m).start();

		showlv();

	}

	public void showlv() {

		ListView lv = (ListView) this.findViewById(R.id.wr_lv);
		lv_ArrayAd = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list_s);
		// ADAPTER(配适器）

		lv.setAdapter(lv_ArrayAd);
		lv_ArrayAd.notifyDataSetChanged(); // 通知数据改变，反应该刷新视图
	}

	// 接收,处理消息,Handler与当前主线程一起运行
	public class MyHandler2 extends Handler {

		public MyHandler2() {
		}

		public MyHandler2(Looper L) {
			super(L);
		}

		// 子类必须重写此方法,接受数据
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			// 传递信息以更新UI
			Bundle b2 = msg.getData();
			Log.i(TAG, "handleMessage" + b2.getInt("DO"));

			switch (b2.getInt("DO")) {
			case 0:

				XA_util_ADialog alog = new XA_util_ADialog(XA_AirQuality.this);
				alog.show1ADialog("问题", "网络错误", "好的");

				break;

			case 1:
				
				XA_AirQuality.this.setTitle(aq.gettime());
				list_s[0] = "空气指数：" + aq.getcurrentAQI();
				list_s[1] = "空气质量：" + aq.getAQStatus();
				list_s[2] = "首要污染：" + aq.getPrimaryPollutants();
				list_s[3] = "主要影响：" + aq.getHealthEffects();
				list_s[4] = "采取措施：" + aq.getRecommendedAction();
				list_s[5] = "PM2.5：" + aq.getIAQI(0);
				list_s[6] = "O3一小时：" + aq.getIAQI(1);
				list_s[7] = "O3八小时：" + aq.getIAQI(2);
				list_s[8] = "CO：" + aq.getIAQI(3);
				list_s[9] = "PM10：" + aq.getIAQI(4);
				list_s[10] = "NO2：" + aq.getIAQI(5);

				showlv(); // 通知数据改变，反应该刷新视图
				
				break;
			}

		}// handleMessage end

	}// myHandler end

	public class MyThread implements Runnable {

		public void run() {

			Looper.prepare();
			String html_s = null; // 网页字符串
			// 向Handler发送消息,更新UI
			Message msg2 = new Message();
			Bundle b2 = new Bundle();// 存放数据

			html_s = GetHtml2Str.reStr(html_s, url, XA_AirQuality.this);
			// 联网处理数据,返回字符串

			if (html_s != null && html_s.substring(1, 2).equals("A")) { // 第一url判断条件

				// 处理数据-----------------------
				aq.settime(html_s.substring(5, 19));// 设置时间

				aq.setcurrentAQI(Integer
						.parseInt(regValue(html_s, "时(\\d*).*")));
				// 时(\d*).* ; '\'需使用'\\'转义
				// Integer.parseInt(String i);//i转换为int

				aq.setAQStatus(regValue(html_s, "时[\\d]{1,3}(.{1,4}).级"));
				// 时[\d]{1,3}(.{1,4}).级

				aq.setPrimaryPollutants(regValue(html_s, "物(\\w.{1,})-2.小"));
				// 物(\w.{1,})-2.小;'\'需使用'\\'转义

				aq.setHealthEffects(regValue(html_s, "响(.*)建"));

				aq.setRecommendedAction(regValue(html_s, "(建.*。)"));

				// 初始化,ArrayList<String> IAQI,weaStationInfo;
				for (i = 0; i <= 6; i++) {
					aq.addIAQI(i);
					aq.addweaStationInfo(i);
				}

				aq.setIAQI(Integer.parseInt(regValue(html_s, "5(\\d*)O")), 0);// PM2.5
				aq.setIAQI(Integer.parseInt(regValue(html_s, "时(\\d*)O")), 1);// O3-1小时
				aq.setIAQI(Integer.parseInt(regValue(html_s, "(\\d*)CO")), 2);// O3-8小时
				aq.setIAQI(Integer.parseInt(regValue(html_s, "CO(\\d*)")), 3);// CO
				aq.setIAQI(Integer.parseInt(regValue(html_s, "PM10(\\d{1,})")),
						4);// PM10
				aq.setIAQI(Integer.parseInt(regValue(html_s, "SO2(\\d*)")), 5);// SO2
				aq.setIAQI(Integer.parseInt(regValue(html_s, "NO2(\\d*)$")), 6);// NO2

				Log.i(TAG,
						"aq...." + aq.gettime() + ":" + aq.getcurrentAQI()
								+ ":" + aq.getAQStatus() + ":"
								+ aq.getPrimaryPollutants() + ":"
								+ aq.getHealthEffects() + ":"
								+ aq.getRecommendedAction() + ":"
								+ aq.getIAQI(0) + ":" + aq.getIAQI(1) + ":"
								+ aq.getIAQI(2) + ":" + aq.getIAQI(3) + ":"
								+ aq.getIAQI(4) + ":" + aq.getIAQI(5) + ":"
								+ aq.getIAQI(6));

				// 向Handler发送消息,更新UI
				b2.putInt("DO", 1);// 1表示成功；可用putString
				msg2.setData(b2);

				XA_AirQuality.this.myHandler2.sendMessage(msg2);
				// 把【信息】在【现在之前的待发信息发送完毕】时推送到【信息队列的末端】

			}// if end
			else {
				// 向Handler发送消息,更新UI
				b2.putInt("DO", 0);// 0表示不成功
				msg2.setData(b2);
			}

			Looper.loop();

		}// run end
	} // my thread end

	public String regValue(String input, String regEX) {

		String out = XA_util_readStrByregEx.readout(input, regEX, 1);
		return out;

	}

	// 自定义【空气质量类】
	public class AirQuality {
		public String time; // 2013年01月19日 20时
		public int currentAQI; // 125
		// public String AQILevel; // 空气质量等级： 三级
		public String AQStatus; // 空气质量状况： 轻度污染
		public String PrimaryPollutants; // 首要污染物： PM2.5
		public String HealthEffects; // 对健康的影响
		public String RecommendedAction; // 建议采取的措施

		public ArrayList<Integer> IAQI = new ArrayList<Integer>(); // 指标
		public ArrayList<Integer> weaStationInfo = new ArrayList<Integer>();// 站点空气质量指数

		public String gettime() {
			return time;
		}

		public void settime(String ti) {
			this.time = ti;
		}// time

		public int getcurrentAQI() {
			return currentAQI;
		}

		public void setcurrentAQI(int cu) {
			this.currentAQI = cu;
		}// currentAQI

		// public String getAQILevel() {
		// return AQILevel;
		// }

		// public void setAQILevel(String AQ) {
		// this.AQILevel = AQ;
		// }// AQILevel

		public String getAQStatus() {
			return AQStatus;
		}

		public void setAQStatus(String AQ) {
			this.AQStatus = AQ;
		}// AQStatus

		public String getPrimaryPollutants() {
			return PrimaryPollutants;
		}

		public void setPrimaryPollutants(String Pr) {
			this.PrimaryPollutants = Pr;
		}// PrimaryPollutants

		public String getHealthEffects() {
			return HealthEffects;
		}

		public void setHealthEffects(String He) {
			this.HealthEffects = He;
		}// HealthEffects

		public String getRecommendedAction() {
			return RecommendedAction;
		}

		public void setRecommendedAction(String Re) {
			this.RecommendedAction = Re;
		}// RecommendedAction

		public void addIAQI(int tmp) {
			this.IAQI.add(tmp);
		}

		public int getIAQI(int index) {
			return IAQI.get(index);
		}

		public void setIAQI(int IAQI, int index) {
			this.IAQI.set(index, IAQI);
		}// IAQI

		public void addweaStationInfo(int tmp) {
			this.weaStationInfo.add(tmp);
		}

		public int getweaStationInfo(int index) {
			return weaStationInfo.get(index);
		}

		public void setweaStationInfo(int wea, int index) {
			this.weaStationInfo.set(index, wea);
		}// weaStationInfo

	}

	// 其他生命周期
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");

		super.onDestroy();
	}

	protected void onPause() {
		Log.i(TAG, "onPause");

		super.onPause();
	}

	protected void onRestart() {
		Log.i(TAG, "onRestart");

		super.onRestart();
	}

	protected void onResume() {
		Log.i(TAG, "onResume");

		super.onResume();
	}

	protected void onStart() {
		Log.i(TAG, "onStart");

		super.onStart();
	}

	protected void onStop() {
		Log.i(TAG, "onStop");

		super.onStop();
	}

	/**
	 * @author wuwenjie
	 * @filename:shapi.php <?php $url =
	 *                     'http://www.semc.gov.cn/aqi/home/Index.aspx';
	 *                     $info=file_get_contents($url);
	 *                     preg_match('|实时空气质量指数([\s\S]*)公众调查|i',$info,$m);
	 *                     $m=preg_replace('|<[^>]*>|','',$m);//去除标签
	 *                     $m=preg_replace('|\s|','',$m); //去除任何空白字符
	 *                     //\(AQI\)(.*)空气质量指数\(AQI\).*(空气质量日报\(0-23时均值\).*)
	 *                     $m=preg_replace('|(空气质量指数\(AQI\).*关闭)|','',$m); echo
	 *                     $m[1]; ?>
	 */

	/**
	 * @author wuwenjie
	 * @filename:shweaStationInfo.php <?php $url =
	 *                                'http://semc.gov.cn/home/index.aspx';
	 *                                $info=file_get_contents($url);
	 *                                preg_match('|(普陀监测站[\s\S]
	 *                                *)semc|i',$info,$m);
	 *                                $m=preg_replace('|<[^>]*>|',':',$m);//去除标签
	 *                                $m=preg_replace('|\s|',':',$m); //去除任何空白字符
	 *                                $m=preg_replace('|:|','',$m); //$m=ltrim()
	 *                                echo $m[1]; ?>
	 */

	/**
	 * @author wuwenjie
	 * @filename:foreignAQI.php <?php $url =
	 *                          'http://shaqi.info/m/shanghai/mobile';
	 *                          $info=file_get_contents($url); preg_match('|<div
	 *                          id="current-aqi" class="section" >([\s\S]*)<!--
	 *                          end graph -->|i',$info,$m);
	 *                          $m=preg_replace('|(<[^>]*>)|i','',$m); //去除标签
	 *                          $m=preg_replace('|([ ]{2,})|','',$m);
	 *                          //去除多于两个以上的空格 $m=preg_replace('|(\n)|','',$m);
	 *                          //去除换行符 echo $m[1]; ?>
	 */

}
