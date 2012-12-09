package wo.wocom.xwell;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.2
 * @more 程序入口
 */
public class XhulooActivity extends Activity {
	/** Called when the activity is first created. */

	private static final String TAG = "XA_Xhuloo";

	String mEditText01_s = null;

	/* 重力感应参数 */
	float x, y, z;
	float currentAcceleration = 0;
	float maxAcceleration = 0;

	/* 生命周期 */
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); // 设置主布局

		Log.i(XhulooActivity.TAG, "XA_start onCreate~~~");

		final TextView main_textview = (TextView) findViewById(R.id.main_textview);
		final Button ok_button = (Button) findViewById(R.id.widget40_button_OK);
		final EditText mEditText01 = (EditText) findViewById(R.id.myEditText);
		final CheckBox iAccept = (CheckBox) findViewById(R.id.CheckBox_Accept);
		final String[] data = { "心怔煩亂敵若云", "有炎百刃月無暈", "靈威震怒鼓徹天", "犀甲迸裂矣厥劍",
				"永思故原遙難反", "遠峙疊巒久無還", "埋骨鬼雄血浸袍", "藏懷青絲志誠傲", "顧往舊徑櫻爛漫",
				"逸鸞鳳鹮面桃然", "雯華琉白馨而靨" };

		/* 按钮监听 */
		ok_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				ok_button.setText("Hi,clicked");

				switch (v.getId()) {
				case R.id.widget40_button_OK:

					mEditText01_s = mEditText01.getText().toString(); // 获得文本编框的文字
					// toast
					Toast.makeText(XhulooActivity.this,
							"XA_Toast:" + mEditText01_s, Toast.LENGTH_LONG)
							.show();
					break;

				default:
					break;
				}// switch
			}// onclick_end

		});

		/* 设置EditText 用OnKeyListener 事件来启动 */
		mEditText01.setOnKeyListener(new EditText.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (mEditText01.getText().toString().length() != 0) {
					ok_button.setText(mEditText01.getText().toString());
				}

				else {
					ok_button.setText("ok请提交");
					AlertDialog.Builder my_ADialog1 = new AlertDialog.Builder(
							XhulooActivity.this);
					/* 设置标题 */
					my_ADialog1.setTitle("XA_AlertDialog.Builder");
					/* 设置显示消息 */
					my_ADialog1.setMessage("请提交");
					/* 设置不能取消 */
					my_ADialog1.setCancelable(false);

					my_ADialog1.setPositiveButton("退出Huloo;并访问官网",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									XhulooActivity.this.finish();
									Uri uri = Uri
											.parse("http://www.wuwenjie.tk");
									Intent intent = new Intent(
											Intent.ACTION_VIEW, uri);
									startActivity(intent);
								}
							});

					my_ADialog1.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
					/* 显示 */
					my_ADialog1.show();
				}

				return false;
			}// onkey_end
		});// sokl_end

		/* 图片 */
		final ImageView mImageView = (ImageView) findViewById(R.id.imageView1);
		mImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.action));

		/* 选择监听 */
		iAccept.setOnClickListener(new CheckBox.OnClickListener() {
			public void onClick(View v) {
				if (iAccept.isChecked()) {
					main_textview.setText("checkbox checked");
					mImageView.setVisibility(View.VISIBLE);
				} else {
					main_textview.setText(getString(R.string.hello));
					mImageView.setVisibility(View.INVISIBLE);
				}
			}
		});

		/* 列表 */
		ListView lv = (ListView) findViewById(R.id.list1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, data); // simple_list_item_1是系统本身有的布局
		lv.setAdapter(adapter);

		/* 重力感应功能 */

		// 获得重力感应硬件控制器
		SensorManager sm = (SensorManager) this
				.getSystemService(SENSOR_SERVICE);
		final Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		// 添加重力感应侦听，并实现其方法，
		SensorEventListener sel = new SensorEventListener() {

			double calibration = SensorManager.STANDARD_GRAVITY;

			// 系统设置的重力加速度标准值，设备在水平静止的情况下就承受这个压力，
			// 所以默认Y轴方向的加速度值为STANDARD_GRAVITY
			public void onSensorChanged(SensorEvent se) {
				x = se.values[SensorManager.DATA_X];
				y = se.values[SensorManager.DATA_Y];
				z = se.values[SensorManager.DATA_Z];

				// 计算三个方向的加速度
				double a = Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)
						+ Math.pow(z, 2)));

				// 消去原有的重力引起的压力
				currentAcceleration = Math.abs((float) (a - calibration));
				if (currentAcceleration > maxAcceleration)
					maxAcceleration = currentAcceleration;

				TextView textView_sensor = (TextView) findViewById(R.id.textView_sensor);
				// 设置显示文字
				textView_sensor.setText("重力感应:" + "\n" + "x=" + x + "\n" + "y="
						+ y + "	" + "(int)z=" + (int) z + "\n" + "maxA:"
						+ maxAcceleration + "\n" + "current:"
						+ currentAcceleration + "\n"
						+ "Name:"
						+ sensor.getName()
						+ " "
						+ "Type:"
						+ sensor.getType()
						+ "\n" // 得到传感器信息
						+ "Version:" + sensor.getVersion() + " " + "vendor:"
						+ sensor.getVendor() + "\n" + "MAXrange:"
						+ sensor.getMaximumRange() + " " + "power:"
						+ sensor.getPower() + "\n" + "Resolution:"
						+ sensor.getResolution() + "\n");

			}// onSensorChanged

			public void onAccuracyChanged(Sensor arg0, int arg1) {
			}
		};
		// 注册Listener，SENSOR_DELAY_GAME为检测的精确度，
		sm.registerListener(sel, sensor, SensorManager.SENSOR_DELAY_GAME);

	}// oncreate end

	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "XA_start onRestart");
		Toast.makeText(XhulooActivity.this, "XA_Toast:onRestart",
				Toast.LENGTH_SHORT).show();
	}

	protected void onResume() {
		super.onResume();
		Log.i(TAG, "XA_start onResume");
		Toast.makeText(XhulooActivity.this, "XA_Toast:onResume",
				Toast.LENGTH_SHORT).show();
	}

	protected void onPause() {
		super.onPause();
		Log.i(TAG, "XA_start onPause");
		Toast.makeText(XhulooActivity.this, "XA_Toast:onPause",
				Toast.LENGTH_SHORT).show();
	}

	protected void onStop() {
		super.onStop();
		Log.i(TAG, "XA_start onStop");
		Toast.makeText(XhulooActivity.this, "XA_Toast:onStop",
				Toast.LENGTH_SHORT).show();
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "XA_start onDestroy");
		Toast.makeText(XhulooActivity.this, "XA_Toast:onDestroy",
				Toast.LENGTH_LONG).show();
	}

	/* 菜单制作 */
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * 
		 * add()方法的四个参数，依次是：
		 * 
		 * 1、组别，如果不分组的话就写Menu.NONE,
		 * 
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单
		 * 
		 * 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 
		 * 4、文本，菜单的显示文本
		 */

		menu.add(Menu.NONE, Menu.FIRST + 1, 7, "音乐").setIcon(

		android.R.drawable.ic_btn_speak_now);

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "球").setIcon(

		android.R.drawable.presence_online);

		menu.add(Menu.NONE, Menu.FIRST + 3, 6, "帮助").setIcon(

		android.R.drawable.ic_menu_help);

		menu.add(Menu.NONE, Menu.FIRST + 4, 4, "相机").setIcon(

		android.R.drawable.ic_menu_camera);

		menu.add(Menu.NONE, Menu.FIRST + 5, 5, "Tbrowser").setIcon(

		R.drawable.browser_icon);

		menu.add(Menu.NONE, Menu.FIRST + 6, 3, "myplaces").setIcon(

		android.R.drawable.ic_menu_myplaces);

		menu.add(Menu.NONE, Menu.FIRST + 7, 5, "天气").setIcon(

		android.R.drawable.ic_menu_today);

		menu.add(Menu.NONE, Menu.FIRST + 8, 8, "录音").setIcon(

		android.R.drawable.ic_menu_revert);

		menu.add(Menu.NONE, Menu.FIRST + 9, 9, "指南").setIcon(

		android.R.drawable.ic_menu_compass);

		menu.add(Menu.NONE, Menu.FIRST + 10, 10, "退出").setIcon(

		android.R.drawable.ic_menu_revert);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case Menu.FIRST + 1:

			Toast.makeText(this, "XA_音乐菜单点击", Toast.LENGTH_SHORT).show();
			/* 跳转新界面activity */
			Intent intent03 = new Intent(); // 新建Intent意图
			intent03.setClass(XhulooActivity.this,
					XhulooActivity_playmusic.class);
			startActivity(intent03); // 跳转

			break;

		case Menu.FIRST + 2:

			Toast.makeText(this, "XA_touchball点击", Toast.LENGTH_SHORT).show();
			/* 跳转新界面activity */
			Intent intent05 = new Intent(); // 新建Intent意图
			intent05.setClass(XhulooActivity.this, XA_touchball.class);
			startActivity(intent05); // 跳转

			break;

		case Menu.FIRST + 3:

			Toast.makeText(this, "XA_帮助：wewenjie.tk", Toast.LENGTH_SHORT)
					.show();

			AlertDialog.Builder my_ADialog02 = new AlertDialog.Builder(
					XhulooActivity.this);
			my_ADialog02.setTitle("XA_AlertDialog"); // 设置标题
			my_ADialog02.setMessage("帮助：访问wuwenjie.tk的项目页|访问GPL"); // 设置显示消息
			my_ADialog02.setCancelable(true); // 设置能取消

			my_ADialog02.setPositiveButton("访问wwj",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Uri uri = Uri.parse("http://www.wuwenjie.tk");
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent);
						}
					});

			my_ADialog02.setNegativeButton("访问GPL",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Uri uri = Uri
									.parse("http://www.gnu.org/copyleft/gpl.html");
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent);
						}
					});
			/* 显示 */
			my_ADialog02.show();

			break;

		case Menu.FIRST + 4:

			Toast.makeText(this, "XA_添加相机点击", Toast.LENGTH_SHORT).show();

			AlertDialog.Builder my_ADialog03 = new AlertDialog.Builder(
					XhulooActivity.this);
			my_ADialog03.setTitle("相机"); // 设置标题
			my_ADialog03.setMessage("选择不同相机"); // 设置显示消息
			my_ADialog03.setCancelable(true); // 设置能取消

			my_ADialog03.setPositiveButton("调用相机",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent intent = new Intent()
									.setAction("android.media.action.STILL_IMAGE_CAMERA");// 调用相机
							startActivity(intent);
						}// onclick end
					});
			my_ADialog03.setNegativeButton("camera",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							/* 跳转新界面activity */
							Intent intent = new Intent(); // 新建Intent意图
							intent.setClass(XhulooActivity.this,
									XA_camera.class);
							startActivity(intent);
						}// onclick end
					});
			/* 显示 */
			my_ADialog03.show();
			break;

		case Menu.FIRST + 5:

			Toast.makeText(this, "XA_browser", Toast.LENGTH_SHORT).show();
			/* 跳转新界面activity */
			Intent intent06 = new Intent(); // 新建Intent意图
			intent06.setClass(XhulooActivity.this,
					wo.wocom.xwell.browser.browser_main_view.class);
			startActivity(intent06); // 跳转，此处的0 OR >=0

			break;

		case Menu.FIRST + 6:

			Toast.makeText(this, "XA_myplaces点击", Toast.LENGTH_SHORT).show();

			/* 跳转新界面activity */
			Intent intent = new Intent(); // 新建Intent意图
			intent.setClass(XhulooActivity.this, XhulooActivity_myplaces.class);
			// 方便的调用名称为,返回一个Class对象
			Bundle mBundle = new Bundle();
			mBundle.putString("XA_Data", "XA_data from X:" + mEditText01_s);
			// 加入数据 参数：key-Data value +mEditText01_s
			intent.putExtras(mBundle); // 添加附加信息
			startActivityForResult(intent, 0); // 跳转，此处的0 OR >=0

			break;

		case Menu.FIRST + 7:

			Toast.makeText(this, "XA_天气菜单点击", Toast.LENGTH_SHORT).show();

			/* 跳转新界面activity */
			Intent intent01 = new Intent(); // 新建Intent意图
			intent01.setClass(XhulooActivity.this,
					XhulooActivity_weatherreport.class);
			startActivity(intent01); // 跳转，此处的0 OR >=0

			break;

		case Menu.FIRST + 8:

			Toast.makeText(this, "XA_录音菜单点击", Toast.LENGTH_SHORT).show();
			/* 跳转新界面activity */
			Intent intent04 = new Intent(); // 新建Intent意图
			intent04.setClass(XhulooActivity.this,
					XhulooActivity_audioRecord.class);
			startActivity(intent04); // 跳转，此处的0 OR >=0

			break;

		case Menu.FIRST + 9:

			Toast.makeText(this, "XA_指南菜单点击", Toast.LENGTH_SHORT).show();

			/* 跳转新界面activity */
			Intent intent02 = new Intent(); // 新建Intent意图
			intent02.setClass(XhulooActivity.this, XhulooActivity_compass.class);
			startActivity(intent02); // 跳转
			break;

		case Menu.FIRST + 10:

			Toast.makeText(this, "XA_退出菜单点击", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "XhulooActivity.this.finish()");
			XhulooActivity.this.finish();

			break;

		}

		return false;

	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		Toast.makeText(this, "选项菜单关闭", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Toast.makeText(this, "选项菜单显示前onPrepareOptionsMenu会被调用，可用来调整菜单",
				Toast.LENGTH_SHORT).show();

		// 如果返回false，此方法就把用户点击menu的动作给消费了，onCreateOptionsMenu方法将不会被调用
		return true;
	}

	/* 处理返回值 startActivityForResult */

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		// resultCode为回传的标记，回传的是RESULT_OK
		case RESULT_OK:
			Bundle b = data.getExtras();
			// data回传的Intent
			String str = b.getString("MP_msg");
			// str即为msg回传的值
			Toast.makeText(
					this,
					"XA_str getString(\"msg\"):" + str + "b.toString:"
							+ b.toString(), Toast.LENGTH_LONG).show();
			Log.i(TAG, "XA_onActivityResult:---" + "XA_str getString(\"msg\"):"
					+ str + " b.toString:" + b.toString());
			break;
		default:
			break;
		}
	}

}