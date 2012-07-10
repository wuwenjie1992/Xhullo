package wo.wocom.xwell;

import java.io.IOException;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class XhulooActivity_weatherreport extends Activity{

	private static final String TAG = "MEM_Xhuloo";
	
	
	ListView listView;  
    String[] titles={"标题1","标题2","标题3","标题4"};  
    String[] texts={"文本内容A","文本内容B","文本内容C","文本内容D"};  
    int[] resIds={R.drawable.action,R.drawable.action,R.drawable.action,R.drawable.action}; 
	
    
	/*activity生命周期*/
	public void onCreate(Bundle savedInstanceState) {    
	
	
	Log.i(TAG, "WR_onCreate------WR_Xhuloo");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.wr);  //设置主布局
    
    listView=(ListView)this.findViewById(R.id.wr_lv);  
    listView.setAdapter(new ListViewAdapter(titles,texts,resIds));  
	
    //final TextView wr_tv=(TextView) findViewById(R.id.wr_tv);
    //wr_tv.setBackgroundResource(R.drawable.ic_launcher);
    
    
/*联网*/
    String html_s ="html";
    HttpGet httpGet_baoshang=new HttpGet("http://m.weather.com.cn/data/101020300.html");  
    HttpClient httpClient=new DefaultHttpClient();  
    try {  
        //得到HttpResponse对象  
        HttpResponse httpResponse=httpClient.execute(httpGet_baoshang);  
        //HttpResponse的返回结果是不是成功  
        if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){  
            //得到返回数据的字符串  
            String dataImageStr=EntityUtils.toString(httpResponse.getEntity()); 
            Log.i(TAG, "WR_html00-----WR_Xhuloo"+dataImageStr);
            html_s=(dataImageStr);
            return;
        }  
    } catch (ClientProtocolException e) {  e.printStackTrace(); } 
    	catch (IOException e) { e.printStackTrace();   }  
    
    Log.i(TAG, "WR_html01-----WR_Xhuloo"+html_s);

    
    
    
    
	}//oncreate

	
	
	
	
	


public class ListViewAdapter extends BaseAdapter {  
    View[] itemViews;  

    public ListViewAdapter(String[] itemTitles, String[] itemTexts,  
            int[] itemImageRes) {  
        itemViews = new View[itemTitles.length];  

        for (int i = 0; i < itemViews.length; i++) {  
            itemViews[i] = makeItemView(itemTitles[i], itemTexts[i],  
                    itemImageRes[i]);  
        }  
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

    private View makeItemView(String strTitle, String strText, int resId) {  
        LayoutInflater inflater = (LayoutInflater)XhulooActivity_weatherreport.this  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  

        // 使用View的对象itemView与list_item.xml关联  
        View itemView = inflater.inflate(R.layout.list_item, null);

        // 通过findViewById()方法实例R.layout.item内各组件  
        TextView title = (TextView) itemView.findViewById(R.id.itemTitle);  
        title.setText(strTitle);  
        TextView text = (TextView) itemView.findViewById(R.id.itemText);  
        text.setText(strText);  
        ImageView image = (ImageView) itemView.findViewById(R.id.itemImage);  
        image.setImageResource(resId);  
          
        return itemView;  
    }  

    public View getView(int position, View convertView, ViewGroup parent) {  
        if (convertView == null)  
            return itemViews[position];  
        return convertView; }  
				} //ListViewAdapter  








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





	
}
