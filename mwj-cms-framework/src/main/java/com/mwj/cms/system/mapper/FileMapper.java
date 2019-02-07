package com.mwj.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mwj.cms.system.entity.SysFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Meng Wei Jin
 */
@Mapper
public interface FileMapper extends BaseMapper<SysFile> {

    /**
     * 自定义分页查询
     * @param page
     * @param sysFile
     * @return
     */
    IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, @Param("file") SysFile sysFile);
}
