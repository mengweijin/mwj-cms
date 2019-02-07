package com.mwj.cms.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.log.LogModule;
import com.mwj.cms.framework.web.controller.BaseController;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.SysFile;
import com.mwj.cms.system.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Meng Wei Jin
 */
@Api(description = "文件管理接口")
@Controller
@RequestMapping("/sys/file")
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;

    @RolesAllowed("system_file_index")
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(){
        return "system/file/fileIndex";
    }

    @ApiOperation(value = "文件查询")
    @Log(module = LogModule.FILE, type = LogType.SELECT)
    @PostMapping("/query")
    @ResponseBody
    public List<SysFile> query(SysFile sysFile){
        return fileService.list(new QueryWrapper<>(sysFile));
    }

    @ApiOperation(value = "文件分页查询")
    @Log(module = LogModule.FILE, type = LogType.SELECT)
    @PostMapping("/queryPage")
    @ResponseBody
    public IPage<Map<String, Object>> queryPage(IPage<Map<String, Object>> page, SysFile sysFile){
        return fileService.selectPageVO(page, sysFile);
    }

    @RolesAllowed("system_file_delete")
    @ApiOperation(value = "文件删除")
    @Log(module = LogModule.FILE, type = LogType.SELECT)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Integer id) throws IOException {
        return Result.resultByBoolean(fileService.deleteFileById(id));
    }
}
