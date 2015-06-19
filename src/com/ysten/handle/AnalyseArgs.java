package com.ysten.handle;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
/**使用模板方法解决不同参数传参问题*/
public abstract class AnalyseArgs {
	// 拼接属性和值
	public String parse(Object object) throws Exception {
		Map<Object, Object> argsMap = new Hashtable<Object, Object>();
		//打上特殊标记
		argsMap=stamp(argsMap,object);
		String value = "";
		Method[] methods = object.getClass().getDeclaredMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.contains("get")) {
				try {
					// 判断方法返回值的类型
					Class<?> returnType = method.getReturnType();
					// 如果是字符串数组类型
					if (returnType.isArray()) {
						// 将字符串数组toString
						String[] arrays = (String[]) method.invoke(object);
						value = Arrays.toString(arrays);
						value = value.replace("[", "").replace("]", "");
					} else {
						// 调用方法,获得返回值
						value = (String) method.invoke(object);
					}
					if (value == null || "".equals(value)) {
						continue;
					}
					// 讲方法名的get去除
					methodName = methodName.replace("get", "");
					// 讲单词的第一个字符小写
					methodName = methodName.replaceFirst(
							methodName.substring(0, 1),
							methodName.substring(0, 1).toLowerCase(
									new Locale(Locale.CHINESE.getCountry())));
					argsMap.put(methodName, value);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/**返回拼接之后的字符串*/
		return handle(argsMap);
	}
	/**给参数map打上标记,为以后的分析做基础*/
	/**第二个参数Object:原来的实体对象*/
	protected abstract Map<Object, Object> stamp(Map<Object, Object> argsMap,Object object); 
	/**处理参数,如何拼接是子类的事情*/
	protected abstract String handle(Map<Object, Object> argsMap);
}
