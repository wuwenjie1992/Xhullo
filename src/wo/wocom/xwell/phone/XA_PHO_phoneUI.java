package wo.wocom.xwell.phone;

import wo.wocom.xwell.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author wuwenjie
 * @date 20130623
 * @version 1.3.10.3.20:1
 * @more
 */
public class XA_PHO_phoneUI extends Activity {

	EditText et, et_n;
	Button bt;
	SmsManager sms;
	TelephonyManager tm;

	// 生命开始
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pac_ph_ui); // 设置主布局

		et = (EditText) findViewById(R.id.pac_ph_ui_et1);
		et_n = (EditText) findViewById(R.id.pac_ph_ui_et2);
		bt = (Button) findViewById(R.id.pac_ph_ui_bt);

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		et_n.setText(tm.getLine1Number());

	}

	public void bt_onClick(View v) {

		sms = SmsManager.getDefault();

		// 创造sentIntent参数
		Intent sentIntent = new Intent("SENT_SMS_ACTION");
		PendingIntent sentPI = PendingIntent.getBroadcast(
				getApplicationContext(), 0, sentIntent, 0);

		// 创造deliveryIntent参数
		Intent deliveryIntent = new Intent("DELIVERED_SMS_ACTION");
		PendingIntent deliverPI = PendingIntent.getBroadcast(
				getApplicationContext(), 0, deliveryIntent, 0);

		sms.sendTextMessage(et_n.toString(), null, et.toString(), sentPI,
				deliverPI);

	}

	// 短信接收器
	public class SmsReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			// 判断是短信发送后的广播信息还是短信到达后的广播信息

			if ("SENT_SMS_ACTION".equals(intent.getAction())) {
				switch (getResultCode()) {
				// 如果发送成功
				case Activity.RESULT_OK:
					Toast.makeText(context, "发送成功", Toast.LENGTH_LONG).show();
					break;
				// 如果发送失败
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(context, "发送失败", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}

			} else if ("DELIVERED_SMS_ACTION".equals(intent.getAction())) {
				switch (getResultCode()) {
				// 如果短信已经到达
				case Activity.RESULT_OK:
					Toast.makeText(context, "短信已经到达", Toast.LENGTH_LONG).show();
					break;
				// 如果短信未到达
				default:
					Toast.makeText(context, "短信未到达", Toast.LENGTH_SHORT).show();

					break;
				}

			}

		}

	}// SmsReceiver

}
