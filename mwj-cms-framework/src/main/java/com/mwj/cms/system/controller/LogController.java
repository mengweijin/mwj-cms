package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.SysLog;
import com.mwj.cms.system.service.LogService;
import edu.emory.mathcs.backport.java.util.Arrays;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;

/**
 * @author Meng Wei Jin
 */
@Api(description = "系统日志管理接口")
@Controller
@RequestMapping("/sys/log")
public class LogController extends BaseController {

    @Autowired
    private LogService logService;

    @RolesAllowed("system_log_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/log/sysLogIndex";
    }

    @ApiOperation(value = "系统日志查询")
    @Log(module = LogModule.LOG, type = LogType.SELECT)
    @PostMapping("/query")
    @ResponseBody
    public List<SysLog> query(SysLog sysLog){
        return logService.list(new QueryWrapper<>(sysLog));
    }

    @ApiOperation(value = "系统日志分页查询")
    @Log(module = LogModule.LOG, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<Map<String, Object>> queryPage(IPage<Map<String, Object>> page, SysLog sysLog){
        return logService.selectPageVO(page, sysLog);
    }

    @RolesAllowed("system_syslog_batchDelete")
    @ApiOperation(value = "系统日志批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "系统日志id数组")
    })
    @Log(module = LogModule.LOG, type = LogType.DELETE)
    @PostMapping("/batchDelete")
    @ResponseBody
    public Result batchDelete(@RequestParam(value = "ids[]") Long[] ids){
        return Result.resultByBoolean(logService.removeByIds(Arrays.asList(ids)));
    }
}
