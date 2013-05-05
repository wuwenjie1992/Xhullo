package wo.wocom.xwell.GUItool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import wo.wocom.xwell.R;
import wo.wocom.xwell.utility.XA_util_dexclassloader;
import wo.wocom.xwell.utility.XA_util_fileExits;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author wuwenjie
 * @date 20130418
 * @version 1.3.10.3.19:2
 * @more 动态加载sd卡中的jar，实现调用其方法，查询ip
 * 
 */
public class GT_loadjar extends Activity {

	EditText et;
	Button bt;
	TextView tv;
	XA_util_dexclassloader dl;
	Method me;

	Object ob;

	// 生命开始
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pac_db_sqleditui); // 设置主布局

		et = (EditText) findViewById(R.id.pac_db_sqlui_et);
		bt = (Button) findViewById(R.id.pac_db_sqlui_bt);
		tv = (TextView) findViewById(R.id.pac_db_sqlui_tv);

		String jp = "/sdcard/jar/test.jar";

		if (XA_util_fileExits.dofExits(jp)) {

			Log.i("GT_loadjar", "jp if");

			dl = new XA_util_dexclassloader(jp, "/sdcard/", null,
					ClassLoader.getSystemClassLoader());
		} else {
			Log.i("GT_loadjar", "jp else");

			new Thread() {
				public void run() {
					try {
						sleep(5);
						GT_loadjar.this.finish();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();

		}

	}

	// Button bt 监听方法 android:onClick="bt_onClick"
	public void bt_onClick(View v) {

		String t = null;
		bt.setEnabled(true);
		t = et.getText().toString();

		Class<?> sdjar_cls = dl
				.getDexReulstClass("tk.wuwenjie.ipsearch.mainclass");
		// 自定义返回指定类

		Log.i("sdjar_cls", sdjar_cls.toString());

		try {
			ob = sdjar_cls.newInstance();
			// 创建此 sdjar_cls 对象所表示的类的一个新实例
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

		// docs.oracle.com/javase/tutorial/reflect/member/methodInvocation.html
		Method[] allMethods = ob.getClass().getDeclaredMethods();
		// 返回Method对象数组，反映此Class表示的类或接口声明的所有方法，
		// 包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法。

		for (Method m : allMethods) {

			Log.i("DeclaredMethods", m.getName() + ":" + m.toGenericString());
			// getName 以 String 形式返回此 Method 对象表示的方法名称。
			// toGenericString 返回描述此 Method 的字符串，包括类型参数。

		}

		try {
			me = ob.getClass().getDeclaredMethod("getAddresses", String.class,
					String.class);
			// Method getDeclaredMethod(String name, Class… parameterTypes)
			// 返回一个 Method 对象，该对象反映此 Class 对象所表示的类或接口的指定已声明方法。
			// Class… parameterTypes

			try {

				bt.setClickable(false);

				me.setAccessible(true);// 设置无障碍,在使用时取消[访问控制检查]
				// JDK安全检查耗时较多.通过关闭安全检查就可提升反射速度目的

				t = (String) me.invoke(ob, "ip=" + t, "utf-8");
				// invoke 动态调用 Returns the result of dynamically invoking this
				// method.

			} catch (IllegalArgumentException e) {

				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (InvocationTargetException e) {

				e.printStackTrace();
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}// try

		if (t != null) {
			bt.setEnabled(true);
		} else {
			bt.setEnabled(false);

		}
		tv.setText(t);

	}// bt_onClick
}
