package com.mwj.cms.framework.api.crossorigin;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Meng Wei Jin
 * @description 跨域调用 REST Services 接口
 **/
@Service
public class CrossOriginService {

    private final RestTemplate restTemplate;

    public CrossOriginService(RestTemplateBuilder restTemplateBuilder) {
        // 可以添加其他请求配置
        //restTemplateBuilder.basicAuthorization("admin", "123456");
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * 测试 跨域调用 Restful 接口  向百度发送一条get请求，映射返回对象为Object, 更多方法参考API
     * @param name 请求参数
     */
    public Object test(String name){
        return restTemplate.getForObject("www.baidu.com", Object.class, name);
    }
}
