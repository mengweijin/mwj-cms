package com.mwj.cms.framework.web.service;

import com.mwj.cms.common.enums.ResultStatus;
import com.mwj.cms.common.enums.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Service
public class CommonService {

    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * 获取状态枚举类集合
     * @return
     */
    public Status[] getStatuses(){
        return Status.values();
    }

    /**
     * 获取登录结果状态枚举类集合
     * @return
     */
    public ResultStatus[] getResultStatuses(){
        return ResultStatus.values();
    }
}
