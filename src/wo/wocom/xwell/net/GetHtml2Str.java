package wo.wocom.xwell.net;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.10.3.8:1
 * @more
 */
public class GetHtml2Str {

	public static String TAG = "pac_net_GH2S";

	/**
	 * @param html_s
	 *            返回字符串
	 * @param url
	 *            请求地址
	 * @param con
	 *            使用其的Activity的Context 上下文
	 */
	public static String reStr(String html_s, String url, Context con) {

		if (isConnectNet.isConInt(con)) {// 判断是否联网

			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			try {
				// 得到HttpResponse对象
				HttpResponse httpResponse = httpClient.execute(httpGet);
				// HttpResponse的返回结果若成功
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// 得到返回数据的字符串
					String data_s = EntityUtils.toString(httpResponse
							.getEntity(),"UTF-8");
					html_s = data_s;
					Log.i(TAG, "data_s" + data_s);
				}
			} catch (java.net.UnknownHostException e) {
				e.printStackTrace();
				Log.i(TAG, "UnknownHost：" + e.toString());
				html_s = null;
			} catch (IOException e) {
				e.printStackTrace();
				html_s = null;
			}// catch
			return html_s;
		}// if

		else {
			return null;
			// alarm
			// finish();
		}
	}// reStr
}
