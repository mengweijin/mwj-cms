package com.mwj.cms.framework.api.webservice;

import cn.hutool.http.webservice.SoapRequest;
import org.apache.commons.collections.MapUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CXF 客户端动态调用服务端
 * @author mengweijin
 */
public class WebServiceClient {

    /**
     * CXF 客户端动态调用WebService
     * @param param 方法入参，建议采用String、xml或者json格式的字符串形式
     * @param url  WebService的WSDL地址，如：http://localhost:8080/services/cxf/myService?wsdl
     * @param method 要调用的服务端的方法名称
     * @return 服务端的方法返回值。建议采用String、xml或者json格式的字符串形式
     */
    public static String send(String url, String method, Object param) throws Exception {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(url);
        Object[] args = new Object[] {param};
        return (String)client.invoke(method, args)[0];
    }

    /**
     * HTTP SOAP方式调用WebService
     * @param url WebService的地址(不加"?wsdl"后缀)，如：http://www.webxml.com.cn/WebServices/WeatherWebService.asmx
     * @param targetNamespace 命名空间，可通过访问wsdl地址后的第一个标签中的targetNamespace属性的值获取到
     * @param methodName 要调用的服务端的方法名称，通过wsdl文档获取
     * @param paramMap 参数集合，其中key为wsdl文档中方法参数的名称，value是需要传递的值
     * @return 返回一个xml字符串，需要自己解析
     */
    public static String sendByHttp(String url, String targetNamespace, String methodName, LinkedHashMap<String, String> paramMap){
        SoapRequest request = new SoapRequest(url, targetNamespace);
        // 请求方法
        request.setMethod(methodName);
        // 请求参数
        if(MapUtils.isNotEmpty(paramMap)){
            for (Map.Entry<String, String> entry: paramMap.entrySet()){
                request.addParam(entry.getKey(), entry.getValue());
            }
        }

        return request.execute();
    }
}
