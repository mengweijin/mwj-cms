package com.mwj.cms.framework.web;

import com.mwj.cms.common.exception.KaptchaException;
import com.mwj.cms.common.exception.MwjCmsException;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Meng Wei Jin
 * @description 自定义异常处理器
 * 注意，不要重复定义异常捕获，对于父类里已经定义好的，只要overwrite就好，不要重复声明异常拦截。
 **/
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * spring secuirty 访问受限 403
     * @param e
     * @return
     */
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    ResponseEntity handleException(AccessDeniedException e) {
        logger.warn(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            SchedulerException.class,
            KaptchaException.class,
            JobExecutionException.class,
            MwjCmsException.class,
            RuntimeException.class,
            Exception.class
    })
    @ResponseBody
    ResponseEntity handleException(HttpServletRequest request, Throwable e) {
        logger.error(e.getMessage(), e);
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(e.getMessage(), null, status);
    }

    /**
     * 获取请求状态码
     * @param request
     * @return
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
