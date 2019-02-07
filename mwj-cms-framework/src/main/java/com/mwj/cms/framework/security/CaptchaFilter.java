package com.mwj.cms.framework.security;

import com.google.code.kaptcha.Constants;
import com.mwj.cms.common.exception.KaptchaException;
import com.mwj.cms.framework.util.MessageSourceUtils;
import com.mwj.cms.framework.util.ServletUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 验证码过滤器
 * @author Meng Wei Jin
 * @description OncePerRequestFilter: 保证过滤器每次请求只会被调用一次
 **/
public class CaptchaFilter extends OncePerRequestFilter {

    /**
     * 验证码在request中的参数名称
     */
    private static final String CAPTCHA_INPUT_NAME = "captcha";

    /**
     * 要过滤的请求方式
     */
    private static final String REQUEST_METHOD_POST = "POST";

    /**
     * 要过滤的URL
     */
    private static final String FILTER_URL = "/login";

    /**
     * 验证不通过时请求的的URL，必须设置为：/login?error，否则登录页面错误信息展示不正确，原因不明
     */
    private static final String FILTER_FAILURE_URL = "/login?error";

    private AuthenticationFailureHandler failureHandler;

    public CaptchaFilter(){
        SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler();
        handler.setDefaultFailureUrl(FILTER_FAILURE_URL);
        this.failureHandler = handler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(FILTER_URL.equals(request.getRequestURI())
                && REQUEST_METHOD_POST.equalsIgnoreCase(request.getMethod())) {
            try {
                validate(request, 120L);
            } catch (KaptchaException e) {
                // 如果校验不通过，调用SpringSecurity的校验失败处理器
                failureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     *
     * @param request
     * @param second 过期时间（单位：秒）
     * @return true 验证通过
     */
    private boolean validate(HttpServletRequest request, long second) {
        if(!ServletUtils.containsParameter(request, CAPTCHA_INPUT_NAME)){
            return true;
        }

        // 登录表单提交过来的验证码
        String code = request.getParameter(CAPTCHA_INPUT_NAME);
        HttpSession session = request.getSession(false);
        String sessionCode;
        if (session != null && (sessionCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY)) != null) {
            if (sessionCode.equalsIgnoreCase(code)) {
                long sessionTime = (long) session.getAttribute(Constants.KAPTCHA_SESSION_DATE);
                long duration = (System.currentTimeMillis() - sessionTime) / 1000;
                if (duration < second) {
                    session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
                    session.removeAttribute(Constants.KAPTCHA_SESSION_DATE);
                    return true;
                } else {
                    throw new KaptchaException(MessageSourceUtils.message("captcha.expire"));
                }
            } else {
                throw new KaptchaException(MessageSourceUtils.message("captcha.error"));
            }
        } else {
            throw new KaptchaException(MessageSourceUtils.message("captcha.not.found"));
        }
    }

}
