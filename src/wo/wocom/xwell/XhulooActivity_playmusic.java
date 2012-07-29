package wo.wocom.xwell;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.2
 * @see		音乐播放界面，调用服务，启动MusicService
 */

public class XhulooActivity_playmusic extends Activity implements OnClickListener{

	private static final String TAG = "PM_Xhuloo";
	
	private Button Previous_bt,Play_bt,Next_bt,Pause_bt,bindservice_bt,unbindservice_bt;  
	private ComponentName component;  //组件名称，intent会根据component name
								//启动一个组件（activity,service,contentProvider）
    private Context mContext;  
    TextView music_PM_tv;
    private MusicService ms;	//调用服务
	
	/*activity生命周期*/
    public void onCreate(Bundle savedInstanceState) {    
	
	
    	Log.i(TAG, "PM_onCreate------PM_Xhuloo");
    	return_toast("PM_onCreate");
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.pm);  //设置主布局
	
    	component = new ComponentName(this,  
                MusicService.class);  
          
    	Previous_bt = (Button)findViewById(R.id.previous);  
    	Play_bt = (Button)findViewById(R.id.play);  
    	Next_bt = (Button)findViewById(R.id.next);  
       Pause_bt = (Button)findViewById(R.id.pause); 
       bindservice_bt=(Button)findViewById(R.id.bindservice_bt);
       unbindservice_bt=(Button)findViewById(R.id.unbindservice_bt);  
       
       //注册监听器
       Previous_bt.setOnClickListener((OnClickListener) this);  
       Play_bt.setOnClickListener((OnClickListener) this);  
       Next_bt.setOnClickListener((OnClickListener) this);  
       Pause_bt.setOnClickListener((OnClickListener) this);  
       bindservice_bt.setOnClickListener((OnClickListener) this);
       unbindservice_bt.setOnClickListener((OnClickListener) this);
       
       
       music_PM_tv = (TextView)findViewById(R.id.music_PM);
       mContext = XhulooActivity_playmusic.this;  
       
       
     
    }//oncreate
       
 // 连接对象  ServiceConnection在Context.bindService和context.unBindService()里用到 
    ServiceConnection msc = new ServiceConnection() {  
 	   //当bindService时，让TextView显示MyService里getSystemTime()的返回值 
        public void onServiceConnected(ComponentName name, IBinder ser) { 
     	   
        	Log.i(TAG, "onServiceCon--PM_Xhuloo");
           ms = ((MusicService.MyBinder)ser).getService(); // ser--onServiceConnected()的参数
           
       	Log.i(TAG, "onServiceConnected--PM_Xhuloo"+ms.getSystemTime());
       	music_PM_tv.setText("from MusicService :" +ms.getSystemTime());   //ms.getSystemTime()
        }  
  
        public void onServiceDisconnected(ComponentName name) {  
        	Log.i(TAG, "onServiceDiscon--PM_Xhuloo");
  
        }  
    }; 
  
    
    //按钮监听响应
    public void onClick(View v) {
		
		switch(v.getId()){   
			case R.id.previous:   
		
        	 Log.i(TAG, "PM_Previous_bt------PM_Xhuloo");
        	 Intent mIntent01 = new Intent(wo.wocom.xwell.MusicService.PREVIOUS_ACTION);  
        	 mIntent01.setComponent(component);  
        	 startService(mIntent01); 
        	 break; 
             
			case R.id.play:   

				Log.i(TAG, "PM_Play_bt------PM_Xhuloo");
				Intent mIntent02 = new Intent(wo.wocom.xwell.MusicService.PLAY_ACTION);  
				mIntent02.setComponent(component);  
				startService(mIntent02);  
				break; 
        	 
         case R.id.next:
        	 
        	 Intent mIntent03 = new Intent(MusicService.NEXT_ACTION);  
        	 mIntent03.setComponent(component);  
        	 startService(mIntent03);  
        	 break; 
        	 

         case R.id.pause: 
        	 
        	 Intent mIntent04 = new Intent(MusicService.PAUSE_ACTION);  
        	 mIntent04.setComponent(component);  
        	 startService(mIntent04); 
        	 break; 
        	 
        	 
         case R.id.bindservice_bt:
        	 
        	 Log.i(TAG, "PM_Play_bt------PM_Xhuloo");
        	 Intent i  = new Intent();  
        	 i.setClass(XhulooActivity_playmusic.this, MusicService.class);  
        	 mContext.bindService(i, msc, BIND_AUTO_CREATE); //执行IBinder() 
        }  
	}//onClick
	
	
	 protected void onRestart() {  
		   super.onRestart();  
		   Log.i(TAG, "PM_onRestart"); 
		   return_toast("PM_onRestart");//自定义与Toast有关的函数
		}  

	 protected void onResume() {  
		   super.onResume();  
		   Log.i(TAG, "PM_onResume");
		   return_toast("PM_onResume");
		   
		}  

	 protected void onPause() {  
		   super.onPause();  
		   Log.i(TAG, "PM_onPause"); 
		   return_toast("PM_onPause");

		}  

	 protected void onStop() {  
		   super.onStop();  
		   Log.i(TAG, "PM_onStop"); 
		   return_toast("PM_onStop");
}  

	 protected void onDestroy() {  
		   super.onDestroy();  
		   Log.i(TAG, "PM_onDestroy");
		   return_toast("PM_onDestroy");
		}  
			 
			 
	//自定义与Toast有关的函数，减少代码量，简洁		 
	 void return_toast(String show){
					
				 Toast.makeText(XhulooActivity_playmusic.this, show, Toast.LENGTH_SHORT).show();
				 return ;
			 }	 
			 
			 
			 
			 
	 
			 
			 
}