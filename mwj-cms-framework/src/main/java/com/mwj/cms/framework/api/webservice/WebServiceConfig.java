package com.mwj.cms.framework.api.webservice;

import com.mwj.cms.framework.api.webservice.service.WebServicesImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @author mengweijin
 */
@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;

    /**
     * http://localhost:80/services
     * @return
     */
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new WebServicesImpl());
        endpoint.publish("/cxf");
        return endpoint;
    }
}
