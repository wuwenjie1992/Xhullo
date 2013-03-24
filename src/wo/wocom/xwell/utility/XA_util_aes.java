package wo.wocom.xwell.utility;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

/**
 * @author wuwenjie
 * @date 20130316
 * @version 1.3.10.3.16:2
 * @more AES 加密方法，生成密钥（未使用，与pc结果相异 Provider）
 * @url http://coolshell.cn/articles/3161.html
 *      http://blog.csdn.net/hbcui1984/article/details/5753083
 *      http://blog.csdn.net/hbcui1984/article/details/5201247
 *      http://www.importnew.com/3456.html
 *      http://www.cjsdn.net/doc/jdk50/java/security
 *      /SecureRandom.html#getInstance%28java.lang.String%29
 */
public class XA_util_aes {

	public static final String AES = "AES"; // 加密方法
	// javax.crypto.BadPaddingException: pad block corrupted
	public static String CRYPT_KEY = "wuwenjiewuwenjie"; // 密钥

	public static byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	// java.security.InvalidKeyException: no IV set when one expected

	// 解密
	public final static String decrypt(String data) {
		try {
			return new String(decrypt(hex2byte(data.getBytes("utf-8")),
					CRYPT_KEY));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 加密
	public final static String encrypt(String data) {
		try {
			return byte2hex(encrypt(data.getBytes("utf-8"), CRYPT_KEY));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	// 生成密钥 与pc存在问题 Provider
	public static SecretKey geneKey(String key) throws NoSuchAlgorithmException {

		// 生成一个256位密钥
		final int outputKeyLength = 256;

		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		Log.i("sr", "secureRandom:" + secureRandom.getProvider().getInfo());
		// SecureRandom 加密的强随机数生成器 (RNG)
		// 生成实现指定随机数生成器 (RNG) 算法的 SecureRandom 对象

		secureRandom.setSeed(key.getBytes());// 重新提供此随机对象的种子
		// Do *not* seed secureRandom! Automatically seeded from system entropy.
		// 不要给secureRandom一个固定的种子！通过系统熵值产生随机的种子

		KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
		// 密钥生成器 指定算法生成一个 KeyGenerator 对象
		keyGenerator.init(outputKeyLength, secureRandom);
		// 使用用户提供的随机源初始化此密钥生成器，使其具有确定的密钥长度
		SecretKey seckey = keyGenerator.generateKey();// 生成一个密钥
		// SecretKey秘密（对称）密钥

		return seckey;
	}

	// 加密
	public static byte[] encrypt(byte[] src, String key) throws Exception {

		//SecretKey seckey = geneKey(key);

		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), AES);

		IvParameterSpec ivspec = new IvParameterSpec(iv);// 指定一个初始化向量 (IV)

		// 加密和解密的密码 cipher 功能
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 生成一个实现指定转换的
																	// Cipher 对象
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);// 用密钥初始化,设置加密形式,密钥
		// cipher.update(src);
		return cipher.doFinal(src);// 加密数据
	}

	// 解密
	public static byte[] decrypt(byte[] src, String key) throws Exception {

		//SecretKey seckey = geneKey(key);

		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), AES);

		IvParameterSpec ivspec = new IvParameterSpec(iv);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);// 设置解密形式,密钥
		// cipher.update(src);
		return cipher.doFinal(src);// 解密数据

	}

	// 二转十六进制字符串
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	// 十六转二
	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

}
