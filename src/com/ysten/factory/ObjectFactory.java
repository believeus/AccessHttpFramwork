package com.ysten.factory;

import java.util.Map;

/**��̬��������*/
public interface ObjectFactory {
	public Class<?> build(Map<String, String> argsMap)throws Exception;
}
