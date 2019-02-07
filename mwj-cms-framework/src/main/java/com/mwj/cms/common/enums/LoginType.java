package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mengweijin
 * 登录类型
 */
public enum LoginType implements IEnum<Integer> {

    LOGIN(0, "登入"),
    LOGOUT(1, "登出");

    private int value;

    private String desc;

    LoginType(final int value, final String desc){
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
        LoginType[] loginTypes = values();
        for (LoginType loginType: loginTypes){
            if(String.valueOf(loginType.value).equals(value)){
                return loginType.desc;
            }
        }

        return null;
    }
}
