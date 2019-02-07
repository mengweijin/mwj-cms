package com.mwj.cms.framework.api.crossorigin;

import com.mwj.cms.framework.web.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Meng Wei Jin
 * @description 允许被跨域调用，如果有Spring Security、shiro 等安全框架需要放行
 *             springMVC的版本要在4.2或以上版本才支持@CrossOrigin，并且必须指定Get/Post请求方式才能生效。
 *                  其中@CrossOrigin中的2个参数：
 *                  origins: 允许可访问的域列表(配置IP或者域名，多个以逗号分割)
 *                  maxAge: 准备响应前的缓存持续的最大时间（以秒为单位）。
 **/
@Api(description = "跨域接口")
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CrossOriginController {

    @ApiOperation(value = "跨域接口测试")
    @PostMapping("/test")
    @CrossOrigin(origins = {"http://127.0.0.1:80"})
    public Result<String> testCrossOrigin(){
        return Result.success();
    }
}
