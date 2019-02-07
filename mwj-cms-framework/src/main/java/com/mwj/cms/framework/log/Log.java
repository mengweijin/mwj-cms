package com.mwj.cms.framework.log;

import com.mwj.cms.common.enums.LogType;

import java.lang.annotation.*;

/**
 * @author Meng Wei Jin
 * @description 自定义操作日志记录注解
 **/
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块
     */
    LogModule module() default LogModule.OTHER;

    /**
     * 操作类型
     */
    LogType type() default LogType.OTHER;

    /**
     * 是否保存请求的参数
     */
    boolean saveReqData() default true;

}
