package com.mwj.cms.framework.web.service;

import com.alibaba.fastjson.JSONObject;
import com.mwj.cms.common.constant.Const;
import com.mwj.cms.system.service.InterfaceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Service
public class WeatherService {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private InterfaceService interfaceService;

    /**
     * 根据城市名称获取天气
     * @param cityName 如：西安
     * @return 返回：12月5日 小雨 10℃/13℃
     */
    public String getWeatherByCityName(String cityName) throws Exception {
        String result = Const.EMPTY;

        LinkedHashMap<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("theCityName", cityName);

        String xml = interfaceService.execute("weather", JSONObject.toJSONString(paramMap));
        logger.debug(xml);

        Elements elements = Jsoup.parse(xml).getElementsByTag("getWeatherbyCityNameResult");
        if(CollectionUtils.isNotEmpty(elements)){
            Element element = elements.get(0);

            Elements stringElements = element.getElementsByTag("string");
            if(CollectionUtils.isNotEmpty(stringElements) && stringElements.size() > 6){
                if(stringElements.get(0).text().contains("查询结果为空")){
                    return result;
                }
                result = stringElements.get(6).text() + Const.SPACE + stringElements.get(5).text();
                return result;
            }
        }
        return result;
    }

}
