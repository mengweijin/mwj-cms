package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.LoginType;
import com.mwj.cms.common.enums.ResultStatus;
import com.mwj.cms.system.entity.LoginLog;
import com.mwj.cms.system.mapper.LoginLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 * @since 2018-10-28
 */
@Service
public class LoginLogService extends ServiceImpl<LoginLogMapper, LoginLog> {

    @Autowired
    private LoginLogMapper loginLogMapper;

    /**
     * 获取登录类型枚举类集合
     * @return
     */
    public LoginType[] getLoginTypes(){
        return LoginType.values();
    }

    /**
     * 自定义分页查询
     * @param page
     * @param loginLog
     * @return
     */
    public IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, LoginLog loginLog){
        page = loginLogMapper.selectPageVO(page, loginLog);
        if(page.getTotal() > 0){
            List<Map<String, Object>> recordList = page.getRecords();
            recordList.stream()
                    .map(map -> {
                        map.put("loginType", LoginType.getDesc(String.valueOf(map.get("loginType"))));
                        map.put("status", ResultStatus.getDesc(String.valueOf(map.get("status"))));
                        return map;
                    })
                    .collect(Collectors.toList());
        }

        return page;
    }

    /**
     * 根据登录人查询最后一次登录的登录记录
     * @param loginName
     * @return
     */
    public LoginLog selectLastOneByOperator(String loginName){
        LoginLog loginLog = new LoginLog().setLoginName(loginName).setLoginType(LoginType.LOGIN);
        return getOne(new LambdaQueryWrapper<>(loginLog).orderByDesc(LoginLog::getOperateTime));
    }
}
