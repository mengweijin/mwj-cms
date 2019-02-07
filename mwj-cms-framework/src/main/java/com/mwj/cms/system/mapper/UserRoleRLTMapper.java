package com.mwj.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mwj.cms.system.entity.Role;
import com.mwj.cms.system.entity.UserRoleRlt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户和角色关联表 Mapper 接口
 * </p>
 *
 * @author Meng Wei Jin
 */
@Mapper
public interface UserRoleRLTMapper extends BaseMapper<UserRoleRlt> {

    /**
     * 根据用户Id获取角色
     *
     * @param UserId
     * @return
     */
    List<Role> getRolesByUserId(Integer UserId);

    /**
     * 根据用户Id删除
     * @param userId
     * @return
     */
    int deleteByUserId(Integer userId);
}
