package com.mwj.cms.framework.api.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author Meng Wei Jin
 * @description 示例Web Service
 * targetNamespace: 命名空间,一般是接口的包名倒序
 **/
@WebService
public interface WebServices {

    @WebMethod
    String sayHello(@WebParam(name = "name") String name);
}
