package com.mwj.cms.framework.web.controller;

import com.mwj.cms.common.constant.Const;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.service.TaoBaoIpInfoService;
import com.mwj.cms.framework.web.service.WeatherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Api(description = "公共接口")
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private TaoBaoIpInfoService taoBaoIpInfoService;

    @ApiOperation(value = "获取当前请求登录IP所在地的天气信息")
    @PostMapping("/getWeather")
    @ResponseBody
    public String getCurrentCityWeather(){
        try {
            String ip = ServletUtils.getClientIP(ServletUtils.getRequest());
            String cityName = taoBaoIpInfoService.getLocationByIp(ip, true);
            return weatherService.getWeatherByCityName(cityName);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return Const.EMPTY;
    }
}
