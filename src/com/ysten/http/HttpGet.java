package com.ysten.http;

import java.lang.reflect.Field;

import com.ysten.handle.AnalyseArgs;

/** 发送get请求 */
public class HttpGet implements HttpRequest {

	@Override
	public String send(Object object, AnalyseArgs analyseArgs) {
		// 构造参数传入值验证
		// 根据对象拼接成url参数形式 例如?u=w&p=9
		String result = "";
		try {
			String args = analyseArgs.parse(object);
			String url = getUrl(object);
			result = HttpCore.get(url, args);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		return result;
	}

	private String getUrl(Object object) throws Exception {
		String url = "";
		try {
			Class<? extends Object> clazz = object.getClass();
			Field field = clazz.getDeclaredField("url");
			field.setAccessible(true);
			url = (String) field.get(object);
		} catch (NoSuchFieldException e) {
			throw new Exception(e.getMessage());
		}
		return url;
	}
}
