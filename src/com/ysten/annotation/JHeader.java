package com.ysten.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**�鴦�Ǹ���json��ͷ����*/
/** ��ע��ֻ�ܷ��ڲ������� */
@Target(value = { ElementType.PARAMETER, ElementType.FIELD })
/**��ע���ܱ��������ȡ��*/
@Retention(RetentionPolicy.RUNTIME)
public @interface JHeader {
	String value();
}
