package com.ysten.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** ��ע��ֻ�ܷ��ڲ������� */
@Target(value = { ElementType.PARAMETER, ElementType.FIELD })
/**��ע���ܱ��������ȡ��*/
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
	String value();
}
