package wo.wocom.xwell.admin;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @author wuwenjie
 * @date 20130328
 * @version 1.3.10.3.17:1
 * @more DeviceAdminReceiver 设备管理员接收者
 * 
 */
public class AdminReceiver extends DeviceAdminReceiver {

	public DevicePolicyManager getManager(Context context) {
		Log.i("pac_ad_adminRec", "AdminReceiver getManager()");
		Toast.makeText(context, "getManager", Toast.LENGTH_SHORT).show();

		return super.getManager(context);
	}

	public ComponentName getWho(Context context) {
		Log.i("pac_ad_adminRec", "AdminReceiver getWho()");
		Toast.makeText(context, "getWho", Toast.LENGTH_SHORT).show();

		return super.getWho(context);
	}

	// 禁用

	public void onDisabled(Context context, Intent intent) {
		Log.i("pac_ad_adminRec", "AdminReceiver onDisabled()");
		Toast.makeText(context, "禁用设备管理onDisabled", Toast.LENGTH_SHORT).show();

		super.onDisabled(context, intent);
	}

	public CharSequence onDisableRequested(Context context, Intent intent) {
		Log.i("pac_ad_adminRec", "AdminReceiver onDisableRequested()");
		Toast.makeText(context, "onDisableRequested", Toast.LENGTH_SHORT)
				.show();

		return super.onDisableRequested(context, intent);
	}

	// 激活

	public void onEnabled(Context context, Intent intent) {
		Log.i("pac_ad_adminRec", "AdminReceiver onEnabled()");
		Toast.makeText(context, "启动设备管理onEnabled", Toast.LENGTH_SHORT).show();

		super.onEnabled(context, intent);
	}

	public void onPasswordChanged(Context context, Intent intent) {
		Log.i("pac_ad_adminRec", "AdminReceiver onPasswordChanged()");
		Toast.makeText(context, "onPasswordChanged", Toast.LENGTH_SHORT).show();

		super.onPasswordChanged(context, intent);
	}

	public void onPasswordFailed(Context context, Intent intent) {
		Log.i("pac_ad_adminRec", "AdminReceiver onPasswordFailed()");
		Toast.makeText(context, "onPasswordFailed", Toast.LENGTH_SHORT).show();

		super.onPasswordFailed(context, intent);
	}

	public void onPasswordSucceeded(Context context, Intent intent) {
		Log.i("pac_ad_adminRec", "AdminReceiver onPasswordSucceeded()");
		Toast.makeText(context, "onPasswordSucceeded", Toast.LENGTH_SHORT)
				.show();

		super.onPasswordSucceeded(context, intent);
	}

	//获得
	public void onReceive(Context context, Intent intent) {
		Log.i("pac_ad_adminRec", "AdminReceiver onReceive()");
		Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();

		super.onReceive(context, intent);
	}

	public IBinder peekService(Context myContext, Intent service) {
		Log.i("pac_ad_adminRec", "AdminReceiver peekService()");
		Toast.makeText(myContext, "peekService", Toast.LENGTH_SHORT).show();

		return super.peekService(myContext, service);
	}

}