package com.mwj.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.system.entity.Config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Meng Wei Jin
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {

    /**
     * 自定义分页查询
     * @param page
     * @param config
     * @return
     */
    IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, @Param("config") Config config);

    /**
     * 获取配置分类集合
     * @return
     */
    List<String> selectConfigTypes();
}
