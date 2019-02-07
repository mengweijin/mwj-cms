package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.system.mapper.UserOnlineMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Service
public class UserOnlineService {

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserOnlineMapper userOnlineMapper;

    /**
     * 查询在线用户列表
     * @param page
     * @return
     */
    public IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page) {
        List<String> loginNameList = new ArrayList<>();
        List<Object> principalList = sessionRegistry.getAllPrincipals();
        principalList.forEach(principal -> {
            if(principal instanceof UserDetails){
                loginNameList.add(((UserDetails)principal).getUsername());
            }
        });
        if(CollectionUtils.isNotEmpty(loginNameList)){
            return userOnlineMapper.selectPageVO(page, loginNameList);
        } else {
            return page;
        }
    }

    /**
     * 强制下线用户
     * @param loginName
     */
    public void invalidateSession(String loginName){
        List<Object> principalList = sessionRegistry.getAllPrincipals();
        principalList.forEach(principal -> {
            if(principal instanceof UserDetails){
                if(((UserDetails)principal).getUsername().equals(loginName)){
                    // 获取所有当前用户的session
                    List<SessionInformation> sessionsInfoList = sessionRegistry.getAllSessions(principal, false);
                    if (CollectionUtils.isNotEmpty(sessionsInfoList)) {
                        // 让session失效
                        sessionsInfoList.forEach(sessionInfo -> sessionInfo.expireNow());
                    }
                }
            }
        });
    }
}
