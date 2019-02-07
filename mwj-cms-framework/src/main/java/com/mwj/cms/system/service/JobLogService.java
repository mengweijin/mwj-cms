package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.system.entity.JobLog;
import com.mwj.cms.system.mapper.JobLogMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 调度任务执行日志表 服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 */
@Service
public class JobLogService extends ServiceImpl<JobLogMapper, JobLog> {

}
