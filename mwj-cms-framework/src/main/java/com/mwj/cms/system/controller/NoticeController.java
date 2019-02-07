package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.Notice;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.service.NoticeService;
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
@Api(description = "通知公告接口")
@Controller
@RequestMapping("/sys/notice")
public class NoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;

    @RolesAllowed("system_notice_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/notice/noticeIndex";
    }

    @ApiOperation(value = "通知公告新增")
    @GetMapping("/add")
    public String add(){
        setAttribute("notice", new Notice());
        return "system/notice/noticeAdd";
    }

    @ApiOperation(value = "通知公告编辑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID")
    })
    @Log(module = LogModule.NOTICE, type = LogType.UPDATE)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id){
        setAttribute("notice", noticeService.getById(id));
        return "system/notice/noticeAdd";
    }

    @RolesAllowed("system_notice_insert")
    @ApiOperation(value = "通知公告新增")
    @Log(module = LogModule.NOTICE, type = LogType.INSERT)
    @PostMapping("/insert")
    @ResponseBody
    public Result insert(Notice notice) {
        User loginUser = ServletUtils.getLoginUser();
        notice.setCreateBy(loginUser.getId())
                .setUpdateBy(loginUser.getId())
                .setStatus(Status.NORMAL);
        return Result.resultByBoolean(noticeService.save(notice));
    }

    @RolesAllowed("system_notice_update")
    @ApiOperation(value = "通知公告编辑")
    @Log(module = LogModule.NOTICE, type = LogType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public Result update(Notice notice){
        notice.setUpdateBy(ServletUtils.getLoginUser().getId());
        return Result.resultByBoolean(noticeService.updateById(notice));
    }

    @RolesAllowed("system_notice_delete")
    @ApiOperation(value = "通知公告删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID")
    })
    @Log(module = LogModule.NOTICE, type = LogType.DELETE)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Integer id){
        return Result.resultByBoolean(noticeService.removeById(id));
    }

    @ApiOperation(value = "系统日志分页查询")
    @Log(module = LogModule.NOTICE, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<Map<String, Object>> queryPage(IPage<Map<String, Object>> page, Notice notice){
        return noticeService.selectPageVO(page, notice);
    }


    @RolesAllowed("system_notice_switchStatus")
    @ApiOperation(value = "通知公告启用/停用转换")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID"),
            @ApiImplicitParam(name = "status", value = "状态")
    })
    @Log(module = LogModule.NOTICE, type = LogType.UPDATE)
    @PostMapping("/switchStatus")
    @ResponseBody
    public Result switchStatus(Integer id, String status){
        return Result.resultByBoolean(noticeService.switchStatus(id, status, ServletUtils.getLoginUser()));
    }

}
