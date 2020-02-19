package com.abc.annotation;

import java.lang.annotation.*;

/**
 * 自定义权限注解
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermInfo {

    /**
     * 权限值
     * @return
     */
    String pval() default "";

    /**
     * 权限名称
     * pname的别名
     * @return
     */
    String value() default "";

}
