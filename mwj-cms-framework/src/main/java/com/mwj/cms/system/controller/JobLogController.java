package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.system.entity.JobLog;
import com.mwj.cms.system.service.JobLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author Meng Wei Jin
 */
@Api(description = "度任务执行日志接口")
@Controller
@RequestMapping("/sys/jobLog")
public class JobLogController extends BaseController {

    @Autowired
    private JobLogService jobLogService;

    @ApiOperation(value = "JobLog分页查询")
    @Log(module = LogModule.JOB, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<JobLog> queryPage(IPage<JobLog> page, JobLog jobLog){
        return jobLogService.page(page, new QueryWrapper<>(jobLog));
    }
}
