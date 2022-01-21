package com.mwj.cms.framework.web.service;

import com.alibaba.fastjson.JSONObject;
import com.mwj.cms.common.constant.Const;
import com.mwj.cms.system.service.InterfaceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Service
public class TaoBaoIpInfoService {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private InterfaceService interfaceService;

    /**
     * 根据ip地址获取地理位置
     * @param ip
     * @return
     * @throws Exception
     */
    public String getLocationByIp(String ip) throws Exception {
        try{
            return getLocationByIp(ip, false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "Unknown";
    }

    /**
     * 根据ip地址获取地理位置，这个Service只适用于淘宝接口
     * 腾讯接口：http://ip.qq.com/cgi-bin/searchip?searchip1=
     * 新浪接口：http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=
     * 淘宝接口：http://ip.taobao.com/service/getIpInfo.php?ip=
     * ip138接口：http://www.ip138.com/ips1388.asp?action=2&ip=
     * @param ip
     * @param onlyCity 是否只获取城市名称
     * @return
     */
    public String getLocationByIp(String ip, boolean onlyCity) throws Exception {
        if("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)){
            return "localhost";
        }
        String address = "Unknown";

        LinkedHashMap<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("ip", ip);
        String rspStr = interfaceService.execute("ipInfoByTaoBao", JSONObject.toJSONString(paramMap));

        if (StringUtils.isEmpty(rspStr)) {
            logger.error("获取IP位置异常:" + ip);
            return address;
        }
        JSONObject jsonObject = JSONObject.parseObject(rspStr);
        JSONObject data = jsonObject.getObject("data", JSONObject.class);
        String region = data.getString("region");
        String city = data.getString("city");
        if(onlyCity){
            return  city;
        } else {
            address = region + Const.SPACE + city;
            return address;
        }
    }
}
