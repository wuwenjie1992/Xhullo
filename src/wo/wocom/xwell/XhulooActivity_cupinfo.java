package wo.wocom.xwell;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.1
 * @more		CUP信息；调用CMDExecute（使用cat命令读取/proc/cupinfo	的内容）
 */
public class XhulooActivity_cupinfo extends Activity{

	private static final String TAG = "CI_Xhuloo";
	String cupinfo_s="cpu-info";
	
	/*activity生命周期*/
	 public void onCreate(Bundle savedInstanceState) {    
        
		 Log.i(TAG, "CI_onCreate");
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.cpuinfo); 
		 final TextView ci_tv=(TextView)findViewById(R.id.ci_tv);

	     cupinfo_s=fetch_cpu_info();
	     ci_tv.setText(cupinfo_s);
	 }
	
	
	 
	// cpu info
public static String fetch_cpu_info() {
	    String result = null;
	    CMDExecute cmdexe = new CMDExecute();//命令执行自定义类 CMDExecute.java
	    try {
	      String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
	      result = cmdexe.run(args, "/system/bin/");
	    	} 
	    catch (IOException e) {e.printStackTrace();}
	    
	    return result;
	  }
	
	
	 
public static String terminal_str() {
    String res = null;
    CMDExecute cmdexe = new CMDExecute();//命令执行自定义类
    try {
      String[] args = { "/system/bin/top", "-t" };
      res = cmdexe.run(args, "/system/bin/");
    	} 
    catch (IOException e) {e.printStackTrace();}
    
    return res;
  } 
	 
	 
}
