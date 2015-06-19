package com.ysten.factory;

import java.util.Map;

/**动态创建对象*/
public interface ObjectFactory {
	public Class<?> build(Map<String, String> argsMap)throws Exception;
}
