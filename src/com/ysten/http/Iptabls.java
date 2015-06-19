package com.ysten.http;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Iptabls {
	// ʹ�ö�̬����Դ���Ķ�����м��
	public static HttpRequest fireWall(Class<?> clazz) throws Exception {
		HttpRequest instance = (HttpRequest) clazz.newInstance();
		InvocationHandler handler = new HttpProxy(instance);
		ClassLoader classLoader = instance.getClass().getClassLoader();
		Class<?>[] interfaces = instance.getClass().getInterfaces();
		Object proxyObj = Proxy.newProxyInstance(classLoader, interfaces,
				handler);
		HttpRequest httpRequest = (HttpRequest) proxyObj;
		return httpRequest;
	}
}
