package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.service.UserOnlineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

/**
 * @author Meng Wei Jin
 */
@Api(description = "在线用户管理接口")
@Controller
@RequestMapping("/sys/user-online")
public class UserOnlineController extends BaseController {

    @Autowired
    private UserOnlineService userOnlineService;

    @RolesAllowed("system_userOnline_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/user/userOnlineIndex";
    }

    @ApiOperation(value = "在线用户分页查询")
    @Log(module = LogModule.USER_ONLINE, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<Map<String, Object>> queryPage(IPage<Map<String, Object>> page){
        return userOnlineService.selectPageVO(page);
    }

    @RolesAllowed("system_userOnline_invalidateSession")
    @ApiOperation(value = "强制下线用户")
    @Log(module = LogModule.USER_ONLINE, type = LogType.INVALIDATE_SESSION)
    @PostMapping("/invalidateSession/{loginName}")
    @ResponseBody
    public Result invalidateSession(@PathVariable("loginName") String loginName){
        userOnlineService.invalidateSession(loginName);
        return Result.success();
    }
}
