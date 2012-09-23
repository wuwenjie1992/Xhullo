package wo.wocom.xwell;

import java.io.IOException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.1
 * @more		调用CMDExecute的模拟shell
 */

public class XA_smallshell extends Activity{

	private static final String TAG = "SS_Xhuloo";
	String cmd_s,parameter_s,workdir_s,output;
	
	/*activity生命周期*/
	 public void onCreate(Bundle savedInstanceState) {    
		 
		 Log.i(TAG, "SS_onCreate");
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.smallshell);
		 
		 //final TextView ss_cmd_tv=(TextView)findViewById(R.id.ss_cmd_tv);
		 final EditText ss_cmd_et=(EditText)findViewById(R.id.ss_cmd_et);
		 //final TextView ss_parameter_tv=(TextView)findViewById(R.id.ss_parameter_tv);
		 final EditText ss_parameter_et=(EditText)findViewById(R.id.ss_parameter_et);
		 //final TextView ss_workdir_tv=(TextView)findViewById(R.id.ss_workdir_tv);
		 final EditText ss_workdir_et=(EditText)findViewById(R.id.ss_workdir_et);
		 final Button ss_update_bt=(Button)findViewById(R.id.ss_update_bt);
		 final TextView ss_output_tv=(TextView)findViewById(R.id.ss_output_tv);
		 
		//注册监听器
		 ss_update_bt.setOnClickListener(new Button.OnClickListener(){
	        	public void onClick(View v) {
	        		
	        		cmd_s=ss_cmd_et.getText().toString();
	        		parameter_s=ss_parameter_et.getText().toString();
	        		workdir_s=ss_workdir_et.getText().toString();
	        		
	        		if(cmd_s.length()==0|workdir_s.length()==0){
	        			return_toast("命令为空或目录为空！");}
	        		else{return_toast("执行"+cmd_s);
	        			output=terminal_str(cmd_s,parameter_s,workdir_s);
	        			ss_output_tv.setText(output);}
	        		
	        									}//onclick end
		 																		}
				 								);
		 
		 
		 
	 }//oncreat end
	 
	 
	 
	 
public static String terminal_str(String cmd,String parameter,String workdir){
	
		    String result = null;
		    CMDExecute cmdexe = new CMDExecute();//命令执行自定义类 CMDExecute.java
		    try {
		      String[] args = {cmd,parameter};
		      result = cmdexe.run(args,workdir);
		    	} 
		    catch (IOException e) {
		    	e.printStackTrace();
		    	result=e.toString();}
		    
		    return result;
		  }
	 
	 
	 

	//自定义与Toast有关的函数，减少代码量，简洁		 
		public void return_toast(String show){
						
					 Toast.makeText(XA_smallshell.this, show, Toast.LENGTH_SHORT).show();
					 return ;
				 }	 
	 
	 
}
