package wo.wocom.xwell;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.5
 * @more		自定义列表模式；联网；解析JASON；
 * 				联网、解析的耗时操作使用handler
 */

public class XhulooActivity_weatherreport extends Activity{

	private static final String TAG = "WR_Xhuloo";
	
	ListView listView; 
	ListViewAdapter my_LVA;
	String a1;
	String[] titles=new String [4];//={a1,a1,a1,a1}; 
	String[] texts={"文本内容A","文本内容B","文本内容C","文本内容D"};  
	int[] resIds={R.drawable.action,
			R.drawable.action,
			R.drawable.action,
			R.drawable.action}; 
	
	MyHandler myHandler;
	int i;
	
	List <weatherinfo> itemlist;
	
    /*activity生命周期*/
    public void onCreate(Bundle savedInstanceState) {    
	
	
    	Log.i(TAG, "WR_onCreate------WR_Xhuloo");
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.wr);  //设置主布局
    	
    	
    	
    	myHandler = new MyHandler();
    	//创建新的Handler实例
    	//绑定到当前线程和消息的队列中,开始分发数据
    	MyThread m = new MyThread();
    	new Thread(m).start();
    	
    	
    	
    	
    	
     	Log.i(TAG, "WR_main:"+a1+titles);
     	
     	
     	ListView listView=(ListView)this.findViewById(R.id.wr_lv);  
     	my_LVA=new ListViewAdapter(titles);
     	listView.setAdapter(my_LVA);  

    	Log.i(TAG, "WR_main:"+a1+titles);
        
	//listView=(ListView)this.findViewById(R.id.wr_lv);  
     //   listView.setAdapter(new ListViewAdapter(titles,texts,resIds));  
    //final TextView wr_tv=(TextView) findViewById(R.id.wr_tv);
    //wr_tv.setBackgroundResource(R.drawable.ic_launcher);
    
    //http://www.weather.com.cn/weather/101020100.shtml
    //http://m.weather.com.cn/data/101020300.html
    
    
        
        
        
        
        
        
	}//oncreate

	
	
    
	

//自定义LISTVIEW
public class ListViewAdapter extends BaseAdapter {  
    
	View[] itemViews;  
	
	
    public ListViewAdapter(String[]itemTitles) {  
    	
		itemViews = new View[itemTitles.length];  //itemViews.length=有几个图即几行

        	for (i = 0; i < itemViews.length; i++) {  
            itemViews[i] = makeItemView(texts[i],texts[i],texts[i],texts[i]);  
        					}  //调用makeItemView自定义函数，来组织界面，返回对象
    }  

    public int getCount() {  
        return itemViews.length;  
    }  

    public View getItem(int position) {  
        return itemViews[position];  
    }  

    public long getItemId(int position) {  
        return position;  
    }  

    private View makeItemView
    			(String strweek, String strweather, String temp,String advise) {  
    	
        LayoutInflater inflater = 
        			(LayoutInflater)XhulooActivity_weatherreport.this  
        				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  

        // 使用View的对象itemView与list_item.xml关联  
        View itemView = inflater.inflate(R.layout.list_item, null);

        // 通过findViewById()方法实例R.layout.item内各组件  
        TextView strweek_tv = (TextView) itemView.findViewById(R.id.strweek_tv);  
        strweek_tv.setText(strweek);  
        
        TextView strweather_tv = (TextView) itemView.findViewById(R.id.strweather_tv);  
        strweather_tv.setText(strweather); 
        
        TextView temp_tv = (TextView) itemView.findViewById(R.id.temp_tv);  
        temp_tv.setText(temp);
        
        TextView advise_tv = (TextView) itemView.findViewById(R.id.advise_tv);  
        advise_tv.setText(advise);
        
        return itemView;  
    }  

    public View getView(int position,View convertView,ViewGroup parent){  
        if (convertView == null)  
            return itemViews[position];  
        return convertView; }  
    
    
    
				} //ListViewAdapter  





///////////////////////////////////////////////////////////////////////////////////////////

//接收,处理消息,Handler与当前主线程一起运行

public class MyHandler extends Handler {
	
	public MyHandler() {}

