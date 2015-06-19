package com.ysten.http;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.ysten.filter.IptablesChain;
import com.ysten.filter.UrlNotNullFilter;
import com.ysten.filter.connectivityFilter;

/** 拦截传入HttpRequest的send方法的参数进行判断和拦截 */
public class HttpProxy implements InvocationHandler {
	private Object object;

	public HttpProxy(Object object) {
		this.object = object;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		String result = "";
		try {
			Object arg_object = args[0];
			IptablesChain iptablesChain = new IptablesChain();
			// 检查对象中是否有url的属性
			iptablesChain.addFilter(new UrlNotNullFilter(arg_object));
			// 检查实体类中是否存在
			// 检查url地址是否存在
			iptablesChain.addFilter(new connectivityFilter(arg_object));
			// 添加构造函数的传入参数必须有@NotNull注解,有@NotNull注解的参数的值不许为null
			// 使用了责任链设计模式,如果需要对传入的参数进行检查,可以新添加一个责任链即可
			// iptablesChain.addFilter(new NotNullFilter(arg_object));
			// 例如
			/* iptablesChain.addFilter(new OhterFilter()); */
			// args[1]代表object
			iptablesChain.doFilter();
			result = (String) method.invoke(object, args);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		return result;
	}

}
