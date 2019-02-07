package com.mwj.cms.framework.web.controller;

import com.mwj.cms.common.constant.Const;
import com.mwj.cms.framework.log.Log;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.system.entity.SysFile;
import com.mwj.cms.system.service.FileService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @author Meng Wei Jin
 **/
@Api(description = "文件下载接口")
@SuppressFBWarnings("DB_DUPLICATE_BRANCHES")
@Controller
public class DownloadController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件ID")
    })
    @Log(type = LogType.DOWNLOAD)
    @PostMapping("download")
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public void download(String fileId, HttpServletResponse response, HttpServletRequest request) throws IOException {
        SysFile sysFile = fileService.getById(fileId);
        if (sysFile != null) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + setDownloadHeader(request, sysFile.getOriginalName()));

            File file = FileUtils.getFile(sysFile.getFilePath());
            FileUtils.copyFile(file, response.getOutputStream());
        } else {
            throw new FileNotFoundException();
        }
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public String setDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String encodeFileName = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            encodeFileName = URLEncoder.encode(encodeFileName, "utf-8");
            encodeFileName = encodeFileName.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            encodeFileName = new String(fileName.getBytes(Charset.forName(Const.UTF_8)), "ISO-8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            encodeFileName = URLEncoder.encode(encodeFileName, "utf-8");
        } else {
            // 其它浏览器
            encodeFileName = URLEncoder.encode(encodeFileName, "utf-8");
        }
        return encodeFileName;

    }
}
