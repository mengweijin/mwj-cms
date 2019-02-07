package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.common.enums.ConfigEnum;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.common.enums.Sex;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.framework.util.MessageSourceUtils;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.entity.UserRoleRlt;
import com.mwj.cms.system.service.ConfigService;
import com.mwj.cms.system.service.UserRoleRLTService;
import com.mwj.cms.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Meng Wei Jin
 */
@Api(description = "用户管理接口")
@Controller
@RequestMapping("/sys/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserRoleRLTService userRoleRLTService;

    @RolesAllowed("system_user_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/user/userIndex";
    }

    @ApiOperation(value = "用户新增页面")
    @GetMapping("/add")
    public String add(){
        setAttribute("user", new User().setSex(Sex.MALE));
        setAttribute("roleIdList", Collections.EMPTY_LIST);
        return "system/user/userAdd";
    }

    @ApiOperation(value = "用户编辑页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID")
    })
    @Log(module = LogModule.USER, type = LogType.UPDATE)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id) {
        setAttribute("user", userService.getById(id));
        List<UserRoleRlt> userRoleRltList = userRoleRLTService.list(new QueryWrapper<>(new UserRoleRlt().setUserId(id)));
        setAttribute("roleIdList", userRoleRltList.stream().map(UserRoleRlt::getRoleId).collect(Collectors.toList()));
        return "system/user/userAdd";
    }

    @RolesAllowed("system_user_insert")
    @ApiOperation(value = "用户新增")
    @Log(module = LogModule.USER, type = LogType.INSERT)
    @PostMapping("/insert")
    @ResponseBody
    public Result insert(@Validated User user, BindingResult bindingResult, String roleIds) {
        // 数据后台验证
        if(bindingResult.hasErrors()) {
            return validateErrorResult(bindingResult);
        }
        User checkUser = userService.getByLoginName(user.getLoginName());
        if(checkUser != null){
            return Result.error(MessageSourceUtils.message("user.name.already.exists"));
        }

        User loginUser = ServletUtils.getLoginUser();
        user
            .setPassword(configService.getValueByEnum(ConfigEnum.DEFAULT_PASSWORD))
            .setCreateBy(loginUser.getId())
            .setUpdateBy(loginUser.getId())
            .setStatus(Status.NORMAL);
        userService.save(user);

        // 增加用户角色关系
        userService.addUserRoleRltByRoleIds(roleIds, user);

        return Result.success();
    }

    @RolesAllowed("system_user_update")
    @ApiOperation(value = "用户编辑")
    @Log(module = LogModule.USER, type = LogType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public Result update(@Validated User user, BindingResult bindingResult, String roleIds){
        // 数据后台验证
        if(bindingResult.hasErrors()) {
            return validateErrorResult(bindingResult);
        }
        user.setUpdateBy(ServletUtils.getLoginUser().getId());
        userService.updateById(user);

        // 清理用户角色关系
        userRoleRLTService.deleteByUserId(user.getId());
        // 更新用户角色关系
        userService.addUserRoleRltByRoleIds(roleIds, user);

        return Result.success();
    }

    @RolesAllowed("system_user_resetPassword")
    @ApiOperation(value = "重置密码")
    @Log(module = LogModule.USER, type = LogType.UPDATE)
    @PostMapping("/resetPassword/{id}")
    @ResponseBody
    public Result resetPassword(@PathVariable("id") Integer id){
        return Result.resultByBoolean(userService.resetPassword(id, ServletUtils.getLoginUser()));
    }

    @ApiOperation(value = "修改密码")
    @GetMapping("/updatePassword")
    public String updatePassword(){
        return "system/user/updatePassword";
    }

    @RolesAllowed("system_user_updatePassword")
    @ApiOperation(value = "修改密码")
    @Log(module = LogModule.USER, type = LogType.UPDATE)
    @PostMapping("/updatePassword")
    @ResponseBody
    public Result updatePassword(String oldPassword, String newPassword){
        return userService.updatePassword(ServletUtils.getLoginUser(), oldPassword, newPassword);
    }

    @ApiOperation(value = "检查登录名")
    @Log(module = LogModule.USER, type = LogType.SELECT)
    @PostMapping("/checkLoginName")
    @ResponseBody
    public Result checkLoginName(String loginName){
        User user = userService.getByLoginName(loginName);
        if(user != null){
            return Result.error(MessageSourceUtils.message("user.name.already.exists"));
        }
        return Result.success();
    }

    @ApiOperation(value = "用户查询")
    @Log(module = LogModule.USER, type = LogType.SELECT)
    @PostMapping("/query")
    @ResponseBody
    public List<User> query(User user){
        return userService.list(new QueryWrapper<>(user));
    }

    @ApiOperation(value = "用户分页查询")
    @Log(module = LogModule.USER, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<Map<String, Object>> queryPage(IPage<Map<String, Object>> page, User user){
        return userService.selectPageVO(page, user);
    }

    @RolesAllowed("system_user_switchStatus")
    @ApiOperation(value = "用户启用/停用转换")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID"),
            @ApiImplicitParam(name = "status", value = "状态")
    })
    @Log(module = LogModule.USER, type = LogType.UPDATE)
    @PostMapping("/switchStatus")
    @ResponseBody
    public Result switchStatus(Integer id, String status){
        return Result.resultByBoolean(userService.switchStatus(id, status, ServletUtils.getLoginUser()));
    }

}
