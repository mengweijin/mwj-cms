package com.mwj.cms.common.enums;

/**
 * @author mengweijin
 */
public enum ErrorCode {

    /** 数据校验不通过 */
    VALIDATE_ERROR(1001);

    private final int code;

    ErrorCode (int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
