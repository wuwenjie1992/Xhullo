package wo.wocom.xwell;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.1
 * @more		通讯信息；ANDROID_ID
 */
public class XhulooActivity_PHONE extends Activity{

	private static final String TAG = "PH_Xhuloo";
	
	/*activity生命周期*/
	 public void onCreate(Bundle savedInstanceState) {    
        
		 Log.i(TAG, "PH_onCreate------PH_Xhuloo");
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.phone); 
	        
	        final Button BC_btn_PH = (Button)findViewById(R.id.BC_btn_PH);
	        final TextView ph_textview=(TextView)findViewById(R.id.ph_textview);
	        StringBuffer NBCinfo_sb=new StringBuffer("小区通讯信息："+"\n");
	        
	    /*通讯信息*/
	        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
	        
	        	List<NeighboringCellInfo> infos=tm.getNeighboringCellInfo();
	        		for(NeighboringCellInfo NBCinfo:infos){ //for(循环变量类型 循环变量名称 : 要被遍历的对象) 循环体 
	        			NBCinfo_sb.append("小区号"+NBCinfo.getCid()+" LAC位置区域码:"+NBCinfo.getLac()+"\n"
	        						+"网络类型:"+NBCinfo.getNetworkType()+"主扰码"+NBCinfo.getPsc()+"\n"
	        						+"信号强度"+NBCinfo.getRssi()+"\n\n");
	        	
	        }
	        
	        String telephone_info_s=new String("手机串号:"+tm.getDeviceId()+"\n"
	        										+"通话状态:"+tm.getCallState()+"\n"
	        										+"数据活动状态:"+tm.getDataActivity()+"\n"
	        										+"数据连接状态:"+tm.getDataState()+"\n"
	        										+"终端软件版本:"+tm.getDeviceSoftwareVersion()+"\n"
	        										+"手机号码:"+tm.getLine1Number()+"\n"
	        										+NBCinfo_sb
	        										+"运营商国别码:"+tm.getNetworkCountryIso()+"\n"
	        										+"运营商数字域名MCC+MNC:"+tm.getNetworkOperator()+"\n"
	        										+"目前注册运营商:"+tm.getNetworkOperatorName()+"\n"
	        										+"网络类型:"+tm.getNetworkType()+"\n"
	        										+"电话类型:"+tm.getPhoneType()+"\n"
	        										+"SIM卡供应商代码："+tm.getSimCountryIso()+"\n"
	        										+"SIM卡供应商MCC+MNC:"+tm.getSimOperator()+"\n"
	        										+"服务供应商名称:"+tm.getSimOperatorName()+"\n"
	        										+"SIM卡序列号:"+tm.getSimSerialNumber()+"\n"
	        										+"SIM卡状态:"+tm.getSimState()+"\n"
	        										+"用户ID:"+tm.getSubscriberId()+"\n"
	        										+"语音信箱标识符:"+tm.getVoiceMailAlphaTag()+"\n"
	        										+"ICC card:"+tm.hasIccCard()+"\n"
	        										+"无线网络漫游:"+tm.isNetworkRoaming ()
	        		);
	        
	        
	      String  androidId ="\n"+"ANDROID_ID:"+android.provider.Settings.Secure.getString
	        		(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	        //Settings.Secure安全系统设置，包含系统偏好，应用程序可以读取，但不允许写
	        
	        
	        ph_textview.setText(NBCinfo_sb.toString()+telephone_info_s+androidId);
	        
	        
	/*按钮监听*/    
	        BC_btn_PH.setOnClickListener(new Button.OnClickListener(){
	        	public void onClick(View v) {
	        		switch(v.getId()){
	        			case R.id.BC_btn_PH:
	        				XhulooActivity_PHONE.this.finish();
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
    Log.i(TAG, "PH_onRestart"); 
    Toast.makeText(XhulooActivity_PHONE.this, "PH_onRestart", Toast.LENGTH_SHORT).show();
}  

	 protected void onResume() {  
    super.onResume();  
    Log.i(TAG, "PH_onResume");
    Toast.makeText(XhulooActivity_PHONE.this, "PH_onResume", Toast.LENGTH_SHORT).show();
}  

	 protected void onPause() {  
    super.onPause();  
    Log.i(TAG, "PH_onPause"); 
    Toast.makeText(XhulooActivity_PHONE.this, "PH_onPause", Toast.LENGTH_SHORT).show();
}  

	 protected void onStop() {  
    super.onStop();  
    Log.i(TAG, "PH_onStop"); 
    Toast.makeText(XhulooActivity_PHONE.this, "PH_onStop", Toast.LENGTH_SHORT).show();
}  

	 protected void onDestroy() {  
    super.onDestroy();  
    Log.i(TAG, "PH_onDestroy");
    Toast.makeText(XhulooActivity_PHONE.this, "PH_onDestroy", Toast.LENGTH_SHORT).show();
}  


	
	
}
