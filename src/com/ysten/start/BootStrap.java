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
								// 从缓存中获取

								String result = "请传入参数!";
								Map<String, String> argsMap = new HashMap<String, String>();
								String queryString = "";
								String method = requset.getMethod();
								// TODO 测试用
								// response.getWriter().println("method:"+method);
								// 发送的是post请求
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
									// 去掉最后一个&
									queryString = queryString.substring(0,
											queryString.length() - 1);
									// 发送的是GET请求
								} else if (method.equals("GET")) {
									queryString = requset.getQueryString();
								}
								// TODO 测试用
								// response.getWriter().println("queryString:"+queryString);

								if (queryString != null) {
									try {
										for (String e : queryString.split("&")) {
											String key = e.split("=")[0];
											String value = e.split("=")[1];
											argsMap.put(key, value);
										}
										Object object = null;
										// 将传入的参数变为对象
										ObjectFactory objectFactory = new AndrObjFactory(
												BootStrap.this);
										Class<?> clazz = objectFactory.build(argsMap);
										object = clazz.newInstance();
										// 如果当前的平台不是android平台是服务器或者pc端
										// 查找缓存是否有数据
										ACache cache = ACache.get(BootStrap.this);
										String arg = requset.getRequestURL().toString() + "?" + queryString;
										// TODO 测试用
										// response.getWriter().println("arg:"+arg);
										// url太长了md5一下变成缓存的key
										String key = HttpUtil.Md5(arg);
										// TODO 测试用
										// response.getWriter().println("cache_key:"+key);
										if (cache.getAsString(key) != null) {
											result = cache.getAsString(key);
											// TODO 测试用
											//response.getWriter().println("load from cache:["+ result + "]");
										} else {
											AnalyseArgs analyseArgs = new ArgsDefault();
											result = HttpService.run(object,Method.POST, analyseArgs);
											// TODO 测试使用
											// response.getWriter().println("load from server:["+result+"]");
											// 将数据缓存一小时
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
