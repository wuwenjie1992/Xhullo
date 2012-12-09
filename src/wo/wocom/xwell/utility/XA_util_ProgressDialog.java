package wo.wocom.xwell.utility;

import android.app.ProgressDialog;
import android.content.Context;

public class XA_util_ProgressDialog extends ProgressDialog {

	public XA_util_ProgressDialog(Context context) {
		super(context);
	}

	public void setloadingstyle(String title, String message, int icon) {

		this.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 采用了圆形，旋转的进度条
		this.setTitle(title);
		this.setMessage(message);
		this.setIcon(icon);
		this.setIndeterminate(true);// 进度条是否不明确
		this.setCancelable(true);// cancelable with the BACK key.
		return;
	}

	// ProgressDialog this=new ProgressDialog(this);
	public void util_pDialogCancel() {
		this.cancel();
	}

}