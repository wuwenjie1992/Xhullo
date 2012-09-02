package wo.wocom.xwell;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.1
 * @more		自定义视图；画图；屏幕触动事件
 */
public class XA_touchball extends Activity{

	CustomView1 CustomView = null;
	String TAG ="XA_TB";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 全屏显示窗口
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    		WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
    	// 获取屏幕宽高
    	Display display = getWindowManager().getDefaultDisplay();
    	
    	// 显示自定义的游戏View
    	CustomView = new CustomView1(this,display.getWidth(), display.getHeight());
        
        setContentView(CustomView);
        
        
	}
	
	
	class CustomView1 extends View{
         
	        private Paint paint;  //圆的画笔
	        private ArrayList<PointF> graphics = new ArrayList<PointF>();
	        private Paint  lPaint; //线的画笔
	        private Path   mPath;  //路径
	        private  int of = 0;
	        private Boolean over =false;  //表示画完与否
	        private float mX, mY;
	        private static final float TOUCH_TOLERANCE = 4;
	 
	        public CustomView1(Context context,int screenWidth, int screenHeight) {
	            super(context);
	            
	            /*圆*/
	            paint = new Paint(Paint.ANTI_ALIAS_FLAG); //Constants常量，位掩码标志启用抗锯齿
	            paint.setColor(Color.argb(74, 129,165,148));	//129，165，148
	            paint.setStrokeJoin(Paint.Join.ROUND);  //设置联接外缘的形式，联接外缘满足圆弧
	            paint.setStrokeCap(Paint.Cap.ROUND);	//设置线条端点的形式,半圆，在路径的末尾中心
	            paint.setStrokeWidth(59);
	            
	            /*线*/
	            lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	            lPaint.setColor(Color.argb(55,0,98,139));//0x00628B
	            lPaint.setStyle(Paint.Style.STROKE);//
	            lPaint.setStrokeJoin(Paint.Join.ROUND);
	            lPaint.setStrokeCap(Paint.Cap.ROUND);
	            lPaint.setStrokeWidth(9);
	            
	            mPath = new Path(); //路径
	            
	        }
	        
	    /////////////////////
	        public boolean onTouchEvent(MotionEvent event) {
	        	
	            float x = event.getX();
	            float y = event.getY();
	            
	            switch (event.getAction()) {
	            
	            	case MotionEvent.ACTION_DOWN:
	            	
	                over = false;	//没有画完的标志
	                graphics.clear();//清除内容
	                of=0;
	                graphics.add(new PointF(x,y));//添加 元素
	                touch_start(x, y);
	                invalidate();
	                Log.i(TAG,"ACTION_DOWN "+"x:"+x+"y:"+y);
	                break;
	                
	            	case MotionEvent.ACTION_MOVE:
	            	
	                graphics.add(new PointF(x, y));
	                touch_move(x, y);
	                invalidate();
	                Log.i(TAG,"ACTION_MOVE "+"x:"+x+"y:"+y);
	                break;
	                
	            	case MotionEvent.ACTION_UP:
	            	
	                over = true;	//画完的标志
	                touch_up();
	                invalidate();
	                Log.i(TAG,"ACTION_UP "+"x:"+x+"y:"+y);
	                break;
	            }
	 
	            return true;
	        }//onTouchEvent
	        
	        
	        private void touch_start(float x, float y) {
	            mPath.reset();	//private Path   mPath;  //路径
	            mPath.moveTo(x, y);
	            mX = x;
	            mY = y;
	        }
	        private void touch_move(float x, float y) {
	            float dx = Math.abs(x - mX);
	            float dy = Math.abs(y - mY);
	            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
	                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
	                mX = x;
	                mY = y;
	            }
	        }
	        private void touch_up() {mPath.lineTo(mX, mY); }
	 
	        
	        protected void onDraw(Canvas canvas) {
	        	
	            canvas.drawColor(Color.argb(95,98,133,199));//清理 背景
	            canvas.drawPath(mPath, lPaint);
	            
	            if(over&&graphics.size()>0){	//over表示画完与否，private int of = 0;
	            	//private ArrayList<PointF> graphics = new ArrayList<PointF>();
	                canvas.drawPoint(graphics.get(of).x, graphics.get(of).y, paint);
	                of+=1;
	                if(of<graphics.size()){
	                    if(of==graphics.size()-1){mPath.reset();}//移动完成后移除线条
	                    invalidate();}
	            								}
	        														}//onDraw
	        
	        
	        
	        
	    }//View
	
	
	
	//菜单
	 public boolean onCreateOptionsMenu(Menu menu) {
		 menu.add(Menu.NONE, Menu.FIRST + 1,1, "返回").setIcon(android.R.drawable.ic_menu_revert);
		 return true;
	 }
	
	 public boolean onOptionsItemSelected(MenuItem item) {
		 
		 switch (item.getItemId()) {
	        	case Menu.FIRST + 1:
	        	Toast.makeText(this, "XA_TB退出", Toast.LENGTH_SHORT).show();
	        	Log.i("XA——TB", "finish");
	        	XA_touchball.this.finish();
        	
        	break;  
              }
	        return false;}
	 
	 
	 
	 
}