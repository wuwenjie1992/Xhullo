package wo.wocom.xwell.utility;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.1
 * @more 自定义 进程对话框
 */
public class XA_util_ProgressDialog extends ProgressDialog {

	// private XA_util_ProgressDialog XA_util_pd ; // 自定义进度对话框
	// XA_util_pd = new XA_util_ProgressDialog(context); //实例化 自定义
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

	// 取消对话框
	public void util_pDialogCancel() {
		this.cancel();
	}

}