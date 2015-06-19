package com.ysten.handle;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.ysten.analyse.AnonAnalyse;
import com.ysten.analyse.JHeaderAnalyse;
import com.ysten.analyse.JsonAnalyse;


public class ArgsJson extends AnalyseArgs {

	@SuppressWarnings("unchecked")
	@Override
	protected Map<Object, Object> stamp(Map<Object, Object> argsMap,
			Object object) {
		try {
			/**������@JHeader��ͷ����valueֵ*/
			AnonAnalyse anonAnalyse=new JHeaderAnalyse();
			String header = (String)anonAnalyse.parse(object);
			argsMap.put("header", header);
			/**������@Json��valueֵ*/
			anonAnalyse=new JsonAnalyse();
			List<String> jsonList = (List<String>)anonAnalyse.parse(object);
			argsMap.put("json", jsonList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return argsMap;
	}
	/**����ƴ�Ӻõ��ַ���*/
	@Override
	public String handle(Map<Object, Object> argsMap) {
		/** argcollction=[{"objectname":"��Ц�桶����������","objecactor":"����"}]*/
		String header = (String)argsMap.get("header");
		argsMap.remove("header");
		@SuppressWarnings("unchecked")
		List<String> jsonList=(List<String>)argsMap.get("json");
		
		String args = "[{";
		Set<Entry<Object, Object>> entrySet = argsMap.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String key=(String)entry.getKey();
			for(String json:jsonList){
				if(key.equals(json)){
					String value = (String)entry.getValue();
					args += "\"" + key + "\"" + ":" + "\"" + value + "\"" + ",";
					break;
				}
			}
		}
		args = header + "=" + args.substring(0, args.length() - 1);
		args += "}]";
		return args;
	}

}
