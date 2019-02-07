package com.mwj.cms.framework.scheduler;

import com.mwj.cms.framework.async.AsyncFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Meng Wei Jin
 * @description 应用调度器
 **/
@Component
public class SpringScheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AsyncFactory asyncFactory;

    /**
     * 每天晚上4点开启异常日志分析任务
     */
    @Scheduled(cron="0 0 4 * * ?")
    public void errorLogAnalysis(){
        try {
            asyncFactory.systemErrorLogAnalysis();
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
}
