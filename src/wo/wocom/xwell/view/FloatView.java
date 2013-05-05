package wo.wocom.xwell.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

/**
 * 
 * @author wuwenjie
 * @date 20130421
 * @version 1.3.10.3.19:2
 * @more	浮动窗口视图
 */
public class FloatView extends ImageView {

	private String TAG = "MFV";
	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;
	private boolean ismove = false;

	public WindowManager wm; // 窗口管理器
	public WindowManager.LayoutParams wmParams;

	// 窗口管理器布局参数

	public FloatView(Context context) {

		super(context);

		// 获取WindowManager
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		// 设置LayoutParams(全局变量）相关参数
		wmParams = new WindowManager.LayoutParams();

		wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT; // 设置window type
														// TYPE_SYSTEM_OVERLAY
		wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

		// 设置Window flag
		wmParams.flags = LayoutParams.FLAG_LAYOUT_NO_LIMITS
				| LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		// | LayoutParams.FLAG_NOT_FOCUSABLE;

		// FLAG_LAYOUT_NO_LIMITS 允许窗口扩展到屏幕之外 Constant Value: 512 (0x00000200)
		// FLAG_NOT_TOUCH_MODAL 不可触状态 Constant Value: 8 (0x00000008)
		// FLAG_WATCH_OUTSIDE_TOUCH 监视外部触动 Constant Value: 262144 (0x00040000)
		// FLAG_NOT_FOCUSABLE Constant Value: 8 (0x00000008)

		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

		wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
		// 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = 10;
		wmParams.y = 10;

		// 设置悬浮窗口长宽数据
		wmParams.width = 37;
		wmParams.height = 37;

		// 显示myFloatView图像
		// wm.addView(FV, wmParams);

	}

	public boolean onTouchEvent(MotionEvent event) {

		int pc = event.getPointerCount();
		Log.i(TAG,
				"getPointerCount:" + pc + "getPressure:" + event.getPressure());

		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY();

		long minus = event.getEventTime() - event.getDownTime();

		// Log.i(TAG,"ETminusDT:"+minus);

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

			ismove = false;
			// 获取相对View的坐标，即以此View左上角为原点
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			Log.i("startP", "startX" + mTouchStartX + ":startY" + mTouchStartY);
			break;

		case MotionEvent.ACTION_MOVE:

			if (minus > 150) {
				ismove = true;
				updateViewPosition();
			}

			break;

		case MotionEvent.ACTION_UP:

			if (ismove) {
				Log.i(TAG, "ETminusDT:" + minus);
				UVP_ACTION_UP(3, -3);
			}

			mTouchStartX = mTouchStartY = 0;

			break;

		case MotionEvent.ACTION_OUTSIDE:

			// FLAG_NOT_TOUCH_MODAL
			Log.i(TAG, "ACTION_OUTSIDE:" + x + ":" + y);

			break;

		}
		// return true;

		return super.onTouchEvent(event);

	}

	private void updateViewPosition() {
		// 更新浮动窗口位置参数
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		wm.updateViewLayout(this, wmParams);

	}

	private void UVP_ACTION_UP(int i, int j) {
		// 更新浮动窗口位置参数
		wmParams.x = (int) (x - mTouchStartX + i);
		wmParams.y = (int) (y - mTouchStartY + j);
		wm.updateViewLayout(this, wmParams);

	}

}