package wo.wocom.xwell;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.4
 * @see 广播接收，启动程序
 */

public class XA_bootBroadcastRec extends BroadcastReceiver {

	static final String ACTION_boot = "android.intent.action.BOOT_COMPLETED";
	static final String TAG = "XA_BBR";

	// 当 BroadcastReceiver 收到一个 Intent 广播
	public void onReceive(Context con, Intent arg1) {

		if (arg1.getAction().equals(ACTION_boot)) {

			Log.i(TAG, "XA_BBR will start new task");
			Intent boot_Intent = new Intent(con,
					wo.wocom.xwell.sensor.XA_sens_sensorExits.class);
			boot_Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// become the start of a new task on this history stack
			con.startActivity(boot_Intent); // 启动activity
		}
	}// onReceive end

	@Override
	public IBinder peekService(Context myContext, Intent service) {
		// TODO Auto-generated method stub
		return super.peekService(myContext, service);
	}

}// XA_BBR end