package com.mwj.cms.framework.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mwj.cms.common.enums.ConfigEnum;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.common.exception.UserException;
import com.mwj.cms.framework.cache.CacheConfig;
import com.mwj.cms.framework.util.MessageSourceUtils;
import com.mwj.cms.system.entity.Menu;
import com.mwj.cms.system.entity.Role;
import com.mwj.cms.system.entity.RoleMenuRlt;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.mapper.MenuMapper;
import com.mwj.cms.system.mapper.RoleMenuRLTMapper;
import com.mwj.cms.system.mapper.UserRoleRLTMapper;
import com.mwj.cms.system.service.ConfigService;
import com.mwj.cms.system.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Service
public class DefaultUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleRLTMapper userRoleRLTMapper;

    @Autowired
    private RoleMenuRLTMapper roleMenuRLTMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private PersistentCacheManager persistentCacheManager;

    @Autowired
    private AuthenticationFailureListener authenticationFailureListener;

    /**
     * 默认最大登录失败次数
     */
    private static final int DEFAULT_MAX_LOGIN_FAIL_TIMES = 5;

    /**
     * 登录失败缓存记录
     */
    private Cache<String, AtomicInteger> loginFailedCache;

    @PostConstruct
    public void init() {
        loginFailedCache = persistentCacheManager.getCache(CacheConfig.LOGIN_FAILED_CACHE_ALIAS, String.class, AtomicInteger.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 缓存用户登录失败次数
        AtomicInteger retryCount = loginFailedCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
        }

        // 账号已锁定
        if(retryCount.get() + 1 > DEFAULT_MAX_LOGIN_FAIL_TIMES){
            authenticationFailureListener.recordLoginFailedLog(username);
            throw new LockedException(MessageSourceUtils.message("user.locked"));
        }

        User user = userService.getByLoginName(username);
        if (user == null){
            authenticationFailureListener.recordLoginFailedLog(username);
            // spring security 默认不抛出UsernameNotFoundException的前台展示信息, 会转成BadCredentialsException，所以前台的错误信息提示总是会为：Bad credentials，所以这里使用自定义的异常类
            throw new UserException(MessageSourceUtils.message("user.not.found"));
        } else if(Status.DISABLE == user.getStatus()){
            authenticationFailureListener.recordLoginFailedLog(username);
            // 用户不可用
            throw new UserException(MessageSourceUtils.message("user.disable"));
        }

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = createAuthorities(user.getId());
        return new org.springframework.security.core.userdetails.User(user.getLoginName(), user.getPassword(), simpleGrantedAuthorities);
    }

    /**
     * 角色菜单权限字符串转化
     *
     * security 判断权限时是以ROLE_开头的权限标识，如 "ROLE_USER,ROLE_ADMIN"
     * SimpleGrantedAuthority("ROLE_system_user_add")用户新增的权限
     *
     * @param userId 用户Id
     */
    private List<SimpleGrantedAuthority> createAuthorities(Integer userId){
        List<Menu> menuList = Collections.EMPTY_LIST;

        User user = userService.getById(userId);
        if(SecurityUtils.ADMIN.equals(user.getLoginName())){
            // 如果是admin，拥有所有菜单的所有权限
            menuList = menuMapper.selectList(new QueryWrapper<>(new Menu()));
        } else {
            // 查询用户所拥有的角色
            List<Role> roleList = userRoleRLTMapper.getRolesByUserId(userId);

            // 当前用户所属角色拥有的所有菜单
            List<RoleMenuRlt> roleMenuRltList = new ArrayList<>();
            for (Role role: roleList){
                List<RoleMenuRlt> tempList = roleMenuRLTMapper.selectList(new QueryWrapper<>(new RoleMenuRlt().setRoleId(role.getId())));
                roleMenuRltList.addAll(tempList);
            }

            // 获取去重后的菜单id
            Set<Integer> menuIdSet = roleMenuRltList.stream().map(RoleMenuRlt::getMenuId).collect(Collectors.toSet());

            // 查询到菜单集合，菜单中包含权限标识
            if(CollectionUtils.isNotEmpty(menuIdSet)){
                menuList = menuMapper.selectBatchIds(new ArrayList<>(menuIdSet));
            }
        }

        // 添加权限
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>(menuList.size());
        for (Menu menu: menuList) {
            // 如果是管理员，则添加所有菜单权限
            if(SecurityUtils.ADMIN.equals(user.getLoginName())){
                simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + menu.getPermission()));
            } else {
                // 如果不是管理员，只有菜单状态为启用状态，才添加权限
                if(Status.NORMAL == menu.getStatus()){
                    simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + menu.getPermission()));
                }
            }

        }
        return simpleGrantedAuthorities;
    }
}
