package com.mwj.cms.framework.web.service;

import com.mwj.cms.common.util.http.JsoupUtils;
import com.mwj.cms.system.entity.SysInterface;
import com.mwj.cms.system.service.InterfaceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Service
public class PublicIPService {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private InterfaceService interfaceService;

    /**
     * 获取本机外网IP地址
     * @return
     * @throws Exception
     */
    public String getPublicIp() {

        String ip;

        try {
            SysInterface sysInterface = interfaceService.getByKeyCode("publicIP");
            Document document = JsoupUtils.getDocument(sysInterface.getUrl());
            Element element = document.getElementById("result");
            Elements elements = element.select("p code");
            ip = elements.get(0).text();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 获取内网ip
            ip = getLocalHostIP();
        }
        return ip;
    }

    /**
     * 获取本地内网ip
     * @return
     */
    public String getLocalHostIP(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }
}
