package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mengweijin
 * 性别枚举
 */

public enum Sex implements IEnum<Integer> {

    MALE(0, "男"),
    FEMALE(1, "女");

    private int value;

    private String desc;

    Sex(final int value, final String desc){
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
        Sex[] sexes = values();
        for (Sex sex: sexes){
            if(String.valueOf(sex.value).equals(value)){
                return sex.desc;
            }
        }

        return null;
    }

    public static Sex get(String value){
        Sex[] statuses = values();
        for (Sex status: statuses){
            if(String.valueOf(status.value).equals(value)){
                return status;
            }
        }

        return null;
    }
}
