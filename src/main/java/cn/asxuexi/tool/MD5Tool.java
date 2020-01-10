package cn.asxuexi.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Tool {
	public static String createMD5Data(String message) {
		MessageDigest md5=null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		char[] chars = message.toCharArray();
		byte[] bytes = new byte[chars.length];
		for (int i = 0; i < chars.length; i++) {
			bytes[i] = (byte)chars[i];
		}
		byte[] md5bytes = md5.digest(bytes);
		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5bytes.length; i++) {
			int val = ((int) md5bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
		
	}
}
