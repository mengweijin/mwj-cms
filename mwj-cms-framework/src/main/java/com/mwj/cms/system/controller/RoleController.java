package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.Role;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

/**
 * @author Meng Wei Jin
 */
@Api(description = "角色管理接口")
@Controller
@RequestMapping("/sys/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @RolesAllowed("system_role_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/role/roleIndex";
    }

    @ApiOperation(value = "角色新增")
    @GetMapping("/add")
    public String add(){
        setAttribute("role", new Role());
        return "system/role/roleAdd";
    }

    @ApiOperation(value = "角色编辑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID")
    })
    @Log(module = LogModule.ROLE, type = LogType.UPDATE)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id){
        setAttribute("role", roleService.getById(id));
        return "system/role/roleAdd";
    }

    @RolesAllowed("system_role_insert")
    @ApiOperation(value = "角色新增")
    @Log(module = LogModule.ROLE, type = LogType.INSERT)
    @PostMapping("/insert")
    @ResponseBody
    public Result insert(Role role) {
        User loginUser = ServletUtils.getLoginUser();
        role.setCreateBy(loginUser.getId())
            .setUpdateBy(loginUser.getId())
            .setStatus(Status.NORMAL);
        return Result.resultByBoolean(roleService.save(role));
    }

    @RolesAllowed("system_role_update")
    @ApiOperation(value = "角色编辑")
    @Log(module = LogModule.ROLE, type = LogType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public Result update(Role role){
        role.setUpdateBy(ServletUtils.getLoginUser().getId());
        return Result.resultByBoolean(roleService.updateById(role));
    }

    @RolesAllowed("system_role_delete")
    @ApiOperation(value = "角色删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID")
    })
    @Log(module = LogModule.ROLE, type = LogType.DELETE)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Integer id){
        return Result.resultByBoolean(roleService.deleteById(id));
    }

    @ApiOperation(value = "角色分页查询")
    @Log(module = LogModule.ROLE, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<Map<String, Object>> queryPage(IPage<Map<String, Object>> page, Role role){
        return roleService.selectPageVO(page, role);
    }

    @RolesAllowed("system_role_switchStatus")
    @ApiOperation(value = "角色启用/停用转换")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID"),
            @ApiImplicitParam(name = "status", value = "状态")
    })
    @Log(module = LogModule.ROLE, type = LogType.UPDATE)
    @PostMapping("/switchStatus")
    @ResponseBody
    public Result switchStatus(Integer id, String status){
        return Result.resultByBoolean(roleService.switchStatus(id, status, ServletUtils.getLoginUser()));
    }

    @RolesAllowed("system_role_authorization")
    @ApiOperation(value = "角色授权")
    @Log(module = LogModule.ROLE, type = LogType.UPDATE)
    @PostMapping("/authorization")
    @ResponseBody
    public Result authorization(Integer roleId, @RequestParam(value = "menuIds[]") Integer[] menuIds){
        return Result.resultByBoolean(roleService.authorization(roleId, menuIds));
    }


}
