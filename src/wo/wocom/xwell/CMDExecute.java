package wo.wocom.xwell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author  	wuwenjie	wuwenjie.tk
 * @version  1.3.1
 * @more		命令执行【自定义类】，传入【作为【命令【CMD】】执行的】参数
 */
public class CMDExecute {
	/*命令执行自定义类*/
	
	public synchronized String run
			(String[] cmd, String workdirectory)throws IOException {
		
		    String result = "";

		    try {
		    		ProcessBuilder builder = new ProcessBuilder(cmd);
		    		// set working directory
		    		if (workdirectory != null)
		    			builder.directory(new File(workdirectory));
		    		
		    		builder.redirectErrorStream(true);
		    		Process process = builder.start();
		    		InputStream in = process.getInputStream();
		    		byte[] re = new byte[1024];
		    		while (in.read(re) != -1) {
		    			System.out.println(new String(re));
		    			result = result + new String(re);
		    										}
		    		in.close();//关闭流

		    	} 
		    catch (Exception ex) { ex.printStackTrace();}
		    
		    return result;//返回结果
		  }

		

}
