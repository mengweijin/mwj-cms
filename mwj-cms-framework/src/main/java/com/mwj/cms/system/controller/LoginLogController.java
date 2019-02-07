package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.system.entity.LoginLog;
import com.mwj.cms.system.service.LoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;

/**
 * @author Meng Wei Jin
 */
@Api(description = "登录日志管理接口")
@Controller
@RequestMapping("/sys/login-log")
public class LoginLogController extends BaseController {

    @Autowired
    private LoginLogService loginLogService;

    @RolesAllowed("system_loginLog_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/log/loginLogIndex";
    }

    @ApiOperation(value = "登录日志查询")
    @Log(module = LogModule.LOGIN_LOG, type = LogType.SELECT)
    @PostMapping("/query")
    @ResponseBody
    public List<LoginLog> query(LoginLog loginLog){
        return loginLogService.list(new QueryWrapper<>(loginLog));
    }

    @ApiOperation(value = "登录日志分页查询")
    @Log(module = LogModule.LOGIN_LOG, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<Map<String, Object>> queryTable(IPage<Map<String, Object>> page, LoginLog loginLog){
        return loginLogService.selectPageVO(page, loginLog);
    }
}
