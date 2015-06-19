package com.ysten.http;

import com.ysten.handle.AnalyseArgs;
import com.ysten.variables.Method;
import com.ysten.variables.Varaibles;

public class HttpService {

	public static String run(Object object, int method,
			AnalyseArgs analyseArgs) {
		String result = "";
		try {
			Class<?> clazz = null;
			switch (method) {
			case Method.POST:
				clazz = Class.forName(Varaibles.HTTP_POST_CLASS);
				break;
			case Method.GET:
				clazz = Class.forName(Varaibles.HTTP_GET_CLASS);
				break;
			}
			// 使用自定义防火墙对传入参数进行检查
			HttpRequest httpRequest = Iptabls.fireWall(clazz);
			result = httpRequest.send(object, analyseArgs);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		return result;
	}
}
