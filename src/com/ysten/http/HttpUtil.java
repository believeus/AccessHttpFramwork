package com.ysten.http;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HttpUtil {
	//通过反射获取url
	public static String getUrl(Object object){
		String url="";
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField("url");
			field.setAccessible(true);
			url = (String) field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
			url=e.getMessage();
		} 
		return url;
	} 
	
	public static String Md5(String plainText) {
		StringBuffer buf=null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}


		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}
}
