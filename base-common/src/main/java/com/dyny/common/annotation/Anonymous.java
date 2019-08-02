package com.dyny.common.annotation;

import java.lang.annotation.*;

/**
 * @Author wanggl(lane)
 * @Description //TODO 表示可匿名访问
 * @Date 11:49 2019-03-12
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Anonymous {
}
