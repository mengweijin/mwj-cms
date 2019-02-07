package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Meng Wei Jin
 * @description 日志操作类型
 */
public enum LogType implements IEnum<Integer> {

    OTHER(0, "其它"),
    INSERT(1, "新增"),
    UPDATE(2, "修改"),
    DELETE(3, "删除"),
    SELECT(4, "查询"),
    IMPORT(5, "导入"),
    EXPORT(6, "导出"),
    UPLOAD(7, "上传"),
    DOWNLOAD(8, "下载"),
    INVALIDATE_SESSION(9, "强制下线");

    private int value;

    private String desc;

    LogType(final int value, final String desc){
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
        LogType[] logTypes = values();
        for (LogType logType: logTypes){
            if(String.valueOf(logType.value).equals(value)){
                return logType.desc;
            }
        }

        return null;
    }
}
