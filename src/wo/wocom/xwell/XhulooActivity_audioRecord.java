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
 * @version  1.3.2<---（1.3.5版）
 * @see		声音录制；保存录音文件(setOutputFile);发送广播
 */

public class XhulooActivity_audioRecord extends Activity{

	private static final String TAG = "AR_Xhuloo";
	public MediaRecorder mrec = null;	//实列化 媒体录制
	private Button startRecording = null;	//开始录音按钮
	private Button stopRecording = null;		//结束录音按钮
	File audiofile;	//文件
	
	
	/*activity生命周期*/
    public void onCreate(Bundle savedInstanceState) {   
    	
    	Log.i(TAG, "AR_onCreate");
    	return_toast("AR_onCreate");//调用
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.ar);  //设置主布局
    	
    	mrec = new MediaRecorder();
        
    	startRecording = (Button)findViewById(R.id.ar_startrecording);
    	stopRecording = (Button)findViewById(R.id.ar_stoprecording);
    	stopRecording.setEnabled(false);//view public Method 设置启用
    	startRecording.setEnabled(true);
        
    	startRecording.setOnClickListener(new View.OnClickListener(){
        		public void onClick(View v) {
        			try{
        				startRecording.setEnabled(false);
        				stopRecording.setEnabled(true);
        				stopRecording.requestFocus(); //要求聚焦
        	  
        				startRecording();//调用 函数
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
        						stopRecording();		//调用 函数
        						Broadcastaudiofile();	//处理音频文件
        												}//onclick
          																});
    }//oncreat end
	
	
    protected void startRecording() throws IOException  {
        mrec.setAudioSource(MediaRecorder.AudioSource.MIC); 	//设置音频源为Microphone audio source
        mrec.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);	//媒体录制 输出格式AMR
        mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); 	//音频编码器AMR (Narrowband窄频带)
        	if (audiofile==null) {
        		File sampleDir = Environment.getExternalStorageDirectory();//获得SD卡的PATH
	          try{
	        	  audiofile=File.createTempFile("wuwenjie录音",".amr",sampleDir);//创建临时文件
	          		} 
	          catch (IOException e){Log.e(TAG,"an error occurs when writing the file");return;}
        }//fi
        mrec.setOutputFile(audiofile.getAbsolutePath());
        mrec.prepare();
        mrec.start();
    }

    protected void stopRecording() {
        mrec.stop();
        mrec.release();
        return_toast(audiofile.getName()+"保存在"+audiofile.getAbsolutePath());
      }
	
	//处理音频文件，广播新音频文件
    protected void Broadcastaudiofile() {
        ContentValues values = new ContentValues(4);//内容值，存储ContentResolver可处理的值
        ContentResolver contentResolver = getContentResolver();//解决在不同的应用包之间共享数据的工具

        long current = System.currentTimeMillis();	//当前毫秒数
            
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());//title of the content
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int)(current/1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/amr");		//The MIME type of the file
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());//data stream for the file
        
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; //“主”外部存储卷
        Uri newUri = contentResolver.insert(base,values);//表中插入一行,insert(Uri url,ContentValues values)
        															//url 	The URL of the table to insert into.
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,newUri));
        //广播动作，要求媒体扫描器扫描文件并添加到媒体数据库中
      }
	
  //自定义与Toast有关的函数，减少代码量，简洁		 
  	public void return_toast(String show){
  		Toast.makeText(XhulooActivity_audioRecord.this, show, Toast.LENGTH_SHORT).show();
  		return ;
  			 }
    
}
