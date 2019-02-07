package com.mwj.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mwj.cms.system.entity.Dept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author Meng Wei Jin
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 部门查询 不分页
     * @param dept
     * @return
     */
    List<Map<String, Object>> selectVO(Dept dept);
}
