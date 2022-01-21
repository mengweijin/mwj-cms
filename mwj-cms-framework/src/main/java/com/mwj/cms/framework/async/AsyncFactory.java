package com.mwj.cms.framework.async;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import com.mwj.cms.common.constant.Const;
import com.mwj.cms.framework.async.task.ErrorLogAnalysisTask;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.util.SpringUtils;
import com.mwj.cms.framework.web.service.TaoBaoIpInfoService;
import com.mwj.cms.system.entity.LoginLog;
import com.mwj.cms.system.entity.SysLog;
import com.mwj.cms.system.service.LogService;
import com.mwj.cms.system.service.LoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Future;

/**
 * @author Meng Wei Jin
 * @description 异步工厂，用来产生异步任务，异步任务中无法通过自动装配获取spring bean对象，可通过SpringUtils工具类获取
 *
 * 在@SpringBootApplication启动类 添加注解@EnableAsync
 * 异步方法使用注解@Async ,返回值为void或者Future
 * 切记一点 ,异步方法和调用方法一定要写在不同的类中,如果写在一个类中,是没有效果的

 **/
@Async
@Component
public class AsyncFactory {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final AsyncResult<String> asyncResult = new AsyncResult<>(Const.SUCCESS);

    /**
     * 登录日志记录
     * @param loginLog 日志信息
     * @return
     * @throws Exception
     */
    public Future<String> addLoginLog(final LoginLog loginLog, HttpServletRequest request) {
        try{
            final UserAgent userAgent = ServletUtils.getUserAgent(request);
            TaoBaoIpInfoService taoBaoIpInfoService = SpringUtils.getBean(TaoBaoIpInfoService.class);
            loginLog.setLoginLocation(taoBaoIpInfoService.getLocationByIp(loginLog.getIp()))
                    .setBrowser(userAgent.getBrowser().getName())
                    .setOs(userAgent.getOs().getName());
            LoginLogService loginLogService = SpringUtils.getBean(LoginLogService.class);

            loginLogService.save(loginLog);

        } catch (Exception e){
            logger.debug(loginLog.toString());
            logger.error("Add loginLog record failed!", e);
        }

        return asyncResult;
    }

    /**
     * 操作日志记录
     * @param sysLog 日志信息
     * @return
     * @throws Exception
     */
    public Future<String> addOperateLog(final SysLog sysLog, HttpServletRequest request) {
        try{
            TaoBaoIpInfoService taoBaoIpInfoService = SpringUtils.getBean(TaoBaoIpInfoService.class);
            sysLog.setOperLocation(taoBaoIpInfoService.getLocationByIp(ServletUtil.getClientIP(request)));
            LogService logService = SpringUtils.getBean(LogService.class);
            logService.save(sysLog);
        } catch (Exception e){
            logger.debug(sysLog.toString());
            logger.error("Add operate log record failed!", e);
        }

        return asyncResult;
    }

    /**
     * 系统错误日志分析
     * @return
     * @throws InterruptedException
     */
    public Future<String> systemErrorLogAnalysis() {
        try {
            ErrorLogAnalysisTask errorLogAnalysisTask = SpringUtils.getBean(ErrorLogAnalysisTask.class);
            errorLogAnalysisTask.execute();
        } catch (Exception e) {
            logger.error("Analysis error log failed!", e);
        }

        return asyncResult;
    }
}
