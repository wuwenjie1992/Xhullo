package wo.wocom.xwell;

import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.5
 * @see 音乐服务;接受其他activity的调用，返回结果；播放通知
 */

public class MusicService extends Service {

	private static final String TAG = "MUSIC_SERVICE_Xhuloo";
	public static final String PLAY_ACTION = "wo.wocom.xwell.PLAY_ACTION";
	public static final String PAUSE_ACTION = "wo.wocom.xwell.PAUSE_ACTION";
	public static final String NEXT_ACTION = "wo.wocom.xwell.NEXT_ACTION";
	public static final String PREVIOUS_ACTION = "wo.wocom.xwell.PREVIOUS_ACTION";
	public static final String URL_ACTION = "wo.wocom.xwell.URL_ACTION";

	@SuppressLint("SdCardPath")
	String mp3filePath_s_P = "/sdcard/music/千纸鹤.mp3";// 真实播放路径
	String mp3filePath_s = null;

	private MediaPlayer MediaPlayer_MS;
	private MyBinder mBinder = new MyBinder();

	public IBinder onBind(Intent intent) {
		MediaPlayer_MS.start();
		Log.i(TAG, "onBind start IBinder~~~bindservice_bt");
		return mBinder;
	}

	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "start onUnbind MUSIC_SERVICE_Xhuloo");
		return super.onUnbind(intent);
	}

	// 第一次启动Service时，先后调用onCreate()--->onStart()
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate MUSIC_SERVICE_Xhuloo");
		MediaPlayer_MS = new MediaPlayer();
	}

	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);

		String mp3filePath_s = intent.getStringExtra("PM_URL");
		Log.i(TAG, "onCreate MUSIC_SERVICE_Xhuloo" + mp3filePath_s);

		String action = intent.getAction();

		if (action.equals(PLAY_ACTION)) {

			// ---------------通知---------------------
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// 得到系统NOTIFICATION_SERVICE服务，通知栏

			Notification notification = new Notification(
					R.drawable.ic_launcher, "播放音乐", System.currentTimeMillis());
			// 实列化 Notifiction，图标，tickerText，when何时

			RemoteViews remoteViews = new RemoteViews(getPackageName(),
					R.layout.notification);
			// 远程视图 可显示于另一个进程 String packageName, int layoutId
			// public abstract String getPackageName () 获得该应用的包名

			// remoteViews.setImageViewResource
			// (R.id.ms_notifi_image,android.R.drawable.ic_lock_silent_mode_off);
			// remoteViews.setTextViewText(R.id.ms_notifi_text,
			// "播放音乐ms_notifi_text");

			notification.contentView = remoteViews;// remoteViews将在状态栏里代表本通知视图

			notification.contentIntent = PendingIntent.getActivity(
					MusicService.this, 0, new Intent(MusicService.this,
							XhulooActivity_playmusic.class), 0);
			// 启动一个新的活动

			notification.flags = Notification.FLAG_AUTO_CANCEL;// 按了自动消除通知
			notification.defaults = Notification.DEFAULT_SOUND;// 提示声为默认
			long[] vibrate = { 0, 100, 200, 300 };// 颤动
			notification.vibrate = vibrate;

			manager.notify(1, notification);

			inite();// 初始化播放

		}

		else if (action.equals(PAUSE_ACTION)) {
			stopSelf(); // pause();
		}

		else if (action.equals(NEXT_ACTION)) {
			inite();
		}

		else if (action.equals(PREVIOUS_ACTION)) {
			inite(); // previous();
		} else if (action.equals(URL_ACTION)) {

			if (mp3filePath_s != null) {
				mp3filePath_s_P = mp3filePath_s;// 文件路径字符串
			}
			inite(); // 初始化 播放 音乐

		}

	}// onstart end

	public void onDestroy() {
		super.onDestroy();
		MediaPlayer_MS.release();
		// 服务结束时要释放MediaPlayer
	}

	// 自定义 初始化 播放 音乐
	public void inite() {
		MediaPlayer_MS.reset();

		try {
			MediaPlayer_MS.setDataSource(mp3filePath_s_P);// 文件路径字符串
			MediaPlayer_MS.prepare();
			MediaPlayer_MS.start();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
			AlertDialog.Builder my_ADialog = new AlertDialog.Builder(
					MusicService.this);
			;
			my_ADialog.setTitle("出错");
			my_ADialog.setMessage(e1.toString());
			my_ADialog.setCancelable(true);
			my_ADialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			my_ADialog.show();
		}

	}// inite end

	// 自定义Binder类，这个是一个内部类，
	// 所以可以知道其外围类的对象，通过这个类，让Activity知道其Service的对象
	public class MyBinder extends Binder {
		MusicService getService() { // 返回Activity所关联的Service对象，
			// 在对应的Activity里就可调用Service里一些公用方法和公用属性
			return MusicService.this;
		}
	}

	// 自定义
	public String getSystemTime() {
		Time t = new Time();
		t.setToNow();
		return t.toString();
	}

}
