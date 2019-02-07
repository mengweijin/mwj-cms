package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mengweijin
 * 系统配置枚举
 */

public enum ConfigEnum implements IEnum<Integer> {

    DEFAULT_PASSWORD(0, "defaultUserResetPassword"),
    OCR_WINDOWS_PATH(3, "ocrToolWindowsPath"),
    OCR_LINUX_PATH(4, "ocrToolLinuxPath");

    private int value;

    private String desc;

    ConfigEnum(final int value, final String desc){
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
        ConfigEnum[] enums = values();
        for (ConfigEnum configEnum: enums){
            if(String.valueOf(configEnum.value).equals(value)){
                return configEnum.desc;
            }
        }

        return null;
    }

    public static ConfigEnum get(String value){
        ConfigEnum[] enums = values();
        for (ConfigEnum configEnum: enums){
            if(String.valueOf(configEnum.value).equals(value)){
                return configEnum;
            }
        }

        return null;
    }
}
