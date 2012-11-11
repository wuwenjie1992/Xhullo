package wo.wocom.xwell;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.4
 * @more NDK(Native Development Kit) JNI(Java Native Interface)
 */
public class XAplasma extends Activity {
	static String TAG = "XA_PL";

	public void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "XA_PL_oncreate!!");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pl);
		TextView textView = (TextView) findViewById(R.id.pl_tv);

		String myString = stringFromNDKJNI();// stringFromNDKJNI
		textView.setText(myString + "\n" + "time_t timer_null="
				+ currentTimeMillis());

		printLOGI();

	}// oncreat end

	static {
		try {
			System.loadLibrary("hello-ndk-jni");
		} catch (UnsatisfiedLinkError ule) {
			Log.i(TAG, "WARNING: Could not load lib!" + ule.toString());
		}
	}

	public native String stringFromNDKJNI();// 返回一个字符串

	public native void printLOGI();// logcat

	public native int currentTimeMillis();// 返回秒数
}
