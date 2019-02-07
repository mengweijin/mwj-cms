package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.system.entity.RoleMenuRlt;
import com.mwj.cms.system.mapper.RoleMenuRLTMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色和菜单权限关系表 服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 */
@Service
public class RoleMenuRLTService extends ServiceImpl<RoleMenuRLTMapper, RoleMenuRlt> {

    @Autowired
    private RoleMenuRLTMapper roleMenuRLTMapper;

    /**
     * 根据角色查菜单
     * @param roleId
     * @return 当前角色所拥有的所有菜单的id集合
     */
    public List<Integer> selectMenuByRoleId(Integer roleId){
        List<RoleMenuRlt> roleMenuRltList = this.list(new QueryWrapper<>(new RoleMenuRlt().setRoleId(roleId)));
        return roleMenuRltList.stream().map(RoleMenuRlt::getMenuId).collect(Collectors.toList());
    }

    /**
     * 根据roleId删除
     * @param roleId
     * @return
     */
    int deleteByRoleId(Integer roleId){
        return roleMenuRLTMapper.deleteByRoleId(roleId);
    }
}
