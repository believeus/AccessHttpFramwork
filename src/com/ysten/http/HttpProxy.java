package com.ysten.http;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.ysten.filter.IptablesChain;
import com.ysten.filter.UrlNotNullFilter;
import com.ysten.filter.connectivityFilter;

/** ���ش���HttpRequest��send�����Ĳ��������жϺ����� */
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
			// ���������Ƿ���url������
			iptablesChain.addFilter(new UrlNotNullFilter(arg_object));
			// ���ʵ�������Ƿ����
			// ���url��ַ�Ƿ����
			iptablesChain.addFilter(new connectivityFilter(arg_object));
			// ��ӹ��캯���Ĵ������������@NotNullע��,��@NotNullע��Ĳ�����ֵ����Ϊnull
			// ʹ�������������ģʽ,�����Ҫ�Դ���Ĳ������м��,���������һ������������
			// iptablesChain.addFilter(new NotNullFilter(arg_object));
			// ����
			/* iptablesChain.addFilter(new OhterFilter()); */
			// args[1]����object
			iptablesChain.doFilter();
			result = (String) method.invoke(object, args);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		return result;
	}

}
