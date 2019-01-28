package com.example.test.datasouce;

import java.lang.annotation.*;
/**
 * 作用于类，接口，方法
 * */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String name();
}
