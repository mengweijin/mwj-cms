package com.mwj.cms.framework.log;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.mwj.cms.common.constant.Const;
import com.mwj.cms.common.enums.ResultStatus;
import com.mwj.cms.framework.async.AsyncFactory;
import com.mwj.cms.framework.util.AspectUtils;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.system.entity.SysLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Meng Wei Jin
 * @description 系统日志记录处理
 * @Before 是在所拦截方法执行之前执行一段逻辑。
 * @After 是在所拦截方法执行之后执行一段逻辑。
 * @Around 是可以同时在所拦截方法的前后执行一段逻辑(写在point.proceed方法前就是之前执行, 写在point.proceed方法后就是之后执行)。
 * @AfterReturning finally块中执行
 * @AfterThrowing 捕获到异常会执行
 * 执行顺序：Before, After, AfterReturning, AfterThrowing
 **/
@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AsyncFactory asyncFactory;

    @Pointcut("@annotation(com.mwj.cms.framework.log.Log)")
    public void sysLogPointCut() {
    }

    /**
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "sysLogPointCut()")
    public void afterReturning(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "sysLogPointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e);
    }

    /**
     * 记录日志
     * @param joinPoint
     * @param e
     */
    protected void handleLog(final JoinPoint joinPoint, final Exception e) {
        try {
            // 获得注解
            Log log = AspectUtils.getMethodAnnotation(joinPoint, Log.class);

            if (log == null) {
                return;
            }

            SysLog sysLog = new SysLog();

            // 模块标题, 获取注解中对方法的描述信息 用于Controller层注解
            sysLog.setTitle(log.module().getValue());

            // 操作类型, 获取注解中对方法的描述信息 用于Controller层注解
            sysLog.setType(log.type());

            // 是否需要保存request，参数和值
            if (log.saveReqData()) {
                // 获取请求的参数
                Map<String, String[]> map = new HashMap<>();
                // 通过ServletUtils.getRequest().getParameterMap()获得的对象为被锁定的对象，不能修改
                // 这里通过putAll方法，可以对基本数据类型的集合进行深拷贝，而对其他引用类型putAll不能实现深拷贝
                map.putAll(ServletUtils.getRequest().getParameterMap());
                // 移除_csrf参数
                if(map.containsKey("_csrf")){
                    map.remove("_csrf");
                }

                String params = JSONObject.toJSONString(map);
                sysLog.setReqParam(params);
            }

            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            sysLog.setMethod(className + Const.DOT + methodName);

            sysLog.setUrl(ServletUtils.getRequest().getRequestURI());

            sysLog.setOperator(ServletUtils.getLoginUser().getId());
            sysLog.setOperTime(LocalDateTime.now());
            sysLog.setOperIp(ServletUtil.getClientIP(ServletUtils.getRequest()));

            if (e != null) {
                sysLog.setStatus(ResultStatus.FAIL);
                sysLog.setErrorMsg(e.getMessage());
            } else {
                sysLog.setStatus(ResultStatus.SUCCESS);
            }

            // 开启异步任务，保存到数据库
            asyncFactory.addOperateLog(sysLog, ServletUtils.getRequest());

        } catch (Exception exp) {
            logger.error("后置通知异常！异常信息:{}", exp);
        }
    }

}
