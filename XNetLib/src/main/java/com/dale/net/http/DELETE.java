package com.dale.net.http;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface DELETE {
    /**
     * 内容是baseUrl之外的路径
     */
    String value() default "";
}
