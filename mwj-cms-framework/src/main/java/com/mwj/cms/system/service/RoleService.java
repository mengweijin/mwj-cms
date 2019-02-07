package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.system.entity.Role;
import com.mwj.cms.system.entity.RoleMenuRlt;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuRLTService roleMenuRLTService;

    /**
     * 获取已启用的角色集合
     * @return
     */
    public List<Role> getNormalRoleList(){
        return this.list(new QueryWrapper<>(new Role().setStatus(Status.NORMAL)));
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public Boolean deleteById(Integer id) {
        return this.removeById(id);
    }

    /**
     * 自定义分页查询
     * @param page
     * @param role
     * @return
     */
    public IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, Role role){
        return roleMapper.selectPageVO(page, role);
    }

    /**
     * 角色启用/停用转换
     * @param id
     * @param status
     * @param loginUser
     * @return
     */
    public Boolean switchStatus(Integer id, String status, User loginUser) {
        return this.updateById(new Role().setId(id).setStatus(Status.get(status)).setUpdateBy(loginUser.getId()));
    }

    /**
     * 角色授权
     * @param roleId
     * @param menuIds
     * @return
     */
    public boolean authorization(Integer roleId, Integer[] menuIds) {

        roleMenuRLTService.deleteByRoleId(roleId);

        List<RoleMenuRlt> roleMenuRltList = new ArrayList<>(menuIds.length);
        for (Integer menuId: menuIds){
            roleMenuRltList.add(new RoleMenuRlt().setRoleId(roleId).setMenuId(menuId));
        }
        return roleMenuRLTService.saveBatch(roleMenuRltList);
    }
}
