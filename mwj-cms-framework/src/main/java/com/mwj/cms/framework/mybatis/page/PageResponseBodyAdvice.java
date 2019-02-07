package com.mwj.cms.framework.mybatis.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Meng Wei Jin
 * @description
 **/
@ControllerAdvice
public class PageResponseBodyAdvice implements ResponseBodyAdvice<IPage> {

    /**
     * 判断是否支持要转换的参数类型
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Class parameterType = methodParameter.getParameterType();
        if(parameterType.equals(IPage.class) || parameterType.equals(Page.class)){
            return true;
        }
        return false;
    }

    /**
     * 当支持后进行相应的转换
     * @param page
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public IPage beforeBodyWrite(IPage page, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        MyPage myPage = new MyPage<>();
        // layui page response parameter
        myPage.setPage(page.getCurrent())
              .setLimit(page.getSize())
              .setCount(page.getTotal())
              .setData(page.getRecords());

        // bootstrap-table page response parameter
        myPage.setTotal(page.getTotal());
        myPage.setRows(page.getRecords());

        // mybatis plus-default page response parameter
        myPage.setCurrent(page.getCurrent());
        myPage.setSize(page.getSize());
        myPage.setTotal(page.getTotal());
        myPage.setRecords(page.getRecords());

        return myPage;
    }
}
