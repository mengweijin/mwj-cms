package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.ConfigEnum;
import com.mwj.cms.system.entity.Config;
import com.mwj.cms.system.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 */
@Service
public class ConfigService extends ServiceImpl<ConfigMapper, Config> {

    @Autowired
    private ConfigMapper configMapper;

    /**
     * 根据keyName获取配置
     * @param configEnum
     * @return
     */
    public String getValueByEnum(ConfigEnum configEnum){
        Config config = this.getOne(new QueryWrapper<>(new Config().setKeyName(configEnum.getDesc())));
        if(config != null) {
            return  config.getValue();
        }
        return null;
    }

    /**
     * 自定义分页查询
     * @param page
     * @param config
     * @return
     */
    public IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, Config config) {
        return configMapper.selectPageVO(page, config);
    }

    /**
     * 获取配置分类集合
     * @return
     */
    public List<String> selectConfigTypes(){
        return configMapper.selectConfigTypes();
    }
}
