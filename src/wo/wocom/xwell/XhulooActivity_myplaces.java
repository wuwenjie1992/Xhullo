package wo.wocom.xwell;

import java.io.File;
import java.util.ArrayList;

import wo.wocom.xwell.utility.startACIntent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.6
 * @see 屏幕信息，文件游览功能，PHONE，MEM,smallshell
 */

public class XhulooActivity_myplaces extends Activity {

	private static final String TAG = "MP_Xhuloo";

	ArrayList<String> items_AL = new ArrayList<String>();

	/* activity生命周期 */
	public void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "MP_onCreate------MP_Xhuloo");
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myplaces);

		final Button BC_btn_MP = (Button) findViewById(R.id.BC_btn_MP);
		final RatingBar score_RB = (RatingBar) findViewById(R.id.ratingBar1);
		final TextView mp_textview = (TextView) findViewById(R.id.mp_textview);
		final Intent score_intent = new Intent(); // 回传分数的INTENT
		final ListView file_index_LV = (ListView) findViewById(R.id.MP_list);

		/* 获取屏幕信息 */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 获得手机的宽带和高度像素单位为px
		int width_screen = dm.widthPixels;
		int height_screen = dm.heightPixels;

		float density_screen = dm.density; // 密度
		float densityDpi_screen = dm.density; // 显示屏上显示的字体的缩放系数
		float xdpi_screen = dm.xdpi; // 每英寸的屏幕在X维的确切物理像素。
		float ydpi_screen = dm.ydpi;

		String ScreenInfo_s = new String("screeninfo:" + "分辨率：" + height_screen
				+ "*" + width_screen + "\n" + "密度density" + density_screen
				+ "\n" + "densityDpi" + densityDpi_screen + "\n" + "xdpi"
				+ xdpi_screen + "::" + "ydpi" + ydpi_screen);

		/* 获取OS信息 */
		String OS_Build_info = new String("底层驱动控制板:" + android.os.Build.BOARD
				+ "\n" + "brand:" + "android.os.Build.BRAND" + "\n"
				+ "CPU_ABI:" + android.os.Build.CPU_ABI + ":" + "DEVICE:"
				+ android.os.Build.DEVICE + "\n" + "DISPLAY:"
				+ android.os.Build.DISPLAY + "\n" + "FINGERPRINT:"
				+ android.os.Build.FINGERPRINT + "\n" + "HOST:"
				+ android.os.Build.HOST + "::" + "ID:" + android.os.Build.ID
				+ "\n" + "MANUFACTURER:" + android.os.Build.MANUFACTURER + "\n"
				+ "MODEL:" + android.os.Build.MODEL + "\n" + "PRODUCT:"
				+ android.os.Build.PRODUCT + "\n" + "TAGS:"
				+ android.os.Build.TAGS + "\n" + "TIME:"
				+ android.os.Build.TIME + "\n" + "TYPE:"
				+ android.os.Build.TYPE + "\n" + "USER:"
				+ android.os.Build.USER + "\n" + "BOOTLOADER:"
				+ android.os.Build.BOOTLOADER + "\n" + "CPU_ABI2:"
				+ android.os.Build.CPU_ABI2 + "\n" + "HARDWARE:"
				+ android.os.Build.HARDWARE + "\n" + "RADIO:"
				+ android.os.Build.RADIO + "\n" + "SERIAL:"
				+ android.os.Build.SERIAL + "\n" + "UNKNOWN:"
				+ android.os.Build.UNKNOWN + "\n" + "CODENAME:"
				+ Build.VERSION.CODENAME + "\n" + "INCREMENTAL:"
				+ Build.VERSION.INCREMENTAL + "\n" + "RELEASE:"
				+ Build.VERSION.RELEASE + "\n" + "SDK:" + Build.VERSION.SDK
				+ "\n" + "SDK_INT:" + Build.VERSION.SDK_INT);

		/* 利用Bundle传递通信 */
		Bundle bundle = getIntent().getExtras(); // 得到传过来的bundle
		String data_MP = bundle.getString("XA_Data");// 读出数据
		Log.i(TAG, "OS_Build_info:" + OS_Build_info);
		mp_textview.setText(data_MP + "\n" + ScreenInfo_s + OS_Build_info);

		/* 得到SD卡PATH */
		String SD_PATH_S = Environment.getExternalStorageDirectory().toString();
		Log.i(TAG, "SD_PATH_S------MP_Xhuloo" + SD_PATH_S);

		/* 文件游览功能 */
		open_path("/", items_AL); // 调用自定义函数，return items (ArrayList<String>)
		final ArrayAdapter<String> fileList_ArrayAd = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, items_AL);
		// items中的PATH加到ADAPTER(配适器）
		file_index_LV.setAdapter(fileList_ArrayAd);

		/* 列表点击监听响应 */
		file_index_LV.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				String selectItem = new String(parent.getAdapter()
						.getItem(position).toString());

				Toast.makeText(XhulooActivity_myplaces.this,
						"MP_flie_Toast:打开" + selectItem, Toast.LENGTH_LONG)
						.show();

				items_AL.clear(); // 清除ITEMS内容
				open_path(selectItem, items_AL);
				fileList_ArrayAd.notifyDataSetChanged(); // 通知数据改变，反应该刷新视图

			}
		});

		/* 按钮监听 */
		BC_btn_MP.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.BC_btn_MP:
					XhulooActivity_myplaces.this.finish();
					break;

				default:
					break;
				}// switch
			}// onclick_end
		});

		/* ratingbar 的监听 */
		score_RB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				score_intent.putExtra("MP_msg",
						"my score from MP:" + score_RB.getRating() * 20); // 等到评分getRating()
				XhulooActivity_myplaces.this.setResult(RESULT_OK, score_intent);
			}
		});

	}// onCreate

	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "MP_onRestart");
		Toast.makeText(XhulooActivity_myplaces.this, "MP_onRestart",
				Toast.LENGTH_SHORT).show();
	}

	protected void onResume() {
		super.onResume();
		Log.i(TAG, "MP_onResume");
		Toast.makeText(XhulooActivity_myplaces.this, "MP_onResume",
				Toast.LENGTH_SHORT).show();
	}

	protected void onPause() {
		super.onPause();
		Log.i(TAG, "MP_onPause");
		Toast.makeText(XhulooActivity_myplaces.this, "MP_onPause",
				Toast.LENGTH_SHORT).show();
	}

	protected void onStop() {
		super.onStop();
		Log.i(TAG, "MP_onStop");
		Toast.makeText(XhulooActivity_myplaces.this, "MP_onStop",
				Toast.LENGTH_SHORT).show();
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "MP_onDestroy");
		Toast.makeText(XhulooActivity_myplaces.this, "MP_onDestroy",
				Toast.LENGTH_SHORT).show();
	}

	/* 菜单制作 */
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * 
		 * add()四个参数
		 * 
		 * 1、组别，如果不分组的话就写Menu.NONE
		 * 
		 * 2、Id,根据Id来确定不同菜单
		 * 
		 * 3、顺序，菜单出现的顺序
		 * 
		 * 4、文本，菜单的显示文本
		 */

		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "内存").setIcon(

		R.drawable.memory); // 自己提供的资源是以R开头

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "CPU").setIcon(

		android.R.drawable.dialog_frame);

		menu.add(Menu.NONE, Menu.FIRST + 3, 3, "smallshell").setIcon(

		android.R.drawable.editbox_dropdown_dark_frame);

		menu.add(Menu.NONE, Menu.FIRST + 4, 4, "phone").setIcon(

		android.R.drawable.ic_menu_call);

		menu.add(Menu.NONE, Menu.FIRST + 5, 5, "JNI").setIcon(

		android.R.drawable.progress_indeterminate_horizontal);

		menu.add(Menu.NONE, Menu.FIRST + 6, 6, "GPS").setIcon(

		android.R.drawable.ic_menu_mylocation);

		menu.add(Menu.NONE, Menu.FIRST + 7, 7, "DB").setIcon(

		android.R.drawable.ic_menu_slideshow);

		menu.add(Menu.NONE, Menu.FIRST + 8, 8, "AES加密").setIcon(

		android.R.drawable.ic_menu_revert);

		menu.add(Menu.NONE, Menu.FIRST + 9, 9, "管理员").setIcon(

		android.R.drawable.ic_menu_revert);

		menu.add(Menu.NONE, Menu.FIRST + 10, 10, "调用jar").setIcon(

		android.R.drawable.ic_menu_revert);

		menu.add(Menu.NONE, Menu.FIRST + 11, 11, "按键服务").setIcon(

		android.R.drawable.ic_menu_revert);

		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case Menu.FIRST + 1:

			startACIntent.stAcIntent(XhulooActivity_myplaces.this,
					XhulooActivity_MEM.class);
			break;

		case Menu.FIRST + 2:

			startACIntent.stAcIntent(XhulooActivity_myplaces.this,
					XhulooActivity_cupinfo.class);
			break;

		case Menu.FIRST + 3:

			startACIntent.stAcIntent(XhulooActivity_myplaces.this,
					XA_smallshell.class);
			break;

		case Menu.FIRST + 4:

			startACIntent.stAcIntent(XhulooActivity_myplaces.this,
					XhulooActivity_PHONE.class);
			break;

		case Menu.FIRST + 5:
			startACIntent.stAcIntent(XhulooActivity_myplaces.this,
					XAplasma.class);

			break;

		case Menu.FIRST + 6:

			startACIntent.stAcIntent(XhulooActivity_myplaces.this,
					wo.wocom.xwell.location.XA_p_lo_simpleGPS.class);

			break;

		case Menu.FIRST + 7:
			startACIntent.stAcIntent(XhulooActivity_myplaces.this,
					wo.wocom.xwell.database.SQLEditUI.class);

			break;
		case Menu.FIRST + 8:
			startACIntent.stAcIntent(XhulooActivity_myplaces.this,
					wo.wocom.xwell.security.XA_SEC_AES.class);
			break;

		case Menu.FIRST + 9:
			startACIntent.stAcIntent(XhulooActivity_myplaces.this,
					wo.wocom.xwell.admin.DevicePolicy.class);
			break;

		case Menu.FIRST + 10:
			startACIntent.stAcIntent(XhulooActivity_myplaces.this,
					wo.wocom.xwell.GUItool.GT_loadjar.class);
			break;

		case Menu.FIRST + 11:

			ComponentName component = new ComponentName(this,
					wo.wocom.xwell.service.SV_hotkeyService.class);
			// 组件名称，intent会根据component
			// name启动一个组件（activity,service,contentProvider）

			Intent mIntent01 = new Intent();
			mIntent01.setComponent(component);
			startService(mIntent01);

			break;

		}

		return false;

	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		Toast.makeText(this, "MP_选项菜单关闭", Toast.LENGTH_SHORT).show();
	}

	/*
	 * 文件游览功能的 打开指定PATH 定义在外面
	 */
	public ArrayList<String> open_path(String file_path, ArrayList<String> item) {

		File[] file_index_f = new File(file_path).listFiles(); // 列出file_path下的文件

		item.add(getString(R.string.to_top)); // 最前添加一个

		for (File file : file_index_f) {// for (循环变量类型 循环变量名称 : 要被遍历的对象) 循环体
			item.add(file.getPath());
		}
		return item;

	}

}