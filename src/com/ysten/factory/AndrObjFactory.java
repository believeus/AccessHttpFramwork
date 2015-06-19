package com.ysten.factory;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.android.DexFile;
import dalvik.system.DexClassLoader;
import android.content.Context;

/** ��android�ֻ��˶�̬����Ӷ��� */
public class AndrObjFactory implements ObjectFactory {
	private Context context;

	public AndrObjFactory(Context context) {
		// ɾ����̬���ɵ��ļ�
		String path = context.getFilesDir().getAbsolutePath();
		File file = new File(path);
		File fileList[] = file.listFiles();
		for (File f : fileList) {
			String fileName = f.getName();
			//ɾ��.class ��.dex��׺���ļ�
			if (fileName.endsWith(".class") || fileName.endsWith(".dex")) {
				f.delete();
			}
		}
		this.context = context;
	}

	public Class<?> build(Map<String, String> argsMap) throws Exception {
		String className = AndrObjFactory.randStr();
		String MyDexClass = className + ".dex";
		Class<?> class_hoge = null;
		File dexFile = new File(context.getFilesDir(), MyDexClass);
		// ��class�ļ��ŵ�����Ŀ¼��ȥ
		// ��ȡ���ô洢λ��
		// generate "xxx.class" file via Javassist.
		final ClassPool cp = ClassPool.getDefault(context
				.getApplicationContext());

		final CtClass cls = cp.makeClass(className);
		final CtConstructor ctor = new CtConstructor(null, cls);
		ctor.setBody("{}");
		cls.addConstructor(ctor);
		Set<Entry<String, String>> entrySet = argsMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String field = entry.getKey();
			String value = entry.getValue();

			String defindField = "private String " + field + "=\"" + value
					+ "\";";
			CtField f = CtField.make(defindField, cls);
			cls.addField(f);

			String setMethed = "set"
					+ field.substring(0, 1).toUpperCase(Locale.getDefault())
					+ field.substring(1);
			String getMethed = "get"
					+ field.substring(0, 1).toUpperCase(Locale.getDefault())
					+ field.substring(1);

			String getFun = "public String " + getMethed + "() { return this."
					+ field + "; }";
			CtMethod m1 = CtMethod.make(getFun, cls);
			cls.addMethod(m1);

			String setFun = "public void " + setMethed + "(String " + field
					+ "){this." + field + "=" + field + ";}";
			CtMethod m2 = CtMethod.make(setFun, cls);
			cls.addMethod(m2);
		}

		cls.writeFile(context.getFilesDir().getAbsolutePath());

		// ����һ��class�ļ�,��class�ļ�ת����dex�ļ�
		// convert from "xxx.class" to "xxx.dex"
		final DexFile df = new DexFile();
		final String dexFilePath = dexFile.getAbsolutePath();
		df.addClass(new File(context.getFilesDir(), className + ".class"));
		df.writeFile(dexFilePath);

		if (dexFile.exists()) {
			DexClassLoader dcl = new DexClassLoader(dexFile.getAbsolutePath(),
					context.getCacheDir().getAbsolutePath(),
					context.getApplicationInfo().nativeLibraryDir,
					context.getClassLoader());
			class_hoge = dcl.loadClass(className);
		}
		return class_hoge;
	}

	private static String randStr() {
		String base = "abcdefghijklmnopqrstuvwxyABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}
