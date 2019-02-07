package com.mwj.cms.framework.security;

import com.mwj.cms.common.enums.LoginType;
import com.mwj.cms.common.enums.ResultStatus;
import com.mwj.cms.framework.async.AsyncFactory;
import com.mwj.cms.framework.cache.CacheConfig;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.system.entity.LoginLog;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.service.UserService;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录成功监听
 *
 * @author Meng Wei Jin
 * @description
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private AsyncFactory asyncFactory;

    @Autowired
    private PersistentCacheManager persistentCacheManager;

    /**
     * 登录失败缓存记录
     */
    private Cache<String, AtomicInteger> loginFailedCache;

    @PostConstruct
    public void init() {
        loginFailedCache = persistentCacheManager.getCache(CacheConfig.LOGIN_FAILED_CACHE_ALIAS, String.class, AtomicInteger.class);
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails)event.getAuthentication().getDetails();

        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // 从缓存中移除当前用户的登录失败次数记录
        loginFailedCache.remove(username);

        User user = userService.getByLoginName(username);

        // 保存用户信息到session
        ServletUtils.getSession().setAttribute(ServletUtils.SESSION_USER, user);

        //开启异步任务，写入登录日志
        LoginLog loginLog = new LoginLog()
                .setLoginName(username)
                .setStatus(ResultStatus.SUCCESS)
                .setLoginType(LoginType.LOGIN)
                .setIp(auth.getRemoteAddress());
        asyncFactory.addLoginLog(loginLog, ServletUtils.getRequest());
    }
}