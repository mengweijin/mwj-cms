package com.mwj.cms.framework.quartz;

import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mwj.cms.system.entity.Job;

import java.util.Date;

/**
 * @description
 * @author Meng Wei Jin
 */
@Component
public class QuartzService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Scheduler scheduler;

    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(Integer jobId) {
        return TriggerKey.triggerKey(QuartzConst.TRIGGER_KEY + jobId);
    }

    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(Integer jobId) {
        return JobKey.jobKey(QuartzConst.JOB_KEY + jobId);
    }

    /**
     * 获取表达式触发器
     */
    public CronTrigger getCronTrigger(Integer jobId) throws SchedulerException {
        return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
    }

    /**
     * 新增定时任务
     */
    public void addJob(Job job) throws SchedulerException {
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(DefaultQuartzJobBean.class)
                .withIdentity(getJobKey(job.getId()))
                .build();

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());

        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

        // 构建一个trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(job.getId())).withSchedule(cronScheduleBuilder).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(QuartzConst.JOB, job);

        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 新增立即执行任务
     */
    public void addRunNowJob(Job job) throws SchedulerException {
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(DefaultQuartzJobBean.class)
                .withIdentity(getJobKey(job.getId()))
                .build();

        // 构建一个trigger，立即执行
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(job.getId()))
                .startNow()
                .build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(QuartzConst.JOB, job);

        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 更新定时任务
     */
    public void updateJob(Job job) throws SchedulerException {

        TriggerKey triggerKey = getTriggerKey(job.getId());

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());

        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

        CronTrigger trigger = getCronTrigger(job.getId());

        // 按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

        // 参数
        trigger.getJobDataMap().put(QuartzConst.JOB, job);

        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 立即执行一次定时任务
     */
    public void runNow(Job job) throws SchedulerException {
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(QuartzConst.JOB, job);
        scheduler.triggerJob(getJobKey(job.getId()), dataMap);
    }

    /**
     * 暂停任务
     */
    public void pauseJob(Integer jobId) throws SchedulerException {
        scheduler.pauseJob(getJobKey(jobId));
    }

    /**
     * 恢复任务
     */
    public void resumeJob(Integer jobId) throws SchedulerException {
        scheduler.resumeJob(getJobKey(jobId));
    }

    /**
     * 删除定时任务
     */
    public void deleteJob(Integer jobId) throws SchedulerException {
        scheduler.deleteJob(getJobKey(jobId));
    }

    /**
     * 处理misfire策略
     * @param job
     * @param cb
     * @return
     */
    private static CronScheduleBuilder handleCronScheduleMisfirePolicy(Job job, CronScheduleBuilder cb) {
        switch (job.getMisfirePolicy()) {
            case MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            case MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            default:
                return cb.withMisfireHandlingInstructionFireAndProceed();
        }
    }

    /**
     * 判断是否为有效的Cron表达式
     * @param cronExpression
     * @return
     */
    public boolean isValidCronExpression(String cronExpression){
        CronTriggerImpl trigger = new CronTriggerImpl();
        try {
            trigger.setCronExpression(cronExpression);
            Date date = trigger.computeFirstFireTime(null);
            return date != null && date.after(new Date());
        } catch (Exception e) {

        }
        return false;
    }
}