	public MyHandler(Looper L) {super(L);}
	
	
	//子类必须重写此方法,接受数据
	public void handleMessage(Message msg) {
  
		Log.i("MyHandler", "handleMessage......");
		super.handleMessage(msg);

        // 传递信息以更新UI
        Bundle b = msg.getData();
        a1 = b.getString("a1");
        Log.i(TAG, "WR_handleMessage:"+a1);
        
        
        for(int i=0;i<4;i++){
        titles[i]=a1; 
        Log.i(TAG, "titles:"+titles[i]);}
        
        
        
        //my_LVA=new ListViewAdapter(titles,texts,resIds);
        
        ///////my_LVA.notifyDataSetChanged();//通知数据改变，反应该刷新视图
     	 //listView.setAdapter(my_LVA);  // 重新设置ListView的数据适配器

     	 
    	//listView.setAdapter(new ListViewAdapter(titles,texts,resIds));
      	
        
    }
	
	
}
//myhandler end




public class MyThread implements Runnable {
    public void run() {
        Log.i(TAG, "MyThread_start");

    	/*联网*/
     	String html_s=a1;
     	HttpGet httpGet=new HttpGet("http://m.weather.com.cn/data/101020300.html");  
        HttpClient httpClient=new DefaultHttpClient();  
        try {  
            //得到HttpResponse对象  
            HttpResponse httpResponse=httpClient.execute(httpGet);  
            //HttpResponse的返回结果是不是成功  
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){  
                //得到返回数据的字符串  
                String data_s=EntityUtils.toString(httpResponse.getEntity());  
                Log.i(TAG, "WR"+data_s); 
                html_s=data_s;
                Log.i(TAG, "WR02"+html_s.length());

            }  
        } catch (ClientProtocolException e) { e.printStackTrace();} 
        	catch (IOException e) { e.printStackTrace();  }  
     
        a1=html_s.substring(0,10);
       
        Message msg = new Message();
        Bundle b = new Bundle();// 存放数据
        b.putString("a1",a1);
        msg.setData(b);

        XhulooActivity_weatherreport.this.myHandler.sendMessage(msg); 
        //向Handler发送消息,更新UI

    }
}



////////////////////////////////////////////////////////////////////////////////////////////


//自定义 weatherinfo 类
public class weatherinfo{
	
	private String week;		//星期几
	private String weather;//天气
	private String temp;//气温  
	private String advise;//建议
	
	public String getweek() { return week;}  
	public void setweek(String week) {this.week = week;} 
   
   public String getweather() { return weather;}  
   public void setweather(String weather) {this.weather = weather;}  
 
   public String gettemp() { return temp;}  
   public void settemp(String temp) {this.temp = temp;} 
   
   public String getadvise() { return advise;}  
   public void setadvise(String advise) {this.advise = advise;} 
   
}


//http://blog.segmac.com/blog/android-google-weather-api.html













/*菜单制作*/
public boolean onCreateOptionsMenu(Menu menu) {
  /*  add()四个参数
     * 1、组别，如果不分组的话就写Menu.NONE
    * 2、Id,根据Id来确定不同菜单
     * 3、顺序，菜单出现的顺序
     * 4、文本，菜单的显示文本
     */
	menu.add(Menu.NONE, Menu.FIRST +1,2, "返回").setIcon(

            android.R.drawable.ic_menu_revert);
    
    return true;

}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    
    case Menu.FIRST +1:
        
         Log.i(TAG, "XhulooActivity.this.finish()");
         XhulooActivity_weatherreport.this.finish();  
    break;

    }

    return false;

}

@Override
public void onOptionsMenuClosed(Menu menu) {
    Toast.makeText(this, "wr_选项菜单关闭", Toast.LENGTH_SHORT).show();
}





/*

public class HttpURLConnectionActivity extends Activity {  
    
    private ImageView imageView;  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.simple1);  
          
        imageView=(ImageView) this.findViewById(R.id.imageView1);  
        //传入网络图片地址  
        try {  
            URL url = new URL("http://news.xinhuanet.com/photo/2012-02/09/122675973_51n.jpg");  
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5*1000);  
            conn.connect();  
            InputStream in=conn.getInputStream();  
            ByteArrayOutputStream bos=new ByteArrayOutputStream();  
            byte[] buffer=new byte[1024];  
            int len = 0;  
            while((len=in.read(buffer))!=-1){  
                bos.write(buffer,0,len);  
            }  
            byte[] dataImage=bos.toByteArray();  
            bos.close();  
            in.close();  
            Bitmap bitmap=BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);  
            //Drawable drawable=BitmapDrawable.  
            imageView.setImageBitmap(bitmap);  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            Toast.makeText(getApplicationContext(), "图片加载失败", 1).show();  
        }  
          
    }  
}  

*/







	
}