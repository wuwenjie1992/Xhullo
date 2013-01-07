package wo.wocom.xwell.utility;

import android.content.Context;
import android.content.Intent;
/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.0
 * @see 跳转新activity
 */
public class startACIntent {
	public static void stAcIntent(Context con, Class<?> cls) {
		/* 跳转新界面activity */
		Intent in = new Intent(); // 新建Intent意图
		in.setClass(con, cls);
		// 方便的调用名称为,返回一个Class对象
		con.startActivity(in); // 跳转
	}
}
