package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.common.enums.*;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.Job;
import com.mwj.cms.system.entity.JobLog;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.service.JobLogService;
import com.mwj.cms.system.service.JobService;
import com.mwj.cms.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;

/**
 * @author Meng Wei Jin
 */
@Api(description = "调度管理接口")
@Controller
@RequestMapping("/sys/job")
public class JobController extends BaseController {

    @Autowired
    private JobService jobService;

    @RolesAllowed("system_job_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/job/jobIndex";
    }

    @ApiOperation(value = "Job新增")
    @GetMapping("/add")
    public String add(){
        setAttribute("job", new Job<>().setMisfirePolicy(MisfirePolicy.MISFIRE_FIRE_AND_PROCEED));
        return "system/job/jobAdd";
    }

    @ApiOperation(value = "JOb编辑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID")
    })
    @Log(module = LogModule.JOB, type = LogType.UPDATE)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id){
        setAttribute("job", jobService.getById(id));
        return "system/job/jobAdd";
    }

    @RolesAllowed("system_job_insert")
    @ApiOperation(value = "Job新增")
    @Log(module = LogModule.JOB, type = LogType.INSERT)
    @PostMapping("/insert")
    @ResponseBody
    public Result insert(Job job) {
        User loginUser = ServletUtils.getLoginUser();
        job.setCreateBy(loginUser.getId())
                .setUpdateBy(loginUser.getId())
                .setStatus(JobSchedulerStatus.DRAFT);
        return Result.resultByBoolean(jobService.save(job));
    }

    @RolesAllowed("system_job_update")
    @ApiOperation(value = "Job编辑")
    @Log(module = LogModule.JOB, type = LogType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public Result update(Job job){
        job.setUpdateBy(ServletUtils.getLoginUser().getId());
        return Result.resultByBoolean(jobService.updateById(job));
    }

    @RolesAllowed("system_job_delete")
    @ApiOperation(value = "Job删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID")
    })
    @Log(module = LogModule.JOB, type = LogType.DELETE)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Integer id){
        return Result.resultByBoolean(jobService.deleteById(id));
    }

    @ApiOperation(value = "Job分页查询")
    @Log(module = LogModule.JOB, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<Map<String, Object>> queryPage(IPage<Map<String, Object>> page, Job job){
        return jobService.selectPageVO(page, job);
    }

    @ApiOperation(value = "判断是否为有效的Cron表达式")
    @Log(module = LogModule.JOB, type = LogType.OTHER)
    @GetMapping("/validCron")
    @ResponseBody
    public boolean validCron(String cron){
        return jobService.isValidCronExpression(cron);
    }

    @RolesAllowed("system_job_release")
    @ApiOperation(value = "发布Job，加入Quartz")
    @Log(module = LogModule.JOB, type = LogType.OTHER)
    @PostMapping("/release")
    @ResponseBody
    public Result release(Integer id) throws SchedulerException {
        return Result.resultByBoolean(jobService.release(id));
    }

    @RolesAllowed("system_job_pause")
    @ApiOperation(value = "暂停Job，从Quartz暂停")
    @Log(module = LogModule.JOB, type = LogType.OTHER)
    @PostMapping("/pause")
    @ResponseBody
    public Result pause(Integer id) throws SchedulerException {
        return Result.resultByBoolean(jobService.pause(id));
    }

    @RolesAllowed("system_job_resume")
    @ApiOperation(value = "恢复Job，从Quartz恢复")
    @Log(module = LogModule.JOB, type = LogType.OTHER)
    @PostMapping("/resume")
    @ResponseBody
    public Result resume(Integer id) throws SchedulerException {
        return Result.resultByBoolean(jobService.resume(id));
    }

    @RolesAllowed("system_job_finish")
    @ApiOperation(value = "结束Job，从Quartz结束")
    @Log(module = LogModule.JOB, type = LogType.OTHER)
    @PostMapping("/finish")
    @ResponseBody
    public Result finish(Integer id) throws SchedulerException {
        return Result.resultByBoolean(jobService.finish(id));
    }

    @RolesAllowed("system_job_log")
    @ApiOperation(value = "Job执行日志")
    @Log(module = LogModule.JOB, type = LogType.OTHER)
    @GetMapping("/log")
    public String log(Integer jobId) {
        setAttribute("jobId", jobId);
        return "system/job/jobLogIndex";
    }
}
