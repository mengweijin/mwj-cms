package com.mwj.cms.framework.quartz;

import com.mwj.cms.common.enums.JobLogStatus;
import com.mwj.cms.framework.quartz.task.QuartzTask;
import com.mwj.cms.framework.util.SpringUtils;
import com.mwj.cms.framework.web.service.PublicIPService;
import com.mwj.cms.system.entity.Job;
import com.mwj.cms.system.entity.JobLog;
import com.mwj.cms.system.entity.JobType;
import com.mwj.cms.system.service.JobLogService;
import com.mwj.cms.system.service.JobTypeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.net.InetAddress;

/**
 * @author Meng Wei Jin
 * @description
 **/
public class DefaultQuartzJobBean extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void executeInternal(JobExecutionContext content) throws JobExecutionException {
        JobLog jobLog = new JobLog();
        JobLogService jobLogService = SpringUtils.getBean(JobLogService.class);
        try {
            Job job = new Job();
            BeanUtils.copyProperties(content.getMergedJobDataMap().get(QuartzConst.JOB), job);

            JobTypeService jobTypeService = SpringUtils.getBean(JobTypeService.class);
            JobType jobType = jobTypeService.getById(job.getJobTypeId());
            String implBeanName = SpringUtils.getBeanNameByClassName(jobType.getImplClassName());
            QuartzTask quartzTask = SpringUtils.getBean(implBeanName);

            jobLog.setJobId(job.getId())
                  .setStatus(JobLogStatus.RUNNING)
                  .setIp(SpringUtils.getBean(PublicIPService.class).getPublicIp());
            // 生成任务明细
            jobLogService.save(jobLog);

            invokeMethod(quartzTask, job);

            // 更新任务明细表状态
            jobLog.setStatus(JobLogStatus.SUCCESS);
            jobLogService.updateById(jobLog);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 更新任务明细表状态
            jobLog.setStatus(JobLogStatus.FAIL).setErrorMsg(e.toString());
            jobLogService.updateById(jobLog);
            throw new JobExecutionException(e);
        }
    }

    private void invokeMethod(Object object, Job job) throws Exception {
        Class cls = object.getClass();
        Class[] paramTypes = new Class[]{Job.class};
        //获取execute方法
        Method methodExecute = cls.getMethod(QuartzConst.METHOD_EXECUTE, paramTypes);
        //获取after方法
        Method methodAfter = cls.getMethod(QuartzConst.METHOD_AFTER, paramTypes);
        //执行execute方法
        methodExecute.invoke(object, job);
        //执行after方法
        methodAfter.invoke(object, job);

    }
}
