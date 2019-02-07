package com.mwj.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author Meng Wei Jin
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 自定义分页查询
     * @param page
     * @param role
     * @return
     */
    IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, @Param("role") Role role);
}
