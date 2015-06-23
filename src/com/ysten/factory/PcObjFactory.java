package com.ysten.factory;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/** ��pc�˶�̬����Ӷ��� */
public class PcObjFactory implements Opcodes, ObjectFactory {

	@Override
	public Class<?> build(Map<String, String> argsMap) throws Exception {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);// �������ò����Զ�����
		String MyClassName = "MyClass.class";
		// ����������췽��
		PcObjFactory.init(cw, MyClassName, argsMap);
		String type = "Ljava/lang/String;";

		Set<Entry<String, String>> entrySet = argsMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String field = entry.getKey();
			cw.visitField(ACC_PRIVATE, field, type, null, null).visitEnd();
			PcObjFactory.createGetter(cw, field, type, String.class,
					MyClassName);
			PcObjFactory.createSetter(cw, field, type, String.class,
					MyClassName);
		}
		cw.visitEnd();
		// �������������
		final byte[] data = cw.toByteArray();

		Class<?> clazz = null;
		try {
			// �����ǰ����ϵͳ��android����ϵͳ

			clazz = new ClassLoader() {
				protected Class<?> findClass(String name)
						throws ClassNotFoundException {
					return defineClass(name, data, 0, data.length);
				}
			}.loadClass(MyClassName);
		} catch (Exception e) {
			e.printStackTrace();
			String error = e.getMessage();
			System.out.println(error);
		}
		return clazz;
	}

	/* ���ɹ��캯������ʼ�����������ֵ */
	@SuppressWarnings("deprecation")
	private static void init(ClassWriter cw, String className,
			Map<String, String> argsMap) {
		cw.visit(V1_5, ACC_PUBLIC, className, null, "java/lang/Object", null);
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null,
				null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		Set<Entry<String, String>> entrySet = argsMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String value = entry.getValue();
			String filed = entry.getKey();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn(value);
			// putfield   Ϊָ�����ʵ��������ֵ;
			mv.visitFieldInsn(PUTFIELD, className, filed, "Ljava/lang/String;");
		}
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();

	}

	/** ����setter���� */
	private static void createSetter(ClassWriter cw, String field, String type,
			Class<?> c, String className) {
		String methodName = "set"
				+ field.substring(0, 1).toUpperCase(Locale.getDefault())
				+ field.substring(1);
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, methodName, "(" + type
				+ ")V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(Type.getType(c).getOpcode(ILOAD), 1);
		//// putfield   Ϊָ�����ʵ��������ֵ;
		mv.visitFieldInsn(PUTFIELD, className, field, type);
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	/** ����getter���� */
	private static void createGetter(ClassWriter cw, String field,
			String returnType, Class<?> c, String className) {
		String methodName = "get"
				+ field.substring(0, 1).toUpperCase(Locale.getDefault())
				+ field.substring(1);
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, methodName, "()"
				+ returnType, null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, className, field, returnType);
		mv.visitInsn(Type.getType(c).getOpcode(IRETURN));
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}
}