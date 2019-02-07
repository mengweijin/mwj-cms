package com.mwj.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mwj.cms.system.entity.RoleMenuRlt;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色和菜单权限关系表 Mapper 接口
 * </p>
 *
 * @author Meng Wei Jin
 */
@Mapper
public interface RoleMenuRLTMapper extends BaseMapper<RoleMenuRlt> {

    /**
     * 根据roleId删除
     * @param roleId
     * @return
     */
    int deleteByRoleId(Integer roleId);
}
