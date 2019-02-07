package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.system.entity.JobCron;
import com.mwj.cms.system.mapper.JobCronMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Meng Wei Jin
 */
@Service
public class JobCronService extends ServiceImpl<JobCronMapper, JobCron> {

    /**
     * 查询已启用的Job Cron
     * @return
     */
    public List<JobCron> getJobCronList(){
        return this.lambdaQuery().eq(JobCron::getStatus, Status.NORMAL).orderByAsc(JobCron::getOrderNum).list();
    }
}
