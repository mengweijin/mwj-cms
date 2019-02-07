package com.mwj.cms.system.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.InterfaceType;
import com.mwj.cms.framework.api.webservice.WebServiceClient;
import com.mwj.cms.system.entity.SysInterface;
import com.mwj.cms.system.mapper.InterfaceMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
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
public class InterfaceService extends ServiceImpl<InterfaceMapper, SysInterface> {

    private final Log logger = LogFactory.getLog(this.getClass());

    public static final String REQUEST_TYPE_GET = "GET";

    public static final String REQUEST_TYPE_POST = "POST";

    @Autowired
    private InterfaceMapper interfaceMapper;

    /**
     * 获取接口类型枚举类集合
     * @return
     */
    public InterfaceType[] getInterfaceTypes(){
        return InterfaceType.values();
    }

    /**
     * 根据keyCode获取接口配置
     * @param keyCode
     * @return
     */
    public SysInterface getByKeyCode(String keyCode){
        return getOne(new QueryWrapper<>(new SysInterface().setKeyCode(keyCode)));
    }

    /**
     * 自定义分页查询
     * @param page
     * @param sysInterface
     * @return
     */
    public IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, SysInterface sysInterface) {
        page = interfaceMapper.selectPageVO(page, sysInterface);

        if(page.getTotal() > 0){
            List<Map<String, Object>> recordList = page.getRecords();
            recordList.stream()
                    .map(map -> {
                        map.put("type", InterfaceType.getDesc(String.valueOf(map.get("type"))));
                        return map;
                    })
                    .collect(Collectors.toList());
        }
        return page;
    }

    /**
     * 调用WebService、http、socket等接口的入口方法
     * @param keyCode 可在sys_interface表中查询
     * @param paramsJson
     * @return
     * @throws Exception
     */
    public String execute(String keyCode, String paramsJson) throws Exception {
        String result = null;
        SysInterface sysInterface = this.getByKeyCode(keyCode);

        logger.debug("sysInterface====>" + sysInterface);
        logger.debug("paramsJson====>" + paramsJson);
        if(sysInterface != null){
            switch (sysInterface.getType()){
                case WEB_SERVICE_CXF:
                    result = WebServiceClient.send(sysInterface.getUrl(), sysInterface.getMethodName(), paramsJson);
                    break;
                case WEB_SERVICE_HTTP:
                    result = WebServiceClient.sendByHttp(
                            sysInterface.getUrl(),
                            sysInterface.getNameSpace(),
                            sysInterface.getMethodName(),
                            JSONObject.parseObject(paramsJson, LinkedHashMap.class));
                    break;
                case HTTP:
                    if(REQUEST_TYPE_GET.equalsIgnoreCase(sysInterface.getRequestType())){
                        result = HttpUtil.get(
                                sysInterface.getUrl(),
                                JSONObject.parseObject(paramsJson, LinkedHashMap.class));
                    } else if(REQUEST_TYPE_POST.equalsIgnoreCase(sysInterface.getRequestType())){
                        result = HttpUtil.post(
                                sysInterface.getUrl(),
                                JSONObject.parseObject(paramsJson, LinkedHashMap.class));
                    } else {
                        logger.error(":Does not support the current request type by "
                                + sysInterface.getRequestType() + "!");
                    }
                    break;
                case SOCKET:
                    logger.error("Interface does not support the socket type!");
                    break;
                default:
                    logger.error("The corresponding interface type is not found by "
                            + sysInterface.getType()
                            + ", please confirm database configuration is correct！");
                    break;
            }
        } else {
            logger.error("Not found interface record by keyCode " + keyCode + "!");
        }

        logger.debug("result====>" + result);
        return result;
    }

}
