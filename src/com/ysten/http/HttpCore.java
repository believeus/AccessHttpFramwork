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
		// ����������devicetype=null&id=009
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
			// URLʹ�û���URL���ɣ����в���Ҫ�Ӳ���
			HttpPost httpPost = new HttpPost(url);
			// �����������ݼ���������
			httpPost.setEntity(requestHttpEntity);
			// ��Ҫ�ͻ��˶�������������
			HttpClient httpClient = new DefaultHttpClient();
			// ��������
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
		// ����������� ��get����Ĭ��ֻ��֧��180���ַ�����
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		// ��������
		try {
			HttpResponse response = httpClient.execute(httpGet);
			// ��ʾ��Ӧ
			result=responeResult(response);// һ��˽�з���������Ӧ�����ʾ����
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String responeResult(HttpResponse response) {
		String result = "";
		// ��ʾ��Ӧ
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
