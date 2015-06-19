package com.ysten.filter;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

/** 检查链接地址是否是连通状态,或者是否链接地址有效 */
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
