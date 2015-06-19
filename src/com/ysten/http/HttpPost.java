package com.ysten.http;

import java.lang.reflect.Field;

import com.ysten.handle.AnalyseArgs;

/** post �������� */
public class HttpPost implements HttpRequest {

	@Override
	public String send(Object object, AnalyseArgs analyseArgs) {
		// �����������ֵ��֤
		// ���ݶ���ƴ�ӳ�url������ʽ ����u=w&p=9
		String result = "";
		try {
			String url = getUrl(object);
			String args = analyseArgs.parse(object);
			result = HttpCore.post(url, args);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		// ���������÷���˷�������
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
