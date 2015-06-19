package com.ysten.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.ysten.annotation.NotNull;

/** 判断构造方法里的参数拥有注解,并且参数都有值 */
public class NotNullFilter implements IptablesFilter {
	private Object object;

	public NotNullFilter(Object object) {
		this.object = object;
	}

	@Override
	public void doFilter() throws Exception {
		Class<?> clazz = this.object.getClass();
		List<String> parameters = new ArrayList<String>();

		ClassLoader classLoader = NotNullFilter.class.getClassLoader();
		Class<?> loadClass = classLoader.loadClass(clazz.getName());
		Constructor<?>[] constructors = loadClass.getConstructors();
		if (constructors.length > 1) {
			throw new Exception(
					"The constructor must only one.Example:Public Person(@NotNull(\"a\") String a)");
		}
		// 获取构造方法有哪些参数
		for (Constructor<?> constructor : constructors) {
			Annotation[][] parameterAnnotations = constructor
					.getParameterAnnotations();
			// System.out.println(parameterAnnotations[0][0]);
			// 参数的个数
			if (parameterAnnotations.length == 0) {
				throw new Exception(
						"The constructor must add @NotNull Annotations.Example: Public Person(@NotNull(\"a\") String a)");
			}
			// 判断构造函数的参数个数和没有注解的参数个数要保持一致
			// annotations[0][0] annotations[0][1]
			int anno = 0;
			for (Annotation[] annotations : parameterAnnotations) {
				for (Annotation annotation : annotations) {
					// 出现其他注解忽略,只检查NotNull注解
					if (annotation instanceof NotNull) {
						anno++;
						NotNull notNull = (NotNull) annotation;
						// 构造方法里的参数变为get方法 例如device变成getDeivce
						String field = notNull.value();
						// 没有为@NutNull设置value
						if (field == null || "".equals(field)) {
							throw new Exception(
									"@NotNull(\"value\"),the value must match the constructor parameter!Example: Public P(@NotNull(\"a\") String a)");
						}
						field = field.trim();
						field = "get"
								+ field.replaceFirst(
										field.substring(0, 1),
										field.substring(0, 1).toUpperCase(
												new Locale(Locale.CHINESE
														.getCountry())));
						parameters.add(field);
					}
				}
			}
			// 如果构造函数的参数和注解个数不一样报错
			int length = parameterAnnotations.length;
			if (anno != length) {
				throw new Exception(
						"you must keep @NotNull annotation as much as the constructor parameter! Example:Public Person(@NotNull(\"a\") String a,@NotNull(\"b\") String b)");
			}
		}
		// 构造函数参数的值是否为空
		for (String methodName : parameters) {
			Method method = clazz.getMethod(methodName);
			Object value = (Object) method.invoke(object);
			if (value == null || "".equals(value)) {
				methodName = methodName.replace("get", "");
				String field = methodName.replaceFirst(
						methodName.substring(0, 1),
						methodName.substring(0, 1).toLowerCase(
								new Locale(Locale.CHINESE.getCountry())));
				throw new Exception("[" + field + "]" + " can't null");
			}
		}
	}
}
