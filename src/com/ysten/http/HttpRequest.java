package com.ysten.http;


import com.ysten.handle.AnalyseArgs;

public interface HttpRequest {

	public abstract String send(Object object, AnalyseArgs analyseArgs);
}
