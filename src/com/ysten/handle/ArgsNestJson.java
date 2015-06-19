package com.ysten.handle;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.ysten.analyse.AnonAnalyse;
import com.ysten.analyse.JHeaderAnalyse;
import com.ysten.analyse.JsonAnalyse;

/** 嵌套json格式解析 */
// datePoint=9&titledata={type:'sd',title:'[HD]%E7%BB%BF%E8%8C%B6'}
public class ArgsNestJson extends AnalyseArgs {

	@SuppressWarnings("unchecked")
	@Override
	protected Map<Object, Object> stamp(Map<Object, Object> argsMap,
			Object object) {

		try {
			AnonAnalyse jheaderAnalyse = new JHeaderAnalyse();
			String jheader = (String) jheaderAnalyse.parse(object);
			argsMap.put("jheader", jheader);
			// 解析出带有@Json注解的参数放到一个集合中去
			AnonAnalyse jsonAnalyse = new JsonAnalyse();
			List<String> jsonList = (List<String>) jsonAnalyse.parse(object);
			argsMap.put("json", jsonList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return argsMap;
	}

	@Override
	protected String handle(Map<Object, Object> argsMap) {
		String jsonStr = "{";
		String jheader = (String) argsMap.get("jheader");
		@SuppressWarnings("unchecked")
		List<String> jsonList = (List<String>) argsMap.get("json");
		for (String json : jsonList) {
			Set<Entry<Object, Object>> keySet = argsMap.entrySet();
			Iterator<Entry<Object, Object>> it = keySet.iterator();
			while (it.hasNext()) {
				Map.Entry<Object, Object> entry = it.next();
				String key = (String) entry.getKey();
				if (key.equals(json)) {
					String value = (String) entry.getValue();
					if (key.equals("type_")) {
						key = key.substring(0, key.length() - 1);
					}
					it.remove();
					jsonStr += "\"" + key + "\"" + ":" + "\"" + value + "\""+ ",";
					break;
				}
			}
		}

		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr = jheader + "=" + jsonStr + "}";

		
		argsMap.remove("json");
		argsMap.remove("jheader");
		String args = "";
		Set<Entry<Object, Object>> entrySet = argsMap.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String key=(String)entry.getKey();
			String value=(String)entry.getValue();
			args += key + "=" + value + "&";
		}
		args = args + jsonStr;
		return args;
	}

}
