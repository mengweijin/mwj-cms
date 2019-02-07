package com.mwj.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mwj.cms.system.entity.JobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 调度任务明细信息表 Mapper 接口
 * </p>
 *
 * @author Meng Wei Jin
 */
@Mapper
public interface JobLogMapper extends BaseMapper<JobLog> {

}
