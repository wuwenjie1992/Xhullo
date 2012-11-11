package wo.wocom.xwell;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.1
 * @more 震动；内存信息
 */
public class XhulooActivity_MEM extends Activity {

	private static final String TAG = "MEM_Xhuloo";
	StringBuffer procinfo = new StringBuffer("运行进程：\n"); // 显示进程信息的stringbuffer

	/* activity生命周期 */
	public void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "MEM_onCreate------MEM_Xhuloo");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mem);

		final Button BC_btn_MEM = (Button) findViewById(R.id.BC_btn_MEM);
		final TextView MEM_textview = (TextView) findViewById(R.id.MEM_textview);

		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE); // 获得系统的activity服务
		ActivityManager.MemoryInfo mf = new ActivityManager.MemoryInfo(); // 可检索可用内存信息
		am.getMemoryInfo(mf);// 得到系统的内存情况

		List<RunningAppProcessInfo> procList = am.getRunningAppProcesses();// 获得正在运行进程信息，Returns
																			// a
																			// list

		for (int i = 0; i < procList.size(); i++) {
			procinfo.append(procList.get(i).processName)
					// 进程名
					.append("\t\t" + "pid:").append(procList.get(i).pid)
					// 进程号
					.append("\t" + "uid:").append(procList.get(i).uid)
					.append("\n");// 使用者号
		}

		/* 震动 */
		final Vibrator vi = (Vibrator) getSystemService(VIBRATOR_SERVICE); // 获取震动硬件服务
		long[] pattern = { 100, 30, 90, 25 }; // 震动时间 OFF/ON/OFF/ON...
		int repeat = 2; // 重复
		vi.vibrate(pattern, repeat);// 震动开始

		MEM_textview.setText("剩余内存(K):"
				+ (mf.availMem >> 10)
				+ "\n" // >>右移位运算符,除以2的10次方,1024
				+ "剩余内存(M):" + (mf.availMem >> 20) + "\n" + "低内存状态:"
				+ mf.lowMemory + "\n" + "低内存临界值：" + mf.threshold + "\n"
				+ "设备配置属性" + am.getDeviceConfigurationInfo().toString() + "\n"
				+ procinfo.toString()

		);

		/* 按钮监听 */
		BC_btn_MEM.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.BC_btn_MEM:
					XhulooActivity_MEM.this.finish();
					vi.cancel();

					break;

				default:
					break;
				}// switch
			}// onclick_end
		});

	}// onCreate

	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "MEM_onRestart");
		Toast.makeText(XhulooActivity_MEM.this, "MEM_onRestart",
				Toast.LENGTH_SHORT).show();
	}

	protected void onResume() {
		super.onResume();
		Log.i(TAG, "MEM_onResume");
		Toast.makeText(XhulooActivity_MEM.this, "MEM_onResume",
				Toast.LENGTH_SHORT).show();
	}

	protected void onPause() {
		super.onPause();
		Log.i(TAG, "MEM_onPause");
		Toast.makeText(XhulooActivity_MEM.this, "MEM_onPause",
				Toast.LENGTH_SHORT).show();
	}

	protected void onStop() {
		super.onStop();
		Log.i(TAG, "MEM_onStop");
		Toast.makeText(XhulooActivity_MEM.this, "MEM_onStop",
				Toast.LENGTH_SHORT).show();
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "MEM_onDestroy");
		Toast.makeText(XhulooActivity_MEM.this, "MEM_onDestroy",
				Toast.LENGTH_SHORT).show();
	}

}