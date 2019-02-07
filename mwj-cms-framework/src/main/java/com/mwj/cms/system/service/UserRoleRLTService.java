package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.system.entity.UserRoleRlt;
import com.mwj.cms.system.mapper.UserRoleRLTMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 */
@Service
public class UserRoleRLTService extends ServiceImpl<UserRoleRLTMapper, UserRoleRlt> {

    @Autowired
    private UserRoleRLTMapper userRoleRLTMapper;

    public int deleteByUserId(Integer userId){
        return userRoleRLTMapper.deleteByUserId(userId);
    }

}
