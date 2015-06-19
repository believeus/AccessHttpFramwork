package com.ysten.handle;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

//参数是username=wqw&age=90
public class ArgsDefault extends AnalyseArgs {
	@Override
	protected Map<Object, Object> stamp(Map<Object, Object> argsMap,Object object) {
		return argsMap;
	}

	@Override
	public String handle(Map<Object, Object> argsMap) {
		String args = "";
		// 返回参数是username=wqw&age=90
		Set<Entry<Object, Object>> entrySet = argsMap.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			args += entry.getKey() + "=" + entry.getValue() + "&";
		}
		args = args.substring(0, args.length() - 1);
		return args;
	}

}
