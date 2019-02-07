package com.mwj.cms.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author Meng Wei Jin
 */
@Mapper
public interface UserOnlineMapper {

    /**
     * 自定义分页查询
     * @param page
     * @param loginNameList
     * @return
     */
    IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, @Param("loginNameList") List<String> loginNameList);
}
