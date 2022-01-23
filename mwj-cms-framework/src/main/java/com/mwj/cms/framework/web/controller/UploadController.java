package com.mwj.cms.framework.web.controller;

import cn.hutool.core.util.IdUtil;
import com.mwj.cms.common.constant.Const;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.common.util.IdUtils;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.SysFile;
import com.mwj.cms.system.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Meng Wei Jin
 **/
@Validated
@Api(description = "文件上传接口")
@Controller
public class UploadController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileService fileService;

    /**
     * 上传文件存放路径
     */
    public static final String UPLOAD_PATH = Const.PROJECT_PATH + File.separator + "upload" + File.separator;

    /**
     * 上传默认模块路径
     */
    public static final String DEFAULT_MODULE = "files";

    /**
     * ^\w*$ 字母数字下划线
     * @param request
     * @param module
     * @return
     */
    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "module", value = "模块名称，以此决定上传到哪个目录下")
    })
    @Log(type = LogType.UPLOAD)
    @PostMapping("upload")
    @ResponseBody
    public Result<String> upload(HttpServletRequest request,
                                 @Pattern(regexp = "^\\w*$")
                                 @RequestParam(value = "module", defaultValue = DEFAULT_MODULE) String module) {
        try {
            ServletContext servletContext = request.getSession().getServletContext();
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
            // 如果是文件上传
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                // 获取所有上传的文件名
                Iterator<String> iterator = multiRequest.getFileNames();
                // 上传路径
                String savePath = UPLOAD_PATH + module;

                MultipartFile file;
                // 原文件名
                String originalName;
                // 存储的真实文件名
                String storageName;
                // 存储文件的绝对全路径
                String filePath;
                // 组id
                String groupId = IdUtil.fastSimpleUUID();

                List<SysFile> sysFileList = new ArrayList<>();

                SysFile sysFile;

                while (iterator.hasNext()) {
                    file = multiRequest.getFile(iterator.next());
                    originalName = file.getOriginalFilename();
                    Path outDirPath = Paths.get(savePath);
                    if (!Files.exists(outDirPath)) {
                        Files.createDirectories(outDirPath);
                    }
                    storageName = IdUtils.getSnowflakeId() + originalName.substring(originalName.lastIndexOf(Const.DOT));
                    filePath = savePath + File.separator + storageName;
                    Files.copy(file.getInputStream(),
                            Files.createFile(Paths.get(filePath)),
                            StandardCopyOption.REPLACE_EXISTING);

                    // 数据库保存
                    sysFile = new SysFile();
                    sysFile.setGroupId(groupId);
                    sysFile.setOriginalName(originalName);
                    sysFile.setStorageName(storageName);
                    sysFile.setFilePath(filePath);
                    sysFile.setCreateBy(ServletUtils.getLoginUser().getId());
                    sysFileList.add(sysFile);
                }
                // 保存到数据库
                fileService.saveBatch(sysFileList);
                return new Result<>(groupId);
            } else {
                logger.error("Can't found upload file!");
            }
        } catch (IOException e) {
            logger.error("IOException:", e);
        }
        return Result.error();
    }


}
