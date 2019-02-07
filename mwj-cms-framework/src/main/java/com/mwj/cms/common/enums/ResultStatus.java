package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mengweijin
 * 通用结果状态
 */

public enum ResultStatus implements IEnum<Integer> {

    SUCCESS(0, "成功"),
    FAIL(1, "失败");

    private int value;

    private String desc;

    ResultStatus(final int value, final String desc){
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
        ResultStatus[] resultStatuses = values();
        for (ResultStatus resultStatus: resultStatuses){
            if(String.valueOf(resultStatus.value).equals(value)){
                return resultStatus.desc;
            }
        }

        return null;
    }
}
