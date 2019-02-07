package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.SysInterface;
import com.mwj.cms.system.service.InterfaceService;
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
@Api(description = "接口管理接口")
@Controller
@RequestMapping("/sys/interface")
public class InterfaceController extends BaseController {

    @Autowired
    private InterfaceService interfaceService;

    @RolesAllowed("system_interface_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/interface/interfaceIndex";
    }

    @ApiOperation(value = "接口编辑页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "接口ID")
    })
    @Log(module = LogModule.INTERFACE, type = LogType.UPDATE)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id){
        SysInterface sysInterface = interfaceService.getById(id);
        setAttribute("interface", sysInterface);
        return "system/interface/interfaceEdit";
    }

    @RolesAllowed("system_interface_update")
    @ApiOperation(value = "接口编辑")
    @Log(module = LogModule.INTERFACE, type = LogType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public Result update(SysInterface sysInterface){
        sysInterface.setUpdateBy(ServletUtils.getLoginUser().getId());
        return Result.resultByBoolean(interfaceService.updateById(sysInterface));
    }

    @ApiOperation(value = "接口查询")
    @Log(module = LogModule.INTERFACE, type = LogType.SELECT)
    @PostMapping("/query")
    @ResponseBody
    public List<SysInterface> query(SysInterface sysInterface){
        return interfaceService.list(new QueryWrapper<>(sysInterface));
    }

    @ApiOperation(value = "接口分页查询")
    @Log(module = LogModule.INTERFACE, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<Map<String, Object>> queryPage(IPage<Map<String, Object>> page, SysInterface sysInterface){
        return interfaceService.selectPageVO(page, sysInterface);
    }
}
