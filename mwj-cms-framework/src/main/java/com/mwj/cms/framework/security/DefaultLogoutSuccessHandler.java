package com.mwj.cms.framework.security;

import com.mwj.cms.common.enums.LoginType;
import com.mwj.cms.common.enums.ResultStatus;
import com.mwj.cms.framework.async.AsyncFactory;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.system.entity.LoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Meng Wei Jin
 * @description
 **/
public class DefaultLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private AsyncFactory asyncFactory;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        //开启异步任务，写入登录日志
        LoginLog loginLog = new LoginLog()
                .setLoginName(username)
                .setStatus(ResultStatus.SUCCESS)
                .setLoginType(LoginType.LOGOUT)
                .setIp(ServletUtils.getClientIP(request));
        asyncFactory.addLoginLog(loginLog, request);

        super.onLogoutSuccess(request, response, authentication);
    }
}
