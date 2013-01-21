package wo.wocom.xwell.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author wuwenjie wuwenjie.tk
 * @version 1.3.10.3.6：2
 * @more 正则表达式
 */
public class XA_util_readStrByregEx {

	public static String readout(String str, String regEx, int index) {

		String outStr;
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(str);
		boolean rs = mat.find();
		if (rs) {
			outStr = mat.group(index);
		} else {
			outStr = null;
		}

		return outStr;

	}
}
