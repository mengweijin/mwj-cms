package com.mwj.cms.framework.quartz.task;

import com.mwj.cms.framework.quartz.demo.Demo;
import com.mwj.cms.system.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Component
public class DemoTask implements QuartzTask<Demo> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 如果涉及产生子任务，可在这里生成
     * @param job 方法参数
     * @return
     */
    @Override
    public void execute(Job<Demo> job) {
        Demo demo = job.getObject();
        if(demo != null){
            logger.info("Do DemoTask " + demo.getName());
        } else {
            logger.info("Do DemoTask......");
        }
        try {
            Thread.sleep(1000L * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void after(Job<Demo> job) {
        Demo demo = job.getObject();
        if(demo != null){
            logger.info("Do DemoTask " + demo.getName() + " Finished!");
        } else {
            logger.info("Do DemoTask Finished!");
        }
    }
}
