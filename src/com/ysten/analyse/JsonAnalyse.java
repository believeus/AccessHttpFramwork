package com.ysten.analyse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import com.ysten.annotation.Json;

public class JsonAnalyse implements AnonAnalyse{

	@Override
	public Object parse(Object object) throws Exception {
		List<String> argList=new ArrayList<String>();
		Class<?> clazz = object.getClass();
		ClassLoader classLoader = JHeaderAnalyse.class.getClassLoader();
		Class<?> loadClass = classLoader.loadClass(clazz.getName());
		Constructor<?>[] constructors = loadClass.getConstructors();
		if (constructors.length > 1) {
			throw new Exception(
					"The constructor must only one.Example:Public Person(@NotNull(\"a\") String a) or Public Person(@NotNull(\"a\") String a,@Json(\"b\")String b)");
		}
		// 获取构造方法有哪些参数
		for (Constructor<?> constructor : constructors) {
			Annotation[][] parameterAnnotations = constructor
					.getParameterAnnotations();
			// System.out.println(parameterAnnotations[0][0]);
			// 参数的个数
			if (parameterAnnotations.length == 0) {
				throw new Exception(
						"The constructor must add @NotNull or @Json Annotations.Example: Public Person(@Json(\"b\")String b,@NotNull(\"a\") String a))");
			}
			// 判断构造函数的参数个数和没有注解的参数个数要保持一致
			boolean notAnotation = true;
			for (Annotation[] annotations : parameterAnnotations) {
				for (Annotation annotation : annotations) {
					if (annotation instanceof Json) {
						notAnotation = false;
						Json json = (Json) annotation;
						argList.add(json.value());
					}
				}
			}
			if (notAnotation) {
				throw new Exception(
						"The constructor must add @Json Annotations.Example: Public Person(@Json(\"b\")String b)),@NotNull(\"a\") String a");
			}
		}
		return argList;
	}
	
}
