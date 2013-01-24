package wo.wocom.xwell.utility;

import wo.wocom.xwell.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.10.3.10:2
 * @more
 */
public class Util_Notifiy extends Notification {

	/*
	 * Util_Notifiy nf = new Util_Notifiy( XhulooActivity_weatherreport.this);
	 * 
	 * nf.Util_simpleNotifiy(XhulooActivity_weatherreport.this,
	 * XhulooActivity_weatherreport.class, "预警", get_url_weaAlarm);
	 */

	public Util_Notifiy(Context context) {
		super();
	}

	public void Util_simpleNotifiy(Context con, Class<?> startcls,
			String tickerText, String notify) {

		NotificationManager nomanager = (NotificationManager) con
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 得到系统NOTIFICATION_SERVICE服务，通知栏

		Notification notification = new Notification(R.drawable.ic_launcher,
				tickerText, System.currentTimeMillis());
		// 实列化 Notifiction，图标，tickerText，when何时

		RemoteViews remoteViews = new RemoteViews(con.getPackageName(),
				R.layout.notification);
		// 远程视图 可显示于另一个进程 String packageName, int layoutId
		// public abstract String getPackageName () 获得该应用的包名
		// int layoutId R.layout.notification 设置布局

		// remoteViews.setImageViewResource
		// (R.id.ms_notifi_image,android.R.drawable.ic_lock_silent_mode_off);
		remoteViews.setTextViewText(R.id.ms_notifi_text, notify);

		notification.contentView = remoteViews;	//remoteViews将在状态栏里代表本通知视图

		notification.contentIntent = PendingIntent.getActivity(con, 0,
				new Intent(con, startcls), 0);
		// 启动一个新的活动,不写会java.lang.IllegalArgumentException: contentIntent
		// required:

		notification.flags = Notification.FLAG_AUTO_CANCEL;// 按了自动消除通知
		notification.defaults = Notification.DEFAULT_SOUND;// 提示声为默认
		long[] vibrate = { 0, 100, 200, 300 };// 颤动
		notification.vibrate = vibrate;

		nomanager.notify(1, notification);
		return;

	}
}