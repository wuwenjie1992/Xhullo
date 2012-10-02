package wo.wocom.xwell;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.2
 * @see		自定义的相机视图；按键处理；捕捉图像
 */

public class XA_camera extends Activity {

	private static final String TAG = "XA_camera";
	private CameraView cv;	//自定义的相机视图
	private Camera mCamera = null;//相机对象
	private Bitmap mBitmap = null;//Bitmap对象
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);		//窗口无标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//窗口全屏，Hide all screen decorations
		getWindow().setFormat(PixelFormat.TRANSLUCENT);//设置窗口格式为半透明
		
		
		FrameLayout fl = new FrameLayout(this);	//提供一个帧布局 
		cv = new CameraView(this);fl.addView(cv);	//创建预览用子类，放于fl底层
		
		TextView tv = new TextView(this);			//创建文本框做特效
		tv.setText("请按\"相机\"按钮ORVOLUME_DOWN");
		fl.addView(tv);
		
		setContentView(fl);		//设置Activity的根内容视图
								}//oncreate end

	
//保存图片的PictureCallback对象；回调接口，用于提供从camera拍摄到的图像数据。
public Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i(TAG,"onPictureTaken");
			Toast.makeText(getApplicationContext(), "正在保存……", Toast.LENGTH_SHORT).show();
		    mBitmap = BitmapFactory.decodeByteArray(data,0,data.length);
			//BitmapFactory.decodeByteArray()将相机传回数据转换成Bitmap对象，解码字节数组
			new DateFormat();
			File file = new File("/sdcard/camera"+ 
									DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.US))+".jpg");
			//把Bitmap保存于文件
			//getInstance获取实例 RETURN： a Calendar subclass instance现在时间
		   try {
		        file.createNewFile();//返回：真，新建了。假，已存在。抛错IOException，不能新建
		        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));//缓冲输出
		        mBitmap.compress(Bitmap.CompressFormat.JPEG,100,os);//将一个压缩为特定版本的位图写到OutputStream
		        os.flush();//刷新该流的保证所有未决数据被写入目标流
		        os.close();//Closes this stream.
		        Toast.makeText(getApplicationContext(), "图片保存", Toast.LENGTH_SHORT).show();
		   		} catch (IOException e) {e.printStackTrace();}
		   
			mCamera.startPreview();//再次 开始预览，无此，view停留takpicture图像
		   
				}	//on picture taken end
			};
			//实列化 end

//回调接口,实际捕捉信号图像时,快门
public Camera.ShutterCallback shutterCallback=new Camera.ShutterCallback() {
	
	public void onShutter() {
		Log.i(TAG,"onShutter");
         /*震动，获取震动硬件服务*/
		final Vibrator vi=(Vibrator)getSystemService(VIBRATOR_SERVICE);
		vi.vibrate(85);//震动开始
	}
};

		
//相机按键按下的事件处理方法
public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG,"XA_camera.onKeyDown");
		if (keyCode==KeyEvent.KEYCODE_CAMERA|keyCode==KeyEvent.KEYCODE_VOLUME_DOWN) {
			if (mCamera != null) {
				Log.i(TAG,"mCamera.takePicture");
				//执行相机对象的takePicture()
				mCamera.takePicture(shutterCallback, null, pictureCallback);
			}
		}
		return cv.onKeyDown(keyCode, event);
	}


// 照相视图
class CameraView extends SurfaceView {
	
	private SurfaceHolder holder = null;
	//构造函数
	public CameraView(Context context) {
		super(context);
		Log.i(TAG,"CameraView");
		holder =this.getHolder();		// 操作surface的holder
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 设置Push缓冲类型
		
		//Callback，实现它可获取表面变化的信息
		holder.addCallback(new SurfaceHolder.Callback() {
				
			//当预览视图创建		
			public void surfaceCreated(SurfaceHolder holder) {
						
					Log.i(TAG,"CameraView_surfaceCreated");
					mCamera = Camera.open();
					try {mCamera.setPreviewDisplay(holder);//设置用于实时预览
					} catch (IOException e) {mCamera.release();mCamera = null;}// 释放相机资源并置空
																			}
			//当视图数据发生变化
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
						
					Log.i(TAG,"CameraView_surfaceChanged");
					Camera.Parameters parameters = mCamera.getParameters();	//获得相机参数对象
					parameters.setPictureFormat(PixelFormat.JPEG);			//设置格式
					parameters.setPreviewSize(480,320);			//设置预览大小
					parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);	//设置自动对焦
					parameters.setPictureSize(1024,480);			//设置图片保存时的分辨率大小
					mCamera.setParameters(parameters);		//给相机对象设置刚才设定的参数
					mCamera.startPreview();				//开始预览
				}//surfaceChanged end
					
			public void surfaceDestroyed(SurfaceHolder holder) {
						
					Log.i(TAG,"CameraView_surfaceDestroyed");
					mCamera.stopPreview();// 停止预览
					mCamera.release();// 释放相机资源并置空
					mCamera = null;}
					
			});//SurfaceHolder.Callback end
			
       }
	
}//camera view end

	
}//XA_camera end