package wo.wocom.xwell;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageTouchListener implements OnTouchListener {
	
	static String TAG = "XA_ImageTouchListener";
	private PointF startPoint;	//声明一个坐标点float
	private Matrix matrix = new Matrix();	//实例化Matrix来控制图片
	private Matrix currentMatrix = new Matrix();
								//声明并实例当前的图片的Matrix
	private ImageView iview;
	
	//将iview设置为传入的参数
	void setview(View v){iview=(ImageView) v;}
	
	//触动反馈时触发函数
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()&MotionEvent.ACTION_MASK) { 
							//ACTION_MASK用于多点触控
			case MotionEvent.ACTION_DOWN://按下屏幕
				startPoint = new PointF(event.getX(), event.getY());
				//getX	得到X轴坐标 getY 得到Y轴标
				currentMatrix.set(iview.getImageMatrix());
				//getImageMatrix()返回该视图的可选矩阵
				//把当前的图片Matrix设置为按下图片的Matrix
				break;

			case MotionEvent.ACTION_MOVE://按下屏幕拖动
				float dx = event.getX() - startPoint.x;//移动的X坐标的距离
				float dy = event.getY() - startPoint.y;//移动的Y坐标的距离
				Log.i(TAG, "MotionEvent.ACTION_MOVE，dx:"+dx+"dy:"+dy);
				matrix.set(currentMatrix);	//设置Matrix当前的matrix
				matrix.postTranslate(dx, dy);//告诉matrix要移动的X轴和Y轴的距离
				break;
		}
		iview.setImageMatrix(matrix);//设置图片矩阵
		return true;	//返回True表明消费掉该动作，不需父控件进一步处理
	}

}
