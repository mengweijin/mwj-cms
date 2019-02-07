package com.mwj.cms.framework.mybatis.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Meng Wei Jin
 * @description
 **/
public class PageArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 判断是否支持要转换的参数类型
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class parameterType = methodParameter.getParameterType();
        if(parameterType.equals(IPage.class) || parameterType.equals(Page.class)){
            return true;
        }
        return false;
    }

    /**
     * 当支持后进行相应的转换
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        // layui page parameter
        String currentStr = request.getParameter(MyPage.VAR_PAGE);
        String sizeStr = request.getParameter(MyPage.VAR_LIMIT);
        String totalStr = request.getParameter(MyPage.VAR_COUNT);

        // mybatis plus page parameter
        if(StringUtils.isEmpty(currentStr) || StringUtils.isEmpty(sizeStr)) {
            currentStr = request.getParameter(MyPage.VAR_CURRENT);
            sizeStr = request.getParameter(MyPage.VAR_SIZE);
            totalStr = request.getParameter(MyPage.VAR_TOTAL);
        }

        // convert default page parameter
        long page =  NumberUtils.toLong(currentStr, MyPage.DEFAULT_PAGE);
        long limit = NumberUtils.toLong(sizeStr, MyPage.DEFAULT_LIMIT);
        long count = NumberUtils.toLong(totalStr, MyPage.DEFAULT_COUNT);

        Page pageObj = new Page();
        pageObj.setCurrent(page);
        pageObj.setSize(limit);
        pageObj.setTotal(count);

        return pageObj;
    }
}
