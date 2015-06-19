package com.ysten.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpCore {

	public static String post(String url, String parameter) {
		String result = "";
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		// 解析出参数devicetype=null&id=009
		String[] args = parameter.split("&");
		// devicetype=null
		for (String arg : args) {
			String key = arg.split("=")[0];
			String value = arg.split("=")[1];
			NameValuePair pair1 = new BasicNameValuePair(key, value);
			pairList.add(pair1);
		}
		try {
			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
			// URL使用基本URL即可，其中不需要加参数
			HttpPost httpPost = new HttpPost(url);
			// 将请求体内容加入请求中
			httpPost.setEntity(requestHttpEntity);
			// 需要客户端对象来发送请求
			HttpClient httpClient = new DefaultHttpClient();
			// 发送请求
			HttpResponse response = httpClient.execute(httpPost);
			result = responeResult(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String get(String url, String args) {
		String result="";
		url=url+"?"+args;
		// 生成请求对象 该get请求默认只能支持180个字符长度
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		// 发送请求
		try {
			HttpResponse response = httpClient.execute(httpGet);
			// 显示响应
			result=responeResult(response);// 一个私有方法，将响应结果显示出来
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String responeResult(HttpResponse response) {
		String result = "";
		// 显示响应
		HttpEntity httpEntity = response.getEntity();
		try {
			InputStream inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
