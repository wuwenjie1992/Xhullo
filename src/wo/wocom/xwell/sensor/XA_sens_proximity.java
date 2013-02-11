package wo.wocom.xwell.sensor;

import wo.wocom.xwell.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.10.3.12:1
 * @see 距离
 */
public class XA_sens_proximity extends Activity {

	String TAG = "XA_S_PX";
	double x;
	float y, z;
	Sensor sensor;
	SensorManager sm;
	TextView textView_sensor;

	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pac_lo_simplegps);
		textView_sensor = (TextView) findViewById(R.id.pac_lo_sg_tv);

		sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		// System services not available to Activities before onCreate
		sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);// 距离
		sm.registerListener(sel, sensor, SensorManager.SENSOR_DELAY_GAME);

	}

	// 添加磁场感应侦听，并实现其方法，
	SensorEventListener sel = new SensorEventListener() {

		@TargetApi(9)
		public void onSensorChanged(SensorEvent se) {
			x = se.values[SensorManager.DATA_X];
			y = se.values[SensorManager.DATA_Y];
			z = se.values[SensorManager.DATA_Z];

			// 设置显示文字
			textView_sensor.setText("距离感应:" + "\n" + "x=" + x + "\n" + "y=" + y
					+ "\n" + "z=" + z + "\n\n" + "Name:" + sensor.getName()
					+ "\n" + "Type:" + sensor.getType() + "\n" + "Version:"
					+ sensor.getVersion() + "\n" + "vendor:"
					+ sensor.getVendor() + "\n" + "MAXrange:"
					+ sensor.getMaximumRange() + "\n" + "MinDelay:"
					+ sensor.getMinDelay() + "\n" + "power:"
					+ sensor.getPower()
					// getMinDelay()Added in API level 9
					+ "\n" + "Resolution:" + sensor.getResolution() + "\n");

			if (x == 0) {
				textView_sensor.setBackgroundColor(0xff000000);// 设背景颜色为黑
			} else if (x == 1) {
				textView_sensor.setBackgroundColor(0xff0000ff);// 设背景颜色为蓝
			} else {
				return;
			}
		}// onSensorChanged

		// 精度
		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		sm.unregisterListener(sel);
	}

	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		sm.unregisterListener(sel);

	}

	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "onRestart");
		sm.registerListener(sel, sensor, SensorManager.SENSOR_DELAY_GAME);

	}

	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		sm.registerListener(sel, sensor, SensorManager.SENSOR_DELAY_GAME);

	}

	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
		sm.unregisterListener(sel);

	}
}