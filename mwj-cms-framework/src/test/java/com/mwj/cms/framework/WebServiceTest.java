package com.mwj.cms.framework;

import cn.hutool.core.lang.Console;
import cn.hutool.http.webservice.SoapRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * @author Meng Wei Jin
 * @description
 **/
public class WebServiceTest {

    @Test
    public void weathTest() {
        SoapRequest request = new SoapRequest(
                "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx",
                "http://WebXml.com.cn/"
        );
        // 命名空间envelope(ns)
        request.setXmlns("soapenv");
        // 请求方法
        request.setMethod("getWeatherbyCityName");
        // 请求参数
        request.addParam("theCityName", "南昌");

        String result = request.execute();
        System.out.println(result);

        Document document = Jsoup.parse(result);
        document.getElementsByAttribute("getWeatherbyCityNameResult");
    }

    @Test
    public void requestTest() {
        SoapRequest request = new SoapRequest(//
                "http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx", //
                "http://WebXml.com.cn/"//
        );
        request.setXmlns("soapenv");
        request.setMethod("getCountryCityByIp");
        request.addParam("theIpAddress", "218.21.240.106");

        Console.log(request.execute());
    }

}
