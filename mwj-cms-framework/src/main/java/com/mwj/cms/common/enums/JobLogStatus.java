package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mengweijin
 * Job调度日志状态
 */

public enum JobLogStatus implements IEnum<Integer> {

    RUNNING(0, "执行中"), SUCCESS(1, "成功"), FAIL(2, "失败");

    private int value;

    private String desc;

    JobLogStatus(final int value, final String desc){
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
        JobLogStatus[] enums = values();
        for (JobLogStatus jobLogStatus : enums){
            if(String.valueOf(jobLogStatus.value).equals(value)){
                return jobLogStatus.desc;
            }
        }

        return null;
    }
}
