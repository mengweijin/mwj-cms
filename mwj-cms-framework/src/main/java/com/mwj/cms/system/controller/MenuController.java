package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.Menu;
import com.mwj.cms.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

/**
 * @author Meng Wei Jin
 */
@Api(description = "菜单管理接口")
@Controller
@RequestMapping("/sys/menu")
public class MenuController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MenuService menuService;

    @RolesAllowed("system_menu_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/menu/menuIndex";
    }

    @ApiOperation(value = "菜单编辑页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单ID")
    })
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id){
        setAttribute("menu", menuService.getById(id));
        return "system/menu/menuEdit";
    }

    @ApiOperation(value = "菜单授权页面")
    @GetMapping("/authorizationTree")
    public String authorizationTree(Integer roleId){
        setAttribute("roleId", roleId);
        return "system/menu/authorizationTree";
    }

    @RolesAllowed("system_menu_update")
    @ApiOperation(value = "菜单编辑")
    @Log(module = LogModule.MENU, type = LogType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public Result update(Menu menu){
        menu.setUpdateBy(ServletUtils.getLoginUser().getId());
        return Result.resultByBoolean(menuService.updateById(menu));
    }

    @ApiOperation(value = "菜单treeGrid树查询")
    @Log(module = LogModule.MENU, type = LogType.SELECT)
    @GetMapping("/queryTreeGrid")
    @ResponseBody
    public Result queryTreeGrid(Menu menu) {
        return Result.success().setData(menuService.list(new QueryWrapper<>(menu)));
    }

    @ApiOperation(value = "授权AuthorizationTreeGrid树查询")
    @Log(module = LogModule.MENU, type = LogType.SELECT)
    @GetMapping("/queryAuthorizationTreeGrid")
    @ResponseBody
    public Result queryAuthorizationTreeGrid(Integer roleId) {
        return Result.success().setData(menuService.selectAuthorizationTreeGrid(roleId));
    }

    @RolesAllowed("system_menu_switchStatus")
    @ApiOperation(value = "菜单启用/停用转换")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单ID"),
            @ApiImplicitParam(name = "status", value = "菜单状态")
    })
    @Log(module = LogModule.MENU, type = LogType.UPDATE)
    @PostMapping("/switchStatus")
    @ResponseBody
    public Result switchStatus(Integer id, String status){
        return Result.resultByBoolean(menuService.switchStatus(id, status, ServletUtils.getLoginUser()));
    }





}
