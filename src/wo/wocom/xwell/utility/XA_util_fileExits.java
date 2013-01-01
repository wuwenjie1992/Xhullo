package wo.wocom.xwell.utility;

import java.io.File;

import android.util.Log;

/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.2
 * @more 判断文件是否存在
 */
public class XA_util_fileExits {

	static File file = null;
	String filePath = null;

	public static boolean dofExits(String f_path) {

		if (f_path == null) {
			return false;
		}// 如果路径为空，false

		try {
			File f = new File(f_path);// 尝试新建文件
			if (!f.exists()) {
				return false;
			}// 如果文件不存在,返回假
		} catch (Exception e) {
			Log.e("XA_util_fileExits", f_path + ":" + e.toString());
		}

		return true;//返回真
	}
}
