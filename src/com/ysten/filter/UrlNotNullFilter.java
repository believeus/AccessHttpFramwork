package com.ysten.filter;

import java.lang.reflect.Field;

/** 检查url属性是否存在 */
public class UrlNotNullFilter implements IptablesFilter {
	private Object object;

	public UrlNotNullFilter(Object object){
		this.object=object;
	}

	@Override
	public void doFilter() throws Exception {
		Field field = null;
		try {
			field = object.getClass().getDeclaredField("url");
			field.setAccessible(true);
		} catch (Exception e) {
			throw new Exception("must have url field");
		}
		String url = (String) field.get(object);
		if (url == null || "".equals(url)) {
			throw new Exception("url value must not null");
		}
	}

}
