package wo.wocom.xwell.utility;

import android.content.Context;
import android.content.Intent;
/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.0
 * @see 跳转新activity
 */
public class startACIntent {
	/**
	 * 
	 * @param con  所在Activity的Context(上下文):	Activity.this
	 * @param cls  新建的Activity:					Activity.class
	 */
	public static void stAcIntent(Context con, Class<?> cls) {
		/* 跳转新界面activity */
		Intent in = new Intent(); // 新建Intent意图
		in.setClass(con, cls);
		//返回一个Class对象
		con.startActivity(in); // 跳转
	}
}
