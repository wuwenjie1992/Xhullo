package wo.wocom.xwell;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.2
 * @more		Android NDK JNI Native Development Kit_Java Native Interface
 */
public class XAplasma extends Activity{
	static String TAG="XA_PL";
	
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "XA_PL_oncreate!!");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pl);
		TextView textView = (TextView)findViewById(R.id.pl_tv);  
	        
		String myString =stringFromNDKJNI();//stringFromNDKJNI
	        	
		textView.setText(myString);  
		
		printLOGI();
}

	    
	static{
			try{System.loadLibrary("hello-ndk-jni"); }
			catch(UnsatisfiedLinkError ule){
					Log.i(TAG,"WARNING: Could not load lib!"+ule.toString());
								}
			}
	
	public native String stringFromNDKJNI();
	
	public native void printLOGI();
}
