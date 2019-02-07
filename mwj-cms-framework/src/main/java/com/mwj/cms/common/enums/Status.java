package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mengweijin
 * 表记录通用状态
 */

public enum Status implements IEnum<Integer> {

    NORMAL(0, "正常"),
    DISABLE(1, "停用");

    private int value;

    private String desc;

    Status(final int value, final String desc){
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue(){
        return value;
    }

    @JsonValue
    public String getDesc() {
        return desc;
    }

    public static String getDesc(String value){
        Status[] statuses = values();
        for (Status status: statuses){
            if(String.valueOf(status.value).equals(value)){
                return status.desc;
            }
        }

        return null;
    }

    public static Status get(String value){
        Status[] statuses = values();
        for (Status status: statuses){
            if(String.valueOf(status.value).equals(value)){
                return status;
            }
        }

        return null;
    }
}
