package wo.wocom.xwell.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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

			while (!NetInfo.isAvailable()) {
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
}
