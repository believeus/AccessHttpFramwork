package com.ysten.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**查处那个是json的头参数*/
/** 改注解只能放在参数上面 */
@Target(value = { ElementType.PARAMETER, ElementType.FIELD })
/**该注解能被反射类读取到*/
@Retention(RetentionPolicy.RUNTIME)
public @interface JHeader {
	String value();
}
