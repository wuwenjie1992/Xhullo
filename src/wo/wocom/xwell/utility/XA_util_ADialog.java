package wo.wocom.xwell.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.2
 * @more 自定义 警告对话框
 */
public class XA_util_ADialog extends AlertDialog {
	
	//XA_util_ADialog alog=new XA_util_ADialog(XA_sens_sensorExits.this);
	//	alog.show1ADialog("出错", "传感器不存在或未知","好的");
	
	public XA_util_ADialog(Context context) {
		super(context);
	}

	// 一个按钮
	public void show1ADialog(String title, String message, String text) {

		this.setTitle(title);
		this.setMessage(message);

		this.setButton(text, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();// 取消警告框
			}
		});// setButton end

		this.show();
		return;
	}// show1ADialog end

}
