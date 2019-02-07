package com.mwj.cms.framework.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Meng Wei Jin
 * @description
 **/
public class SecurityUtils {

    /**
     * 管理员账号
     */
    public static final String ADMIN = "admin";

    /**
     * 获取当前用户身份认证
     * @return
     */
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前用户
     * @return
     */
    public static UserDetails getPrincipal(){
        return (UserDetails) getAuthentication().getPrincipal();
    }

    /**
     * 获取当前用户登录名
     * @return
     */
    public static String getUsername(){
        return getPrincipal().getUsername();
    }
}
