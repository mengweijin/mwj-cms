package com.mwj.cms.framework.api.webservice.service;

import org.springframework.stereotype.Component;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Component
public class WebServicesImpl implements WebServices {

    @Override
    public String sayHello(String name) {
        return "Hello! " + name;
    }
}
