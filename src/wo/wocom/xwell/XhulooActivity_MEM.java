package wo.wocom.xwell;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class XhulooActivity_MEM extends Activity{

	private static final String TAG = "MEM_Xhuloo";
	
	
	/*activity生命周期*/
	public void onCreate(Bundle savedInstanceState) {    
	
	
	Log.i(TAG, "MEM_onCreate------MEM_Xhuloo");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mem); 
    
    final Button BC_btn_MEM = (Button)findViewById(R.id.BC_btn_MEM);
    final TextView MEM_textview=(TextView)findViewById(R.id.MEM_textview);
   
    
    
    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);  
    ActivityManager.MemoryInfo mf = new ActivityManager.MemoryInfo();  
    am.getMemoryInfo(mf);  
    ActivityManager.RunningAppProcessInfo rap = new ActivityManager.RunningAppProcessInfo();
    am.getRunningAppProcesses();
    
    /*震动*/
    final Vibrator vi=(Vibrator)getSystemService(VIBRATOR_SERVICE ); 
    long[] pattern = {100, 50,200, 60,300,70,390,80,300,50,200,30}; // OFF/ON/OFF/ON... 
    int repeat=4;
    vi.vibrate(pattern, repeat);
    
    MEM_textview.setText("剩余内存(K):"+ (mf.availMem >>10)+"\n"	//>>右移位运算符,除以2的10次方,1024
    						+"剩余内存(M):"+ (mf.availMem >>20)+"\n"
    						+"低内存状态:"+mf.lowMemory+"\n"
    						+"低内存临界值："+mf.threshold+"\n"
    						+"设备配置属性"+am.getDeviceConfigurationInfo().toString()+"\n"
    						+"运行进程："+rap.processName+"\n"
    						
    		); 
    									
    
    
    
    /*按钮监听*/    
    BC_btn_MEM.setOnClickListener(new Button.OnClickListener(){
    	public void onClick(View v) {
    		switch(v.getId()){
    			case R.id.BC_btn_MEM:
    				XhulooActivity_MEM.this.finish();
    				vi.cancel ();
    				
    			break;
    			
    			default:
    			break;
    		}//switch
    	 						}//onclick_end
    													}
    								  );	        
    
    
    
    
    
    
    
	}//onCreate
	 
	 protected void onRestart() {  
   super.onRestart();  
   Log.i(TAG, "MEM_onRestart"); 
   Toast.makeText(XhulooActivity_MEM.this, "MEM_onRestart", Toast.LENGTH_SHORT).show();
}  

	 protected void onResume() {  
   super.onResume();  
   Log.i(TAG, "MEM_onResume");
   Toast.makeText(XhulooActivity_MEM.this, "MEM_onResume", Toast.LENGTH_SHORT).show();
}  

	 protected void onPause() {  
   super.onPause();  
   Log.i(TAG, "MEM_onPause"); 
   Toast.makeText(XhulooActivity_MEM.this, "MEM_onPause", Toast.LENGTH_SHORT).show();
}  

	 protected void onStop() {  
   super.onStop();  
   Log.i(TAG, "MEM_onStop"); 
   Toast.makeText(XhulooActivity_MEM.this, "MEM_onStop", Toast.LENGTH_SHORT).show();
}  

	 protected void onDestroy() {  
   super.onDestroy();  
   Log.i(TAG, "MEM_onDestroy");
   Toast.makeText(XhulooActivity_MEM.this, "MEM_onDestroy", Toast.LENGTH_SHORT).show();
}  

	
	
	
	
}
