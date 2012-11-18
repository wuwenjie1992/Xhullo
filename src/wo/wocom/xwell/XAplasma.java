package wo.wocom.xwell;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.5
 * @more NDK(Native Development Kit) JNI(Java Native Interface)
 * @via some codes from :Author: Frank Ableson Contact Info:
 *      fableson@navitend.com
 */
public class XAplasma extends Activity {
	static String TAG = "XA_PL";
	private ImageView ivDisplay = null;
	private Bitmap bitmapOrig = null;// 保存原始彩色图像。
	private Bitmap bitmapGray = null;// 保存图像的灰度副本，并且仅在 findEdges 例程中暂时使用
	private Bitmap bitmapWip = null;// 保存修改亮度值时的灰度图像

	public void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "XA_PL_oncreate!!");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pl);//设置界面布局
		TextView textView = (TextView) findViewById(R.id.pl_tv);
		ivDisplay = (ImageView) findViewById(R.id.pl_ivDisplay);

		/*
		 * BitmapFactory 方法有一个 options 参数， 支持您加载 ARGB 格式的图像。“A”表示 alpha 通道，
		 * “RGB”表示红色、绿色、蓝色。许多开源图像处理库需要 24 位彩色图像， 红色、绿色和蓝色各 8 位，并且每个像素由 RGB 三元组成。
		 * 每个值的范围为 0 至 255。Android 平台上的图像保存为 32 位整数 （alpha、红色、绿色和蓝色）。
		 */
		String pathName = Environment.getExternalStorageDirectory()
				+ "/bluetooth/Image/dj.jpg";//图像的url
		BitmapFactory.Options options = new BitmapFactory.Options();//选项
		options.inPreferredConfig = Config.ARGB_8888;//首选配置,ARGB_8888 Each pixel is stored on 4 bytes.
		bitmapOrig = BitmapFactory.decodeFile(pathName, options);
		if (bitmapOrig != null)
			ivDisplay.setImageBitmap(bitmapOrig);//Sets a Bitmap as the content of this ImageView.

		String myString = stringFromNDKJNI();// stringFromNDKJNI
		textView.setText(myString + "\n" + "time_t timer_null="
				+ currentTimeMillis());

		printLOGI();

	}// oncreat end

	// pl_btnReset按钮的动作；重置图像；android:onClick="onResetImage"
	public void onResetImage(View v) {
		Log.i(TAG, "onResetImage");
		ivDisplay.setImageBitmap(bitmapOrig);
		//Sets a Bitmap as the content of this ImageView
	}

    //pl_btnFindEdges；找到边界；android:onClick="onFindEdges"
	public void onFindEdges(View v) {
		Log.i(TAG, "onFindEdges");

		//保存图像的灰度副本
		bitmapGray = Bitmap.createBitmap(bitmapOrig.getWidth(),
				bitmapOrig.getHeight(), Config.ALPHA_8);
		//保存修改亮度值时的灰度图像
		bitmapWip = Bitmap.createBitmap(bitmapOrig.getWidth(),
				bitmapOrig.getHeight(), Config.ALPHA_8);
		// 在【找到边界】前，将其转换图像为灰色
		convertToGray(bitmapOrig, bitmapGray);
		// 找到边界，find edges in the image
		findEdges(bitmapGray,bitmapWip);
		ivDisplay.setImageBitmap(bitmapWip);//修改亮度值时的灰度图像
	}

	//pl_btnConvert；转换灰色
	public void onConvertToGray(View v) {
		Log.i(TAG, "onConvertToGray");
		
		//保存修改亮度值时的灰度图像
		bitmapWip = Bitmap.createBitmap(bitmapOrig.getWidth(),
				bitmapOrig.getHeight(), Config.ALPHA_8);
		convertToGray(bitmapOrig, bitmapWip);
		ivDisplay.setImageBitmap(bitmapWip);
	}

	//调光；pl_btnDimmer
	public void onDimmer(View v) {
		Log.i(TAG, "onDimmer");

		changeBrightness(2, bitmapWip);
		ivDisplay.setImageBitmap(bitmapWip);
	}

	//明亮的，pl_btnBrighter
	public void onBrighter(View v) {
		Log.i(TAG, "onBrighter");

		changeBrightness(1, bitmapWip);
		ivDisplay.setImageBitmap(bitmapWip);
	}

	static {
		try {
			System.loadLibrary("hello-ndk-jni");
		} catch (UnsatisfiedLinkError ule) {
			Log.i(TAG, "WARNING: Could not load lib!" + ule.toString());
		}
	}

	public native String stringFromNDKJNI();// 返回一个字符串

	public native void printLOGI();// logcat

	public native int currentTimeMillis();// 返回秒数

	public native void convertToGray(Bitmap bitmapIn, Bitmap bitmapOut);//转换到灰色

	public native void changeBrightness(int direction, Bitmap bitmap);//改变亮度

	public native void findEdges(Bitmap bitmapIn, Bitmap bitmapOut);//找到边界

}
