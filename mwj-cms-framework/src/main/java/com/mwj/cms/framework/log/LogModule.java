package com.mwj.cms.framework.log;

/**
 * 日志记录 系统模块枚举
 * @author mengweijin
 */
public enum LogModule {

    OTHER("其他"),
    DEPT("部门管理"),
    USER("用户管理"),
    ROLE("角色管理"),
    MENU("菜单管理"),
    DICT("字典管理"),
    CONFIG("配置管理"),
    INTERFACE("接口管理"),
    FILE("文件管理"),
    LOG("系统日志管理"),
    LOGIN_LOG("登录日志管理"),
    USER_ONLINE("在线用户管理"),
    NOTICE("通知公告"),
    JOB("调度管理");

    private String value;

    LogModule(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
