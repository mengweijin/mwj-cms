package com.mwj.cms.framework.api.webservice;

import org.junit.Test;

/**
 * @author Meng Wei Jin
 * @description
 **/
public class WebServiceClientTest {

    @Test
    public void test() {
        String url = "http://localhost:80/services/cxf?wsdl";
        Object resStr = null;
        try {
            resStr = WebServiceClient.send(url, "sayHello", "张三");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(resStr);
    }
}
