package com.mwj.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mwj.cms.system.entity.JobCron;
import com.mwj.cms.system.entity.JobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Meng Wei Jin
 */
@Mapper
public interface JobCronMapper extends BaseMapper<JobCron> {

}
