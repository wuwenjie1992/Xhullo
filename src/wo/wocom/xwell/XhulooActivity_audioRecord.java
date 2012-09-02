package wo.wocom.xwell;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.1<---（1.3.5版）
 * @see		声音录制；
 */

public class XhulooActivity_audioRecord extends Activity{

	private static final String TAG = "AR_Xhuloo";
	public MediaRecorder mrec = null;
	private Button startRecording = null;
	private Button stopRecording = null;
	File audiofile;
	
	
	/*activity生命周期*/
    public void onCreate(Bundle savedInstanceState) {   
    	
    	Log.i(TAG, "AR_onCreate");
    	return_toast("AR_onCreate");//调用
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.ar);  //设置主布局
    	
    	mrec = new MediaRecorder();
        
    	startRecording = (Button)findViewById(R.id.ar_startrecording);
    	stopRecording = (Button)findViewById(R.id.ar_stoprecording);
    	stopRecording.setEnabled(false);
    	startRecording.setEnabled(true);
        
    	startRecording.setOnClickListener(new View.OnClickListener(){
        		public void onClick(View v) {
        			try{
        				startRecording.setEnabled(false);
        				stopRecording.setEnabled(true);
        				stopRecording.requestFocus();
        	  
        				startRecording();
        				return_toast("AR_startRecording");//调用
        	   
        			}
        			catch (Exception e){Log.e(TAG,"发生错误：" + e.getMessage());
        									return_toast("AR_ERROR:"+e.getMessage());		}
        										}//onclick
        																	});
        
    	stopRecording.setOnClickListener(new View.OnClickListener(){
        				public void onClick(View v) {
        						startRecording.setEnabled(true);
        						stopRecording.setEnabled(false);
        						startRecording.requestFocus();
        						stopRecording();
        						processaudiofile();
        												}//onclick
          																});
        
	   
    	
    }
	
	
	
    protected void startRecording() throws IOException  {
        mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
        mrec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
       // mRecorder.setOutputFile("/sdcard/yousuck2.3gp");
        if (audiofile == null) {
	          File sampleDir = Environment.getExternalStorageDirectory();
	        
	          try { 
	        	  audiofile = File.createTempFile("wuwenjie录音", ".3gp", sampleDir);
	          } 
	          catch (IOException e)
	          {
	              Log.e(TAG,"sdcard access error");
	              return;
	          }
        }

        mrec.setOutputFile(audiofile.getAbsolutePath());
        
        mrec.prepare();
        mrec.start();
        
    }
	
	
    protected void stopRecording() {
        mrec.stop();
        mrec.release();
      }
	
	
    protected void processaudiofile() {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
            
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
        ContentResolver contentResolver = getContentResolver();
        
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        // this does not always seem to work cleanly....
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
      }
	
	
  
    
  //自定义与Toast有关的函数，减少代码量，简洁		 
  	public void return_toast(String show){
  					
  				 Toast.makeText(XhulooActivity_audioRecord.this, show, Toast.LENGTH_SHORT).show();
  				 return ;
  			 }	 
    
    
}
