package com.mwj.cms.framework.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Configuration
public class SchedulerFactoryBeanConfig implements SchedulerFactoryBeanCustomizer {

    @Autowired
    private DataSource dataSource;

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        // 延时启动
        schedulerFactoryBean.setStartupDelay(10);
        // 设置自动启动，默认为true
        schedulerFactoryBean.setAutoStartup(true);
        // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        // 是org.springframework.scheduling.quartz.SchedulerFactoryBean这个类中把spring上下文以key/value的方式存放在了quartz的上下文中了，可以用applicationContextSchedulerContextKey所定义的key得到对应的spring上下文
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContextKey");

        schedulerFactoryBean.setSchedulerName("MwjCmsScheduler");

        schedulerFactoryBean.setDataSource(dataSource);

    }
}
