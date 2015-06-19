package com.ysten.filter;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

/** ������ӵ�ַ�Ƿ�����ͨ״̬,�����Ƿ����ӵ�ַ��Ч */
public class connectivityFilter implements IptablesFilter {
	private Object object;

	public connectivityFilter(Object object) {
		this.object = object;
	}

	@Override
	public void doFilter() throws Exception {
		//http://www.believeus.cn/
		Field field = object.getClass().getDeclaredField("url");
		field.setAccessible(true);
		String url = (String) field.get(object);
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		int responseCode = conn.getResponseCode();
		if(responseCode==404){
			throw new Exception("status 404:resource is not available.");
		}else if(responseCode==500){
			throw new Exception("status 500:server Error");
		}
		
	}

}
