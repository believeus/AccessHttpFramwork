package com.ysten.handle;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
/**ʹ��ģ�巽�������ͬ������������*/
public abstract class AnalyseArgs {
	// ƴ�����Ժ�ֵ
	public String parse(Object object) throws Exception {
		Map<Object, Object> argsMap = new Hashtable<Object, Object>();
		//����������
		argsMap=stamp(argsMap,object);
		String value = "";
		Method[] methods = object.getClass().getDeclaredMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.contains("get")) {
				try {
					// �жϷ�������ֵ������
					Class<?> returnType = method.getReturnType();
					// ������ַ�����������
					if (returnType.isArray()) {
						// ���ַ�������toString
						String[] arrays = (String[]) method.invoke(object);
						value = Arrays.toString(arrays);
						value = value.replace("[", "").replace("]", "");
					} else {
						// ���÷���,��÷���ֵ
						value = (String) method.invoke(object);
					}
					if (value == null || "".equals(value)) {
						continue;
					}
					// ����������getȥ��
					methodName = methodName.replace("get", "");
					// �����ʵĵ�һ���ַ�Сд
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
		/**����ƴ��֮����ַ���*/
		return handle(argsMap);
	}
	/**������map���ϱ��,Ϊ�Ժ�ķ���������*/
	/**�ڶ�������Object:ԭ����ʵ�����*/
	protected abstract Map<Object, Object> stamp(Map<Object, Object> argsMap,Object object); 
	/**�������,���ƴ�������������*/
	protected abstract String handle(Map<Object, Object> argsMap);
}
