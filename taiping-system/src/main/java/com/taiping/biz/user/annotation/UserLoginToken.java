package com.taiping.biz.user.annotation;

import java.lang.annotation.*;

/**
 * @author zhangliangyu
 * @since 2019/9/26
 * 需要进行Token验证的接口加次注解 @UserLoginToken
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserLoginToken {
    boolean required() default true;
}
