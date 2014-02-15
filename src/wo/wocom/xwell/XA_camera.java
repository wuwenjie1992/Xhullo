package wo.wocom.xwell;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.2:1.3.10.3.21:2
 * @see 自定义的相机视图；按键处理；捕捉图像;更换聚焦模式
 */

public class XA_camera extends Activity {

	private static final String TAG = "XA_camera";
	private CameraView cv; // 自定义的相机视图
	private Camera mCamera = null;// 相机对象
	private Bitmap mBitmap = null;// Bitmap对象
	Camera.Parameters parameters; // 相机参数对象
	ImageButton ib;// 微距按键

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 窗口无标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 窗口全屏，Hide all screen decorations
		getWindow().setFormat(PixelFormat.TRANSLUCENT);// 设置窗口格式为半透明

		FrameLayout fl = new FrameLayout(this); // 提供一个帧布局
		cv = new CameraView(this); // 在新建视图的同时，实例化了Camera
		fl.addView(cv); // 创建预览用子类，放于fl底层

		LinearLayout ll = new LinearLayout(this);

		TextView tv = new TextView(this); // 创建文本框做特效
		tv.setText(R.string.XA_camera_note);// 请按音量下
		ll.addView(tv);

		ImageButton ib = new ImageButton(this);
		ib.setImageResource(R.drawable.about);
		ll.addView(ib);

		fl.addView(ll);
		setContentView(fl); // 设置Activity的根内容视图

		// 图片按钮监听
		ib.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				parameters = mCamera.getParameters(); // 获得相机参数对象
				if (parameters.getFocusMode().equals(
						Camera.Parameters.FOCUS_MODE_AUTO)) {// 若自动聚焦
					parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
				} else {
					parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
				}

				mCamera.setParameters(parameters); // 给相机对象设置刚才设定的参数
				mCamera.startPreview(); // 开始预览

			}
		});

	}// oncreate end

	// 保存图片的PictureCallback对象；回调接口，用于提供从camera拍摄到的图像数据。
	public Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i(TAG, "onPictureTaken");
			Toast.makeText(getApplicationContext(), R.string.XA_camera_save,
					Toast.LENGTH_SHORT).show();
			mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			// BitmapFactory.decodeByteArray()将相机传回数据转换成Bitmap对象，解码字节数组
			new DateFormat();
			File file = new File("/sdcard/camera"
					+ DateFormat.format("yyyyMMdd_hhmmss",
							Calendar.getInstance(Locale.US)) + ".jpg");
			// 把Bitmap保存于文件
			// getInstance获取实例 RETURN： a Calendar subclass instance现在时间
			try {
				file.createNewFile();// 返回：真，新建了。假，已存在。抛错IOException，不能新建
				BufferedOutputStream os = new BufferedOutputStream(
						new FileOutputStream(file));// 缓冲输出
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);// 将一个压缩为特定版本的位图写到OutputStream
				os.flush();// 刷新该流的保证所有未决数据被写入目标流
				os.close();// Closes this stream.
				Toast.makeText(getApplicationContext(),
						R.string.XA_camera_savef, Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}

			mCamera.startPreview();// 再次 开始预览，无此，view停留takpicture图像

		} // on picture taken end
	};// 实列化 end

	// 回调接口,实际捕捉信号图像时,快门
	public Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {

		public void onShutter() {
			Log.i(TAG, "onShutter");
			mCamera.autoFocus(AutoFocusCallBack);
			/* 震动，获取震动硬件服务 */
			final Vibrator vi = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vi.vibrate(85);// 震动开始
		}
	};

	// 自动对焦
	private Camera.AutoFocusCallback AutoFocusCallBack = new Camera.AutoFocusCallback() {

		public void onAutoFocus(boolean success, Camera camera) {
			if (success) { // 对焦成功
				Log.i(TAG, "AutoFocusCallback success then tikePic");
				mCamera.takePicture(shutterCallback, null, pictureCallback);
			}
		}
	};

	// 相机按键按下的事件处理方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "XA_camera.onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_CAMERA
				| keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			if (mCamera != null) {
				Log.i(TAG, "onKeyDown mCamera.takePicture");
				//自动对焦后拍照
				mCamera.autoFocus(AutoFocusCallBack);
				//stackoverflow.com/questions/5878042/android-camera-autofocus-on-demand
			}
		}
		return cv.onKeyDown(keyCode, event);
	}

	public int getRotation() {
		return this.getWindowManager().getDefaultDisplay().getRotation();
	}

	// 照相视图
	class CameraView extends SurfaceView {

		private SurfaceHolder holder = null;

		// 构造函数
		public CameraView(Context context) {
			super(context);
			Log.i(TAG, "CameraView");
			holder = this.getHolder(); // 操作surface的holder
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 设置Push缓冲类型

			// Callback，实现它可获取表面变化的信息
			holder.addCallback(new SurfaceHolder.Callback() {

				// 当预览视图创建
				public void surfaceCreated(SurfaceHolder holder) {

					Log.i(TAG, "CameraView_surfaceCreated");
					mCamera = Camera.open();
					try {
						mCamera.setPreviewDisplay(holder);// 设置用于实时预览
					} catch (IOException e) {
						Log.i(TAG,
								"surfaceCreated_setPreviewDisplay"
										+ e.toString());
						mCamera.release();
						mCamera = null;
					}// 释放相机资源并置空
				}

				// 当视图数据发生变化
				public void surfaceChanged(SurfaceHolder holder, int format,
						int width, int height) {

					Log.i(TAG, "CameraView_surfaceChanged");

					parameters = mCamera.getParameters(); // 获得相机参数对象
					parameters.setPictureFormat(PixelFormat.JPEG); // 设置格式
					parameters.set("jpeg-quality", 85);// 照片质量
					parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO); // 设置自动对焦
					parameters.setPreviewSize(width, height);
					parameters.setPreviewFrameRate(48);// 每秒48帧

					// setDisplayOrientation
					Log.i(TAG, "getRotation" + getRotation());

					// 设置图片保存时的分辨率大小
					List<Size> previewSizes_L = parameters
							.getSupportedPreviewSizes();
					Log.i(TAG, "\n" + previewSizes_L.size() + "\n"
							+ previewSizes_L.get(0));
					Size s = previewSizes_L.get(0);// 640 480 ;0 320 240
					Log.i(TAG, s.width + "X" + s.height);
					// parameters.setPictureSize(s.width,s.height);//默认分辨率高

					mCamera.setParameters(parameters); // 给相机对象设置刚才设定的参数
					mCamera.startPreview(); // 开始预览

					float[] output = new float[3];
					parameters.getFocusDistances(output);
					Log.i(TAG, "getFocusDistances" + output[0] + ":"
							+ output[1] + ":" + output[2]);
					// Camera和作为焦点对象之间的距离

				}// surfaceChanged end

				public void surfaceDestroyed(SurfaceHolder holder) {

					Log.i(TAG, "CameraView_surfaceDestroyed");
					mCamera.stopPreview();// 停止预览
					mCamera.release();// 释放相机资源并置空
					mCamera = null;
				}

			});// SurfaceHolder.Callback end

		}

	}// camera view end

}// XA_camera end