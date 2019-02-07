package com.mwj.cms.framework.datasource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 自定义多数据源切换注解
 *
 * @author Meng Wei Jin
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetDataSource {
    /**
     * 切换数据源名称
     */
    String value() default DynamicDataSource.MASTER;
}
