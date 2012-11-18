package wo.wocom.xwell;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.4
 * @see 方向传感,指南针,图片旋转
 */
public class XhulooActivity_compass extends Activity implements
		SensorEventListener {

	private static final String TAG = "COM_Xhuloo";
	SensorManager sensorManager; // 传感器管理器
	Sensor sensor; // 传感器
	Compass_imageView compassView; // 自定义view
	TextView tv;

	/* activity生命周期 */
	public void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "COM_onCreate------COM_Xhuloo");
		super.onCreate(savedInstanceState);

		// 全屏显示窗口
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		FrameLayout fl = new FrameLayout(this); // 提供一个帧布局
		compassView = new Compass_imageView(this); // 实列化 自定义view		
		fl.addView(compassView); // 创建预览用子类，放于fl底层

		tv = new TextView(this); // 创建文本框做特效
		fl.addView(tv);

		setContentView(fl); // 设置Activity的根内容视图
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // 获取管理服务
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION); // 方向传感器

	}// oncreateEND

	public void onRestart() {
		super.onRestart();
		Log.i(TAG, "COM_onRestart");
	}

	public void onResume() {
		super.onResume();
		Log.i(TAG, "COM_onResume");
		// 注册监听器
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void onPause() {
		super.onPause();
		Log.i(TAG, "COM_onPause");
		sensorManager.unregisterListener(this);// 注销监听器
	}

	public void onStop() {
		super.onStop();
		Log.i(TAG, "COM_onStop");
		sensorManager.unregisterListener(this);
	}

	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "COM_onDestroy");
		sensorManager.unregisterListener(this);
	}

	/* 菜单制作 */
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, Menu.FIRST + 1, 2, "返回").setIcon(

		android.R.drawable.ic_menu_revert);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case Menu.FIRST + 1:

			Log.i(TAG, "XhulooActivity_compass.this.finish()");
			XhulooActivity_compass.this.finish();
			break;

		}

		return false;

	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		Toast.makeText(this, "wr_选项菜单关闭", Toast.LENGTH_SHORT).show();
	}

	/* SensorEventListener */
	/* 忽略精度的变化，Called when the accuracy of a sensor has changed. */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	/* 侦听【传感器】的变化并输出，Called when sensor values have changed. */
	public void onSensorChanged(SensorEvent event) {
		int orientation = (int) event.values[0];
		// 方向，values[0]: 垂直于Z轴，沿Y轴正方向顺时针旋转的角度
		tv.setText("(int) event.values[0]="+orientation);
		compassView.setDirection(orientation);// 传入参数，旋转图片view
	}

	/*
	 * public void onConfigurationChanged(Configuration newConfig){
	 * super.onConfigurationChanged(newConfig); }
	 */

	/* 自定义类 表盘 */
	public class Compass_imageView extends ImageView {
		int direction = 0;

		public Compass_imageView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setImageResource(R.drawable.compasslogo);// 设置图片内容
		}

		// 在组件绘制时触发
		public void onDraw(Canvas canvas) {

			int height = this.getHeight();// Returns the height of the current
											// drawing layer
			int width = this.getWidth();
			// Log.i(TAG, "Compass_imageView onDraw--heigth="+height);
			// Log.i(TAG, "Compass_imageView onDraw--width="+width);
			// Log.i(TAG, "Compass_imageView onDraw--density="+
			// canvas.getDensity());//获得密度
			// Log.i(TAG,
			// "Compass_imageView onDraw--SaveCount="+canvas.getSaveCount());//返回的矩阵/剪辑状态
			canvas.rotate(direction, width / 2, height / 2);// 旋转，参数1：旋转量；2、3：旋转中心
			super.onDraw(canvas);
		}

		// 由XhulooActivity_compass对象调用，更新方向
		public void setDirection(int direction_par) {
			this.direction = direction_par;
			this.invalidate(); // 要求重绘,无效的整体视图,如果可见,会调用OnDraw
		}

	}// Compass_imageView end

}
