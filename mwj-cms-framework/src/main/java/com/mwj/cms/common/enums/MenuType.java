package com.mwj.cms.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mengweijin
 * 菜单类型
 */

public enum MenuType implements IEnum<Integer> {

    CATALOG(0, "目录"),
    MENU(1, "菜单"),
    BUTTON(2, "按钮");

    private int value;

    private String desc;

    MenuType(final int value, final String desc){
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
        MenuType[] menuTypes = values();
        for (MenuType menuType: menuTypes){
            if(String.valueOf(menuType.value).equals(value)){
                return menuType.desc;
            }
        }

        return null;
    }

    public static MenuType get(String value){
        MenuType[] statuses = values();
        for (MenuType status: statuses){
            if(String.valueOf(status.value).equals(value)){
                return status;
            }
        }

        return null;
    }
}
