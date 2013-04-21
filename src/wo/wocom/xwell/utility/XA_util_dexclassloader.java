package wo.wocom.xwell.utility;

import android.util.Log;
import dalvik.system.DexClassLoader;

/**
 * 
 * @author wuwenjie
 * @date 20130418
 * @version 1.3.10.3.19:2
 * @more dex类加载
 * 
 */
public class XA_util_dexclassloader extends DexClassLoader {

	XA_util_dexclassloader dcl = null;
	Class<?> libProviderClazz = null;
	//DexClassLoader参数	
	//dex|jar文件路径 优化后dex文件目录 包含本地库目录 父类加载器
	String dexPath, dexOutputDir, libPath;
	ClassLoader parent;

	public XA_util_dexclassloader(String dexPath, String dexOutputDir,
			String libPath, ClassLoader parent) {

		// libPath 可能是空包含的本地库 ;parent the parent class loader

		super(dexPath, dexOutputDir, libPath, parent);

		this.dexPath = dexPath;
		this.dexOutputDir = dexOutputDir;
		this.libPath = libPath;
		this.parent = parent;

	}

	public Class<?> getDexReulstClass(String className) {

		dcl = new XA_util_dexclassloader(dexPath, dexOutputDir, libPath, parent);

		try {
			libProviderClazz = Class.forName(className, true, dcl);
			
			//http://bugs.sun.com/view_bug.do?bug_id=6434149
			//dcl.loadClass(className);
			//forName按名称返回指定类
			
		} catch (ClassNotFoundException e) {
			Log.i("XA_util_dexclassloader","getDexReulstClass ClassNotFoundException");
			e.printStackTrace();
			return null;
		}

		return libProviderClazz;

	}

}
