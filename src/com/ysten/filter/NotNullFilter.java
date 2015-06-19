package com.ysten.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.ysten.annotation.NotNull;

/** �жϹ��췽����Ĳ���ӵ��ע��,���Ҳ�������ֵ */
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
		// ��ȡ���췽������Щ����
		for (Constructor<?> constructor : constructors) {
			Annotation[][] parameterAnnotations = constructor
					.getParameterAnnotations();
			// System.out.println(parameterAnnotations[0][0]);
			// �����ĸ���
			if (parameterAnnotations.length == 0) {
				throw new Exception(
						"The constructor must add @NotNull Annotations.Example: Public Person(@NotNull(\"a\") String a)");
			}
			// �жϹ��캯���Ĳ���������û��ע��Ĳ�������Ҫ����һ��
			// annotations[0][0] annotations[0][1]
			int anno = 0;
			for (Annotation[] annotations : parameterAnnotations) {
				for (Annotation annotation : annotations) {
					// ��������ע�����,ֻ���NotNullע��
					if (annotation instanceof NotNull) {
						anno++;
						NotNull notNull = (NotNull) annotation;
						// ���췽����Ĳ�����Ϊget���� ����device���getDeivce
						String field = notNull.value();
						// û��Ϊ@NutNull����value
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
			// ������캯���Ĳ�����ע�������һ������
			int length = parameterAnnotations.length;
			if (anno != length) {
				throw new Exception(
						"you must keep @NotNull annotation as much as the constructor parameter! Example:Public Person(@NotNull(\"a\") String a,@NotNull(\"b\") String b)");
			}
		}
		// ���캯��������ֵ�Ƿ�Ϊ��
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
