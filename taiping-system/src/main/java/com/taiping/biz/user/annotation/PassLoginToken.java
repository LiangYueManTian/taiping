package com.taiping.biz.user.annotation;

import java.lang.annotation.*;

/**
 * @author zhangliangyu
 * @since 2019/9/26
 * 用来跳过Token验证得注解 @PassLoginToken
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PassLoginToken {
    boolean required() default true;
}
