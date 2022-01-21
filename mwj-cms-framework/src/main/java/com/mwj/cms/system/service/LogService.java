package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.LogType;
import com.mwj.cms.common.enums.ResultStatus;
import com.mwj.cms.system.entity.SysLog;
import com.mwj.cms.system.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 */
@Service
public class LogService extends ServiceImpl<LogMapper, SysLog> {

    @Autowired
    private LogMapper logMapper;

    /**
     * 获取日志类型枚举类的name集合
     * @return
     */
    public LogType[] getLogTypes(){
        return LogType.values();
    }

    /**
     * 自定义分页查询
     * @param page
     * @param sysLog
     * @return
     */
    public IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, SysLog sysLog){
        page = logMapper.selectPageVO(page, sysLog);
        if(page.getTotal() > 0){
            List<Map<String, Object>> recordList = page.getRecords();
            recordList.stream()
                    .map(map -> {
                        map.put("type", LogType.getDesc(String.valueOf(map.get("type"))));
                        map.put("status", ResultStatus.getDesc(String.valueOf(map.get("status"))));
                        // 由于数据库保存的 reqParam 是字符串类型的 JSON，在 H2 环境下，MyBatis 查出来会被转成 NClobProxyImpl.class 类型的值。
                        // 从而导致 jackson 无法序列化而抛出异常，因此这里转一下（临时解决方案）
                        // 在 MySQL 下是正常的。
                        map.put("reqParam", String.valueOf(map.get("reqParam")));
                        return map;
                    })
                    .collect(Collectors.toList());
        }

        return page;
    }
}
