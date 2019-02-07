package com.mwj.cms.framework.datasource;

import com.mwj.cms.framework.util.AspectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Meng Wei Jin
 * @description 多数据源处理
 * <p>
 * 通知：
 * @Before 是在所拦截方法执行之前执行一段逻辑。
 * @After 是在所拦截方法执行之后执行一段逻辑。
 * @Around 是可以同时在所拦截方法的前后执行一段逻辑(写在point.proceed方法前就是之前执行, 写在point.proceed方法后就是之后执行)。
 * @AfterReturning finally块中执行
 * @AfterThrowing 捕获到异常会执行
 * 执行顺序：Before, After, AfterReturning, AfterThrowing
 **/
@Aspect
@Order(1)
@Component
public class DataSourceAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.mwj.cms.framework.datasource.TargetDataSource)")
    public void targetDataSourcePointCut() {
    }

    @Around("targetDataSourcePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        TargetDataSource targetDataSource = AspectUtils.getMethodAnnotation(joinPoint, TargetDataSource.class);
        if (targetDataSource != null) {
            DynamicDataSource.setDataSourceKey(targetDataSource.value());
        }

        try {
            return joinPoint.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DynamicDataSource.clear();
        }
    }
}
