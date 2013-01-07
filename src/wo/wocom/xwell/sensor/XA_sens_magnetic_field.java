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
 * @version 1.3.1
 * @see 磁场
 */
public class XA_sens_magnetic_field extends Activity {

	String TAG = "XA_S_Mf";
	float x, y, z;
	Sensor sensor;
	SensorManager sm;

	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pac_lo_simplegps);
		sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		// System services not available to Activities before onCreate
		sensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sm.registerListener(sel, sensor, SensorManager.SENSOR_DELAY_GAME);

	}

	// 添加磁场感应侦听，并实现其方法，
	SensorEventListener sel = new SensorEventListener() {

		@TargetApi(9)
		public void onSensorChanged(SensorEvent se) {
			x = se.values[SensorManager.DATA_X];
			y = se.values[SensorManager.DATA_Y];
			z = se.values[SensorManager.DATA_Z];

			TextView textView_sensor = (TextView) findViewById(R.id.pac_lo_sg_tv);
			// float[] i = {(float) 0.0,(float) 0.0,(float) 0.0};
			// 设置显示文字
			textView_sensor.setText("磁场感应:" + "\n" + "x=" + x + "\n" + "y=" + y
					+ "\n" + "z=" + z + "\n\n" + "Name:" + sensor.getName()
					+ "\n" + "Type:" + sensor.getType() + "\n" + "Version:"
					+ sensor.getVersion() + "\n" + "vendor:"
					+ sensor.getVendor() + "\n" + "MAXrange:"
					+ sensor.getMaximumRange() + "\n" + "MinDelay:"
					+ sensor.getMinDelay()+"\n" + "power:" + sensor.getPower()
					// getMinDelay()Added in API level 9
					+ "\n" + "Resolution:" + sensor.getResolution() + "\n");
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
