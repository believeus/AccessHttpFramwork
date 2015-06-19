package com.ysten.start;

import java.io.IOException;
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
import com.ysten.factory.PcObjFactory;
import com.ysten.handle.AnalyseArgs;
import com.ysten.handle.ArgsDefault;
import com.ysten.http.HttpService;
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
								String result = "�봫�����!";
								Map<String, String> argsMap = new HashMap<String, String>();
								String queryString = requset.getQueryString();
								if (queryString != null) {
									try {
										for (String e : queryString.split("&")) {
											String key = e.split("=")[0];
											String value = e.split("=")[1];
											argsMap.put(key, value);
										}
										Object object = null;
										//��õ�ǰ�����������
										String vm = System.getProperty("java.vm.name");
										ObjectFactory objectFactory = null;
										//�����ǰ��ƽ̨��androidƽ̨
										if (vm.equalsIgnoreCase("Dalvik")) {
											//������Ĳ�����Ϊ����
											objectFactory = new AndrObjFactory(BootStrap.this);
											Class<?> clazz = objectFactory.build(argsMap);
											object = clazz.newInstance();
										//�����ǰ��ƽ̨����androidƽ̨�Ƿ���������pc��
										} else {
											//������ת��Ϊ����
											objectFactory = new PcObjFactory();
											Class<?> clazz = objectFactory.build(argsMap);
											object = clazz.newInstance();
										}
										AnalyseArgs analyseArgs = new ArgsDefault();
										result = HttpService.run(object,Method.POST, analyseArgs);
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
