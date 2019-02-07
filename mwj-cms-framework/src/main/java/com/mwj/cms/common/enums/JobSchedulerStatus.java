package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mengweijin
 * Job调度状态
 */

public enum JobSchedulerStatus implements IEnum<Integer> {

    DRAFT(0, "草稿"), NORMAL(1, "正常"), PAUSE(2, "暂停"), FINISHED(3, "结束");

    private int value;

    private String desc;

    JobSchedulerStatus(final int value, final String desc){
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
        JobSchedulerStatus[] enums = values();
        for (JobSchedulerStatus jobSchedulerStatus: enums){
            if(String.valueOf(jobSchedulerStatus.value).equals(value)){
                return jobSchedulerStatus.desc;
            }
        }

        return null;
    }
}
