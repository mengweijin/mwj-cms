package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mengweijin
 * 计划执行错误策略
 */
public enum MisfirePolicy implements IEnum<Integer> {

    /**
     * 立刻执行，以当前时间为触发频率立刻触发一次执行，然后按照Cron频率依次执行（默认）
     */
    MISFIRE_FIRE_AND_PROCEED(0, "立刻执行", "以当前时间为触发频率立刻触发一次执行，然后按照Cron频率依次执行"),
    /**
     * 放弃执行，不触发立即执行，等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
     */
    MISFIRE_DO_NOTHING (1, "放弃执行", "不触发立即执行，等待下次Cron触发频率到达时刻开始按照Cron频率依次执行"),
    /**
     * 全部执行，以错过的第一个频率时间立刻开始执行，重做错过的所有频率周期后，当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
     */
    MISFIRE_IGNORE_MISFIRES(2, "全部执行", "以错过的第一个频率时间立刻开始执行，重做错过的所有JOB，之后按照正常的Cron频率依次执行");

    private int value;

    private String desc;

    private String detail;

    MisfirePolicy(final int value, final String desc, final String detail){
        this.value = value;
        this.desc = desc;
        this.detail = detail;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc() {
        return this.desc;
    }

    public String getDetail() {
        return this.detail;
    }

    public static String getDesc(String value){
        MisfirePolicy[] enums = values();
        for (MisfirePolicy misfirePolicy: enums){
            if(String.valueOf(misfirePolicy.value).equals(value)){
                return misfirePolicy.desc;
            }
        }

        return null;
    }
}
