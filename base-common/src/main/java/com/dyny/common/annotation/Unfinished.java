package com.dyny.common.annotation;

import java.lang.annotation.*;

/**
 * @Author wanggl
 * @Description 未完成标注, 只作提醒, 对程序无实际影响, 表示类或方法未完成
 * @create 15:57 2018-11-22
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Unfinished {
}
