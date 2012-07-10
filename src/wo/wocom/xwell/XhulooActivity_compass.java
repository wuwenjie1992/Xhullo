package wo.wocom.xwell;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.2
 * @see		方向传感，指南针，旋转动画
 */
public abstract class XhulooActivity_compass extends Activity implements SensorEventListener{

	private static final String TAG = "COM_Xhuloo";
	
	ImageView image;  //指南针图片
	float currentDegree = 0; //指南针图片转过的角度
	SensorManager mSensorManager; //管理器
	
	float Pitch_f =0;//绕x轴转
	float Roll_f =0;
	
	
	/*activity生命周期*/
	public void onCreate(Bundle savedInstanceState) {    
	
	
		Log.i(TAG, "COM_onCreate------COM_Xhuloo");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.com); 
    
		image = (ImageView)findViewById(R.id.COM_ImageView);
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE); //获取管理服务
		Sensor sensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION); //方向传感器
    
		TextView COM_textView=(TextView)findViewById(R.id.COM_textView);	//文字信息
    
    
  //设置显示文字
    COM_textView.setText("重力感应:"+"\n"+"Pitch:"+ReturnValue(Pitch_f)+"\n"+"Roll:"+ReturnValue(Roll_f)+"\n"
    							+"test:"+ReturnValue((float) 5.08)+"\n"
			  					+"Name:"+sensor.getName()+" "+"Type:"+sensor.getType()+"\n"    //得到传感器信息
			  					+"Version:"+sensor.getVersion()+" "+"vendor:"+sensor.getVendor()+"\n"
			  					+"MAXrange:"+sensor.getMaximumRange()+" "+"power:"+sensor.getPower()+"\n"
			  					+"Resolution:"+sensor.getResolution()+"\n"
			  );
    
    
    
    
	}//oncreateEND
	
	 protected void onRestart() {  
		   super.onRestart();  
		   Log.i(TAG, "COM_onRestart"); 
		   Toast.makeText(XhulooActivity_compass.this, "COM_onRestart", Toast.LENGTH_SHORT).show();
		}  

	 protected void onResume() {  
		   super.onResume();  
		   Log.i(TAG, "COM_onResume");
		   Toast.makeText(XhulooActivity_compass.this, "COM_onResume", Toast.LENGTH_SHORT).show();
		   
		   //注册监听器
	    	mSensorManager.registerListener(
	    			this,
	    			mSensorManager.getDefaultSensor
	    			(Sensor.TYPE_ORIENTATION), 
	    			SensorManager.SENSOR_DELAY_GAME);
	 }  

	 protected void onPause() {  
		   super.onPause();  
		   Log.i(TAG, "COM_onPause"); 
		   Toast.makeText(XhulooActivity_compass.this, "COM_onPause", Toast.LENGTH_SHORT).show();
		   mSensorManager.unregisterListener(this);
	 }  

	 protected void onStop() {  
		   super.onStop();  
		   Log.i(TAG, "COM_onStop"); 
		   Toast.makeText(XhulooActivity_compass.this, "COM_onStop", Toast.LENGTH_SHORT).show();
		   mSensorManager.unregisterListener(this);
	 }  

	 protected void onDestroy() {  
		   super.onDestroy();  
		   Log.i(TAG, "COM_onDestroy");
		   Toast.makeText(XhulooActivity_compass.this, "COM_onDestroy", Toast.LENGTH_SHORT).show();
		   mSensorManager.unregisterListener(this);
	 }  
	 
	
	 
	 /*菜单制作*/
	 public boolean onCreateOptionsMenu(Menu menu) {
	   
	 	menu.add(Menu.NONE, Menu.FIRST +1,2, "返回").setIcon(

	             android.R.drawable.ic_menu_revert);
	     
	     return true;

	 }

	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	     
	     case Menu.FIRST +1:
	         
	          Log.i(TAG, "XhulooActivity.this.finish()");
	          XhulooActivity_compass.this.finish();  
	     break;

	     }

	     return false;

	 }

	 @Override
	 public void onOptionsMenuClosed(Menu menu) {
	     Toast.makeText(this, "wr_选项菜单关闭", Toast.LENGTH_SHORT).show();
	 }
	 
	 
	 
	 
	 /*方向传感骑功能*/
	 	//当传感器准确性更改
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
	   
		//传感器值更改	Called when sensor values have changed.
		public void onSensorChanged(SensorEvent event) {
			
			switch(event.sensor.getType()){	//获取触发event的传感器类型

			case Sensor.TYPE_ORIENTATION:
				float degree = event.values[0]; //获取z转过的角度
				
				float Pitch_f =event.values[1];	//绕x轴转
				float Roll_f =event.values[2];		//绕y轴转
				
				ReturnValue(Pitch_f); //自定义函数
				ReturnValue(Roll_f);
				
				ReturnValue((float) 5.08);
				
				//旋转动画,指南针的滚动
				RotateAnimation ra = new RotateAnimation
					(currentDegree,
					-degree,
					Animation.RELATIVE_TO_SELF,
					0.5f,
					Animation.RELATIVE_TO_SELF,
					0.5f
					);


			 ra.setDuration(100);//动画持续时间
			 image.startAnimation(ra);//开始动画
			 currentDegree = -degree;
			 break;
			
			}
			 

		}

		private float ReturnValue(float num) {
			return num;
			
		}
	 
	 
	 
	 
	 
			 
}
