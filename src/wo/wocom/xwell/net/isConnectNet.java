package wo.wocom.xwell.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.10.3.8:2.1
 * @more 判断网络是否连接,本机ip
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

			while (!NetInfo.isAvailable()) { // 当网络不可达时
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

	/**
	 * Get IP address from first non-localhost interface
	 * 
	 * @param ipv4
	 *            true=return ipv4, false=return ipv6
	 * @return address or empty string
	 */
	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());

			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf
						.getInetAddresses());

				for (InetAddress addr : addrs) {

					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress().toUpperCase();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 port
																// suffix
								return delim < 0 ? sAddr : sAddr.substring(0,
										delim);
							}
						}
					}// if end

				}// for 2 end
			}// for end
		} catch (Exception ex) {
		} // for now eat exceptions
		return "";
	}

}