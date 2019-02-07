package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.InterfaceType;
import com.mwj.cms.common.enums.JobSchedulerStatus;
import com.mwj.cms.common.enums.MisfirePolicy;
import com.mwj.cms.common.enums.Sex;
import com.mwj.cms.framework.quartz.QuartzService;
import com.mwj.cms.system.entity.Job;
import com.mwj.cms.system.entity.JobLog;
import com.mwj.cms.system.entity.Role;
import com.mwj.cms.system.mapper.JobMapper;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.SchedulerException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 * @since 2018-10-28
 */
@Transactional(rollbackFor = {SchedulerException.class, Exception.class, RuntimeException.class})
@Service
public class JobService extends ServiceImpl<JobMapper, Job> {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private QuartzService quartzService;

    @Autowired
    private JobLogService jobLogService;

    /**
     * 获取计划错过执行时间策略枚举类集合
     * @return
     */
    public MisfirePolicy[] getMisfirePolicies(){
        return MisfirePolicy.values();
    }

    /**
     * 获取状态枚举类（0草稿 1正常 2暂停 3结束）集合
     * @return
     */
    public JobSchedulerStatus[] getJobSchedulerStatus(){
        return JobSchedulerStatus.values();
    }

    /**
     * 自定义分页查询
     * @param page
     * @param job
     * @return
     */
    public IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, Job job) {
        page = jobMapper.selectPageVO(page, job);
        if(page.getTotal() > 0){
            List<Map<String, Object>> recordList = page.getRecords();
            recordList.stream()
                    .map(map -> {
                        map.put("misfirePolicy", MisfirePolicy.getDesc(String.valueOf(map.get("misfirePolicy"))));
                        map.put("status", JobSchedulerStatus.getDesc(String.valueOf(map.get("status"))));
                        return map;
                    })
                    .collect(Collectors.toList());
        }

        return page;
    }

    /**
     * 判断是否为有效的Cron表达式
     * @param cronExpression
     * @return
     */
    public boolean isValidCronExpression(String cronExpression){
        return quartzService.isValidCronExpression(cronExpression);
    }

    /**
     * 发布Job，加入Quartz
     * @param id
     * @return
     * @throws SchedulerException
     */
    public Boolean release(Integer id) throws SchedulerException {
        quartzService.addJob(this.getById(id));
        boolean bool = this.updateById(new Job().setId(id).setStatus(JobSchedulerStatus.NORMAL));
        return bool;
    }

    /**
     * 暂停Job，从Quartz暂停
     * @param id
     * @return
     * @throws SchedulerException
     */
    public Boolean pause(Integer id) throws SchedulerException {
        quartzService.pauseJob(id);
        boolean bool = this.updateById(new Job().setId(id).setStatus(JobSchedulerStatus.PAUSE));
        return bool;
    }

    /**
     * 恢复Job，从Quartz恢复
     * @param id
     * @return
     * @throws SchedulerException
     */
    public Boolean resume(Integer id) throws SchedulerException {
        quartzService.resumeJob(id);
        boolean bool = this.updateById(new Job().setId(id).setStatus(JobSchedulerStatus.NORMAL));
        return bool;
    }

    /**
     * 结束Job，从Quartz结束
     * @param id
     * @return
     * @throws SchedulerException
     */
    public Boolean finish(Integer id) throws SchedulerException {
        quartzService.deleteJob(id);
        boolean bool = this.updateById(new Job().setId(id).setStatus(JobSchedulerStatus.FINISHED));
        return bool;
    }

    /**
     * 删除Job，并删除管理Job的执行日志
     * @param id
     * @return
     */
    public Boolean deleteById(Integer id) {
        // 删除日志
        List<JobLog> jobLogList = jobLogService.lambdaQuery().eq(JobLog::getJobId, id).list();
        if(CollectionUtils.isNotEmpty(jobLogList)){
            List<Long> jobLogIds = jobLogList.stream()
                    .map(jobLog -> jobLog.getId())
                    .collect(Collectors.toList());
            jobLogService.removeByIds(jobLogIds);
        }

        return this.removeById(id);
    }
}
