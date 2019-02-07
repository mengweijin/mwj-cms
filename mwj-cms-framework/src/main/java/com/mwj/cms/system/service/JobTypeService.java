package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.system.entity.JobType;
import com.mwj.cms.system.mapper.JobTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 */
@Service
public class JobTypeService extends ServiceImpl<JobTypeMapper, JobType> {

    /**
     * 获取正常状态的Job类型集合
     * @return
     */
    public List<JobType> getJobTypeList(){
        return this.lambdaQuery()
                .eq(JobType::getStatus, Status.NORMAL)
                .orderByAsc(JobType::getCreateTime)
                .list();
    }
}
