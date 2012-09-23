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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.5
 * @more		音乐播放界面，调用服务，启动MusicService,
 * 				音乐文件路径输入框
 */

public class XhulooActivity_playmusic extends Activity implements OnClickListener{

	private static final String TAG = "PM_Xhuloo";
	
	private Button Previous_bt,Play_bt,Next_bt,Pause_bt,bindservice_bt,unbindservice_bt;  
	private ComponentName component;  //组件名称，intent会根据component name
								//启动一个组件（activity,service,contentProvider）
    private Context mContext;  //上下文,主要有Activity、Service以及BroadcastReceiver
    TextView music_PM_tv;
    EditText PM_MusicPath_et;
    private MusicService ms;	//调用服务
    
    String musicPath_s;
	
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
       unbindservice_bt=(Button)findViewById(R.id.unb_bt);  
       
       //注册监听器
       Previous_bt.setOnClickListener((OnClickListener) this);  
       Play_bt.setOnClickListener((OnClickListener) this);  
       Next_bt.setOnClickListener((OnClickListener) this);  
       Pause_bt.setOnClickListener((OnClickListener) this);  
       bindservice_bt.setOnClickListener((OnClickListener) this);
       unbindservice_bt.setOnClickListener((OnClickListener) this);
       
       PM_MusicPath_et=(EditText)findViewById(R.id.PM_MusicPath_et);//音乐文件路径输入框
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
        	break;  
        	 
         case R.id.unb_bt:
        	 
        	 Intent mIntent05 = new Intent(MusicService.URL_ACTION);
        	 mIntent05.setComponent(component); 
        	 
        	 musicPath_s=PM_MusicPath_et.getText().toString();	//获得文本编框的文字
        	 
        	 if(musicPath_s.length()!=0){
        		 Bundle Bundle_PM = new Bundle();    
        		 Bundle_PM.putString("PM_URL", musicPath_s);//加入数据 参数：key-Data value  
        		 mIntent05.putExtras(Bundle_PM);  //添加附加信息 
     		 
        		 startService(mIntent05); 
        	 }
        	 else{return_toast("路径为空，无法播放指定内容，请检查！"); }
        	 
        	 break;  
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
	public void return_toast(String show){
					
				 Toast.makeText(XhulooActivity_playmusic.this, show, Toast.LENGTH_SHORT).show();
				 return ;
			 }	 
			 
			 
			 
			 
	 
			 
			 
}