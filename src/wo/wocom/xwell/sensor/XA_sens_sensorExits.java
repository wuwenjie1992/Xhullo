package wo.wocom.xwell.sensor;

import java.util.ArrayList;
import java.util.List;

import wo.wocom.xwell.R;
import wo.wocom.xwell.utility.XA_util_ADialog;
import wo.wocom.xwell.utility.startACIntent;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.1
 * @see 指南,
 */
public class XA_sens_sensorExits extends Activity {

	//private static final String TAG = "XA_pac_sensorEXits";

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pac_sens_senex);

		final ListView sensor_lv = (ListView) findViewById(R.id.pac_sens_senex_lv);

		// 从系统服务中获得传感器管理器
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		// 从传感器管理器中获得全部的传感器列表
		List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
		// int i=allSensors.size();
		ArrayList<String> item = new ArrayList<String>(13);

		// Log.i(TAG,"i="+i);
		for (int j = 0; j <= 13; j++) {
			item.add("不存在" + j);
		}

		for (Sensor s : allSensors) {// for (循环变量类型 循环变量名称 : 要被遍历的对象) 循环体
			// item.add(s.getName()+" : "+s.getType());
			switch (s.getType()) {// 传感器的泛型类型 int

			case Sensor.TYPE_ACCELEROMETER:
				item.set(0, s.getName() + "加速度");// set(int index, E object)
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				item.set(1, s.getName() + "电磁场");
				break;
			case Sensor.TYPE_ORIENTATION:
				item.set(2, s.getName() + "方向");
				break;
			case Sensor.TYPE_GYROSCOPE:
				item.set(3, s.getName() + "陀螺仪");
				break;
			case Sensor.TYPE_LIGHT:
				item.set(4, s.getName() + "光线");
				break;
			case Sensor.TYPE_PRESSURE:
				item.set(5, s.getName() + "压力");
				break;
			case Sensor.TYPE_TEMPERATURE:
				item.set(6, s.getName() + "温度");
				break;
			case Sensor.TYPE_PROXIMITY:
				item.set(7, s.getName() + "距离");
				break;
			case 9:// Sensor.TYPE_GRAVITY:
				item.set(8, s.getName() + "重力");
				break;
			case 10:// Sensor.TYPE_LINEAR_ACCELERATION:
				item.set(9, s.getName() + "线性加速度");
				break;
			case 11:// Sensor.TYPE_ROTATION_VECTOR :
				item.set(10, s.getName() + "旋转矢量");
				break;
			case 12:// Sensor.TYPE_RELATIVE_HUMIDITY :
				item.set(11, s.getName() + "相对湿度");
				break;
			case 13:// Sensor.TYPE_AMBIENT_TEMPERATURE :
				item.set(12, s.getName() + "环境温度");
				break;
			default:
				item.set(13, s.getType() + "未知");
				break;
			}

			// Log.i(TAG, item.toString());

		}// forend
			// BMA220 3轴加速度传感器;MMC314X 磁力计;mag acc combo orientation sensor 方向
			// TAOS 12389 10 11
			// MEMS传感器包括加速度计(ACC)、(GYRO)、磁力计(MAG)、压力传感器(PS)和话筒(MIC)

		final ArrayAdapter<String> fileList_ArrayAd = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, item);
		// items中的数据加到ADAPTER(配适器）
		sensor_lv.setAdapter(fileList_ArrayAd);

		/* 列表点击监听响应 */
		sensor_lv.setOnItemClickListener(new OnItemClickListener() {
			// 当item被点击时
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				String select_s = new String(parent.getAdapter()
						.getItem(position).toString());

				Toast.makeText(XA_sens_sensorExits.this, "打开" + select_s,
						Toast.LENGTH_SHORT).show();

				if (select_s.matches("不存在.|未知")) {
					XA_util_ADialog alog=new XA_util_ADialog(XA_sens_sensorExits.this);
					alog.show1ADialog("注意", "传感器不存在或未知","好的");
				} else {
					switch (position) {
					case 0:

						break;
					case 1:
						startACIntent.stAcIntent(XA_sens_sensorExits.this,
								XA_sens_magnetic_field.class);
						break;
					case 2:
						startACIntent.stAcIntent(XA_sens_sensorExits.this,
								XA_sens_orientation.class);
						break;
					case 3:

						break;
					case 4:

						break;
					case 5:
					}// switch

				}// else
			}
		});

	}// oncreat

	public void stAcIntent(Context con, Class<?> cls) {
		/* 跳转新界面activity */
		Intent in = new Intent(); // 新建Intent意图
		in.setClass(con, cls);
		// 方便的调用名称为,返回一个Class对象
		startActivity(in); // 跳转
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onRestart() {
		super.onRestart();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {
		super.onStop();
	}

}
