package com.ysten.http;

import java.lang.reflect.Field;

import com.ysten.handle.AnalyseArgs;

/** post 方法请求 */
public class HttpPost implements HttpRequest {

	@Override
	public String send(Object object, AnalyseArgs analyseArgs) {
		// 构造参数传入值验证
		// 根据对象拼接成url参数形式 例如u=w&p=9
		String result = "";
		try {
			String url = getUrl(object);
			String args = analyseArgs.parse(object);
			result = HttpCore.post(url, args);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		// 发送请求让服务端返回数据
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
