package wo.wocom.xwell.admin;

import wo.wocom.xwell.R;
import android.app.Activity;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @author wuwenjie
 * @date 20130328
 * @version 1.3.10.3.17:1
 * @more DevicePolicy;设备策略管理;锁屏
 * 
 */
public class DevicePolicy extends Activity {

	String TAG = "PAC_AD_DP";
	Button startPermissionBtn;
	Button stopPermissionBtn;
	Button sysLockBtn;
	private DevicePolicyManager dpm;
	private ComponentName compName;// 组件名称,intent根据compName,启动组件

	// （activity,service,contentProvider）

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pac_ad_dp);

		// 初始化
		sysLockBtn = (Button) findViewById(R.id.pac_ad_dp_bt1);
		startPermissionBtn = (Button) findViewById(R.id.pac_ad_dp_bt2);
		stopPermissionBtn = (Button) findViewById(R.id.pac_ad_dp_bt3);

		// 取得系统服务,设备策略管理
		dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		compName = new ComponentName(this, AdminReceiver.class);

	}

	// Button bt1 监听方法 android:onClick="bt1_onClick"
	public void bt1_onClick(View v) {
		boolean active = dpm.isAdminActive(compName);
		// 组件是否获得管理员
		if (active) {
			dpm.lockNow();// 锁定
			dpm.setMaximumTimeToLock(compName, 60000);
			// 设置最大解锁时间
		}
		Log.i(TAG, "DevicePolicy locked");

	}

	public void bt2_onClick(View v) {
		startDeviceManager();
	}

	public void bt3_onClick(View v) {
		stopDeviceManager();
	}

	public void bt4_onClick(View v) {
		if (dpm.isAdminActive(compName)) {
			dpm.resetPassword("wwj", DeviceAdminInfo.USES_POLICY_RESET_PASSWORD);
			// 重置密码
		}
	}

	public void bt5_onClick(View v) {
		if (dpm.isAdminActive(compName)) {
			dpm.resetPassword("wwj2",
					DeviceAdminInfo.USES_POLICY_RESET_PASSWORD);
			// 重置密码
		}
	}

	// setStorageEncryption

	// 启动设备管理权限
	private void startDeviceManager() {

		Log.i(TAG, "DevicePolicy startDeviceManager");

		// 添加一个隐式意图，完成设备权限的添加
		// 这个Intent (DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)跳转到 权限提醒页面
		// 并传递了两个参数EXTRA_DEVICE_ADMIN 、 EXTRA_ADD_EXPLANATION
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

		// 权限列表
		// EXTRA_DEVICE_ADMIN参数中说明用到哪些权限，
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);

		// 描述(additional explanation)
		// EXTRA_ADD_EXPLANATION参数为附加的说明
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"DevicePolicyManager附加的说明");

		startActivityForResult(intent, 0);
		// 返回结果的跳转
	}

	// 禁用设备管理权限方法实现

	private void stopDeviceManager() {
		Log.i(TAG, "DevicePolicy stopDeviceManager");
		if (dpm.isAdminActive(compName)) {
			dpm.removeActiveAdmin(compName);
		}
	}// stopDeviceManager

}
