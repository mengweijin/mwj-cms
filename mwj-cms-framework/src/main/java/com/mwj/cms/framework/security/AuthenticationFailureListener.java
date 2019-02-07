package com.mwj.cms.framework.security;

import com.mwj.cms.common.enums.LoginType;
import com.mwj.cms.common.enums.ResultStatus;
import com.mwj.cms.framework.async.AsyncFactory;
import com.mwj.cms.framework.cache.CacheConfig;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.system.entity.LoginLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Meng Wei Jin
 * @description 登录失败监听
 **/
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final Log logger = LogFactory.getLog(this.getClass());

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
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        WebAuthenticationDetails auth = (WebAuthenticationDetails) event.getAuthentication().getDetails();

        String username = event.getAuthentication().getPrincipal().toString();

        // 加入登录失败用户次数到缓存
        AtomicInteger retryCount = loginFailedCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
        }
        // 自增1
        retryCount.incrementAndGet();
        loginFailedCache.put(username, retryCount);

        // 开启异步任务，写入失败登录日志
        LoginLog loginLog = new LoginLog()
                .setLoginName(username)
                .setStatus(ResultStatus.FAIL)
                .setLoginType(LoginType.LOGIN)
                .setIp(auth.getRemoteAddress());
        asyncFactory.addLoginLog(loginLog, ServletUtils.getRequest());
    }

    /**
     * 记录登录失败的日志
     * @param loginName
     */
    public void recordLoginFailedLog(String loginName) {
        // 加入登录失败用户次数到缓存
        AtomicInteger retryCount = loginFailedCache.get(loginName);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
        }
        // 自增1
        retryCount.incrementAndGet();
        loginFailedCache.put(loginName, retryCount);

        // 开启异步任务，写入失败登录日志
        LoginLog loginLog = new LoginLog()
                .setLoginName(loginName)
                .setStatus(ResultStatus.FAIL)
                .setLoginType(LoginType.LOGIN)
                .setIp(ServletUtils.getClientIP(ServletUtils.getRequest()));
        asyncFactory.addLoginLog(loginLog, ServletUtils.getRequest());
    }
}
