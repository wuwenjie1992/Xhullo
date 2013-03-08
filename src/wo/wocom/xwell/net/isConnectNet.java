package wo.wocom.xwell.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.10.3.8:2.1
 * @more 判断网络是否连接
 */
public class isConnectNet {
	static String TAG = "PAC_NET_ISCONN";

	public static boolean isConInt(Context con) {

		ConnectivityManager conma = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo NetInfo = conma.getActiveNetworkInfo();

		// if (NetInfo != null);
		// for (boolean bool = NetInfo.isAvailable(); ;bool = false)
		// return bool;

		if (NetInfo != null) {
			Log.i(TAG, "NetInfo != null if:" + NetInfo);

			while (!NetInfo.isAvailable()) {	//当网络不可达时
				Log.i(TAG, "NetInfo.isAvailable()" + NetInfo.isAvailable());
				return false;
			}

		} else {
			Log.i(TAG, "NetInfo != null else:" + NetInfo);
			return false;
		}
		Log.i(TAG, "return true");
		return true;
	}// isConInt

	// public boolean requestRouteToHost (int networkType, int hostAddress)

}
