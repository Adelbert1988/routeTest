package com.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: chw
 * Date: 2018/1/22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RouteLink {
    String value(); //路径path
    String name() default "";  //名称
    int extra() default Integer.MIN_VALUE; //扩展开关
    boolean isNeedLogin() default false; //是否需要登陆
}
