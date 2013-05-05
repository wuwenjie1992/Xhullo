package wo.wocom.xwell.service;

import wo.wocom.xwell.view.FloatView;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author wuwenjie
 * @date 20130421
 * @version 1.3.10.3.19:1
 * @more 浮动窗口服务
 */
public class SV_FloateViewService extends Service {

	String TAG = "SV_hotkeyService";

	private FloatView FV = null;

	// 第一次启动Service时，先后调用onCreate()--->onStart()
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, TAG + ":onCreate");
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Log.i(TAG, TAG + ":onStart");

		createView();

	}// onStart

	// 服务结束
	public void onDestroy() {
		super.onDestroy();

		Log.i(TAG, TAG + ":onDestroy");

	}

	public void onRebind(Intent intent) {
		super.onRebind(intent);

		Log.i(TAG, TAG + ":onRebind");

	}

	public boolean onUnbind(Intent intent) {

		Log.i(TAG, TAG + ":onUnbind");

		return super.onUnbind(intent);

	}

	// 绑定
	// Service与Activity通信:
	// 用到bindService和onBindService方法，
	// 分别执行了Service中IBinder()和onUnbind()方法
	public IBinder onBind(Intent arg0) {

		Log.i(TAG, TAG + ":onBind");

		return null;

	}

	private void createView() {

		FV = new FloatView(getApplicationContext());
		//返回当前进程应用的上下文全局对象
		FV.setImageResource(wo.wocom.xwell.R.drawable.home);
		FV.wm.addView(FV, FV.wmParams);
		

	}

}
