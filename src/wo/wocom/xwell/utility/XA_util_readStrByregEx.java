package wo.wocom.xwell.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
