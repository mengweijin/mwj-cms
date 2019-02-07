package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.Config;
import com.mwj.cms.system.service.ConfigService;
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
@Api(description = "配置管理接口")
@Controller
@RequestMapping("/sys/config")
public class ConfigController extends BaseController {

    @Autowired
    private ConfigService configService;

    @RolesAllowed("system_config_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/config/configIndex";
    }

    @ApiOperation(value = "配置编辑页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "配置ID")
    })
    @Log(module = LogModule.CONFIG, type = LogType.UPDATE)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id){
        Config config = configService.getById(id);
        setAttribute("config", config);
        return "system/config/configEdit";
    }

    @RolesAllowed("system_config_update")
    @ApiOperation(value = "配置编辑")
    @Log(module = LogModule.CONFIG, type = LogType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public Result update(Config config){
        config.setUpdateBy(ServletUtils.getLoginUser().getId());
        return Result.resultByBoolean(configService.updateById(config));
    }

    @ApiOperation(value = "配置查询")
    @Log(module = LogModule.CONFIG, type = LogType.SELECT)
    @PostMapping("/query")
    @ResponseBody
    public List<Config> query(Config config){
        return configService.list(new QueryWrapper<>(config));
    }

    @ApiOperation(value = "配置分页查询")
    @Log(module = LogModule.CONFIG, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<Map<String, Object>> queryPage(IPage<Map<String, Object>> page, Config config){
        return configService.selectPageVO(page, config);
    }
}
