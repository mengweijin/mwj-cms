package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mengweijin
 * 接口类型
 */
public enum InterfaceType implements IEnum<Integer> {

    WEB_SERVICE_CXF(1, "Web Service(cxf方式调用)"),
    WEB_SERVICE_HTTP(2, "Web Service(http方式调用)"),
    HTTP(3, "http"),
    SOCKET(4, "socket");

    private int value;

    private String desc;

    InterfaceType(final int value, final String desc){
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc() {
        return this.desc;
    }

    public static String getDesc(String value){
        InterfaceType[] interfaceTypes = values();
        for (InterfaceType interfaceType: interfaceTypes){
            if(String.valueOf(interfaceType.value).equals(value)){
                return interfaceType.desc;
            }
        }

        return null;
    }
}
