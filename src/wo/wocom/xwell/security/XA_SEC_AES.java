package wo.wocom.xwell.security;

import wo.wocom.xwell.R;
import wo.wocom.xwell.utility.XA_util_ADialog;
import wo.wocom.xwell.utility.XA_util_aes;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author wuwenjie
 * @date 20130316
 * @version 1.3.10.3.16:1
 * @more AES加密方法 图形界面
 * 
 */
public class XA_SEC_AES extends Activity {

	EditText et_key, et_in;
	Button bt1, bt2;
	TextView tv;
	String key, target, show;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.pac_sec_aes);
		setTitle(R.string.pac_sec_t);

		et_key = (EditText) findViewById(R.id.pac_sec_aes_et1);
		et_in = (EditText) findViewById(R.id.pac_sec_aes_et2);
		bt1 = (Button) findViewById(R.id.pac_sec_aes_bt1);
		bt2 = (Button) findViewById(R.id.pac_sec_aes_bt2);
		tv = (TextView) findViewById(R.id.pac_sec_aes_tv1);

		et_key.setText("wuwenjiewuwenjie");
		et_in.setText("0140B3D76DCDD7CB07025FC05F420D"
				+ "095BD41D703EF1F5549BADC4F3FF947425"
				+ "DB7E8400CFEC1347161DD017013C1D6B");

	}

	// Button bt1 监听方法 android:onClick="bt1_onClick"
	public void bt1_onClick(View v) {
		// java.lang.IllegalStateException: Could not find a method
		// bt2_onClick(View) in the activity class
		// wo.wocom.xwell.security.XA_SEC_AES for onClick handler on view class
		// android.widget.Button with id 'pac_sec_aes_bt2'
		encryptOrDe(true);
	}

	// Button bt2 监听方法 android:onClick="bt2_onClick"
	public void bt2_onClick(View v) {
		encryptOrDe(false);
	}

	// 是否执行加密操作
	public void encryptOrDe(Boolean b) {

		key = et_key.getText().toString();
		target = et_in.getText().toString();

		if (key == null || target == null) { // 只要有一个为空
			XA_util_ADialog alog = new XA_util_ADialog(XA_SEC_AES.this);
			alog.show1ADialog(
					XA_SEC_AES.this.getString(R.string.pac_sec_warning),
					XA_SEC_AES.this.getString(R.string.pac_sec_w),
					XA_SEC_AES.this.getString(R.string.goback));
		} else {
			XA_util_aes.CRYPT_KEY = key;

			// Log.i("crypt", key);

			if (b) {
				// Log.i("encrypt", target);
				show = XA_util_aes.encrypt(target);
				et_in.setText(show);
			} else {
				// Log.i("decrypt", target);
				show = XA_util_aes.decrypt(target);
				et_in.setText(show);
			}

			tv.setText(show);
		}

	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onRestart() {
		super.onRestart();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {
		super.onStop();
	}

}
