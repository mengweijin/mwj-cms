package com.mwj.cms.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mwj.cms.common.constant.Const;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.common.exception.MwjCmsException;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.framework.util.MessageSourceUtils;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.Dept;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.service.DeptService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Meng Wei Jin
 */
@Api(description = "部门管理接口")
@Controller
@RequestMapping("/sys/dept")
public class DeptController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DeptService deptService;

    @RolesAllowed("system_dept_index")
    @ApiOperation(value = "部门首页")
    @GetMapping("/index")
    public String index(){
        return "system/dept/deptIndex";
    }

    @ApiOperation(value = "部门新增页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父部门ID")
    })
    @GetMapping("/add")
    public String add(Long parentId){
        Dept parentDept = deptService.getById(parentId);
        setAttribute("parentDept", parentDept);
        setAttribute("dept", new Dept());
        return "system/dept/deptAdd";
    }

    @ApiOperation(value = "部门编辑页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门ID")
    })
    @Log(module = LogModule.DEPT, type = LogType.UPDATE)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id){
        Dept dept = deptService.getById(id);
        Dept parentDept = deptService.getById(dept.getParentId());
        setAttribute("dept", dept);
        setAttribute("parentDept", parentDept);

        return "system/dept/deptAdd";
    }

    @RolesAllowed("system_dept_insert")
    @ApiOperation(value = "部门新增")
    @Log(module = LogModule.DEPT, type = LogType.INSERT)
    @PostMapping("/insert")
    @ResponseBody
    public Result insert(Dept dept){
        User loginUser = ServletUtils.getLoginUser();
        dept.setCreateBy(loginUser.getId()).setUpdateBy(loginUser.getId()).setStatus(Status.NORMAL);
        return Result.resultByBoolean(deptService.save(dept));
    }

    @RolesAllowed("system_dept_update")
    @ApiOperation(value = "部门编辑")
    @Log(module = LogModule.DEPT, type = LogType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public Result update(Dept dept){
        dept.setUpdateBy(ServletUtils.getLoginUser().getId());
        return Result.resultByBoolean(deptService.updateById(dept));
    }

    @RolesAllowed("system_dept_delete")
    @ApiOperation(value = "部门删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门ID")
    })
    @Log(module = LogModule.DEPT, type = LogType.DELETE)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Integer id) {
        boolean bool = deptService.deleteById(id);

        String msg = Const.EMPTY;
        if(!bool){
            msg = MessageSourceUtils.message("dept.delete.error");
        }
        return Result.resultByBoolean(bool, msg);
    }

    @ApiOperation(value = "部门查询")
    @Log(module = LogModule.DEPT, type = LogType.SELECT)
    @PostMapping("/query")
    @ResponseBody
    public List<Dept> query(Dept dept){
        return deptService.list(new QueryWrapper<>(dept));
    }

    @ApiOperation(value = "部门SelectTree树查询")
    @Log(module = LogModule.DEPT, type = LogType.SELECT)
    @PostMapping("/querySelectTree")
    @ResponseBody
    public List<JSONObject> querySelectTree(Dept dept, String selectedId) throws MwjCmsException {
        List<String> selectedIdList = new ArrayList<>(1);
        selectedIdList.add(selectedId);
        return deptService.querySelectTree(dept, selectedIdList);
    }

    @ApiOperation(value = "部门treeGrid树查询")
    @Log(module = LogModule.DEPT, type = LogType.SELECT)
    @GetMapping("/queryTreeGrid")
    @ResponseBody
    public Result queryTreeGrid(Dept dept) {
        return Result.success().setData(deptService.selectVO(dept));
    }

    @RolesAllowed("system_dept_switchStatus")
    @ApiOperation(value = "部门启用/停用转换")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门ID"),
            @ApiImplicitParam(name = "status", value = "部门状态")
    })
    @Log(module = LogModule.DEPT, type = LogType.UPDATE)
    @PostMapping("/switchStatus")
    @ResponseBody
    public Result switchStatus(Integer id, String status){
        boolean bool = false;
        try {
            bool = deptService.switchStatus(id, status, ServletUtils.getLoginUser());
        } catch (MwjCmsException e) {
            logger.error(e.getMessage(), e);
        }
        return Result.resultByBoolean(bool);
    }
}
