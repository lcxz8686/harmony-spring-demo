package com.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Retention: 指定注解的保留策略, RetentionPolicy.RUNTIME: 表示这个注解将在运行时可用，允许通过反射机制来获取和处理这个注解
 * @Target: 指定注解可以应用于哪些元素, ElementType.TYPE: 表示这个注解可以应用于类、接口、枚举等类型元素
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {
    String value() default "";
}
