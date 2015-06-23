package com.ysten.start;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import com.ysten.factory.AndrObjFactory;
import com.ysten.factory.ObjectFactory;
import com.ysten.handle.AnalyseArgs;
import com.ysten.handle.ArgsDefault;
import com.ysten.http.ACache;
import com.ysten.http.HttpService;
import com.ysten.http.HttpUtil;
import com.ysten.variables.Method;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BootStrap extends Service {
	private Server server = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		server = new Server(9999);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (!server.isRunning()) {
			new Thread(new Runnable() {

				@Override
				public void run() {

					try {

						server.setHandler(new AbstractHandler() {

							@Override
							public void handle(String arg0,
									Request baseRequest,
									HttpServletRequest requset,
									HttpServletResponse response)
									throws IOException, ServletException {
								response.setContentType("text/html;charset=utf-8");
								response.setStatus(HttpServletResponse.SC_OK);
								baseRequest.setHandled(true);
								// �ӻ����л�ȡ

								String result = "�봫�����!";
								Map<String, String> argsMap = new HashMap<String, String>();
								String queryString = "";
								String method = requset.getMethod();
								// TODO ������
								// response.getWriter().println("method:"+method);
								// ���͵���post����
								if (method.equals("POST")) {
									Enumeration<String> en = requset
											.getParameterNames();
									while (en.hasMoreElements()) {
										String paramName = (String) en
												.nextElement();
										String paraValue = requset
												.getParameter(paramName);
										queryString += paramName + "="
												+ paraValue + "&";
									}
									// ȥ�����һ��&
									queryString = queryString.substring(0,
											queryString.length() - 1);
									// ���͵���GET����
								} else if (method.equals("GET")) {
									queryString = requset.getQueryString();
								}
								// TODO ������
								// response.getWriter().println("queryString:"+queryString);

								if (queryString != null) {
									try {
										for (String e : queryString.split("&")) {
											String key = e.split("=")[0];
											String value = e.split("=")[1];
											argsMap.put(key, value);
										}
										Object object = null;
										// ������Ĳ�����Ϊ����
										ObjectFactory objectFactory = new AndrObjFactory(
												BootStrap.this);
										Class<?> clazz = objectFactory.build(argsMap);
										object = clazz.newInstance();
										// �����ǰ��ƽ̨����androidƽ̨�Ƿ���������pc��
										// ���һ����Ƿ�������
										ACache cache = ACache.get(BootStrap.this);
										String arg = requset.getRequestURL().toString() + "?" + queryString;
										// TODO ������
										// response.getWriter().println("arg:"+arg);
										// url̫����md5һ�±�ɻ����key
										String key = HttpUtil.Md5(arg);
										// TODO ������
										// response.getWriter().println("cache_key:"+key);
										if (cache.getAsString(key) != null) {
											result = cache.getAsString(key);
											// TODO ������
											//response.getWriter().println("load from cache:["+ result + "]");
										} else {
											AnalyseArgs analyseArgs = new ArgsDefault();
											result = HttpService.run(object,Method.POST, analyseArgs);
											// TODO ����ʹ��
											// response.getWriter().println("load from server:["+result+"]");
											// �����ݻ���һСʱ
											cache.put(key, result,3600);
										}

									} catch (Exception e1) {
										e1.printStackTrace();
										result = e1.getMessage();
									}
								}
								response.getWriter().println(result);

							}

						});
						server.start();
						server.join();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

		return START_STICKY;
	}
}
