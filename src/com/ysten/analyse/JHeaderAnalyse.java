package com.ysten.analyse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.ysten.annotation.JHeader;

public class JHeaderAnalyse implements AnonAnalyse{
	@Override
	public  Object parse(Object object) throws Exception {
		String value = "";
		Class<?> clazz = object.getClass();
		ClassLoader classLoader = JHeaderAnalyse.class.getClassLoader();
		Class<?> loadClass = classLoader.loadClass(clazz.getName());
		Constructor<?>[] constructors = loadClass.getConstructors();
		if (constructors.length > 1) {
			throw new Exception(
					"The constructor must only one.Example:Public Person(@NotNull(\"a\") String a) or Public Person(@NotNull(\"a\") String a,@JHeader(\"b\")String b)");
		}
		// ��ȡ���췽������Щ����
		for (Constructor<?> constructor : constructors) {
			Annotation[][] parameterAnnotations = constructor
					.getParameterAnnotations();
			// System.out.println(parameterAnnotations[0][0]);
			// �����ĸ���
			if (parameterAnnotations.length == 0) {
				throw new Exception(
						"The constructor must add @NotNull or @JHeader Annotations.Example: Public Person(@JHeader(\"b\")String b,@NotNull(\"a\") String a))");
			}
			// �жϹ��캯���Ĳ���������û��ע��Ĳ�������Ҫ����һ��
			boolean notAnotation = true;
			for (Annotation[] annotations : parameterAnnotations) {
				for (Annotation annotation : annotations) {
					if (annotation instanceof JHeader) {
						notAnotation = false;
						JHeader jHeader = (JHeader) annotation;
						value = jHeader.value();
						return value;
					}
				}
			}
			if (notAnotation) {
				throw new Exception(
						"The constructor must add @JHeader Annotations.Example: Public Person(@JHeader(\"b\")String b)),@NotNull(\"a\") String a");
			}
		}
		throw new Exception(
				"The constructor @JHeader(\"value\"),the value must not null");
	}

}