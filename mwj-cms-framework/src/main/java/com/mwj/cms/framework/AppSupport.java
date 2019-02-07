package com.mwj.cms.framework;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Component
public class AppSupport {

    /**
     * 开发环境
     */
    public static final String ENV_DEV = "dev";

    /**
     * 测试环境
     */
    public static final String ENV_TEST = "test";

    /**
     * 生产环境
     */
    public static final String ENV_PROD = "prod";

    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * 应用作者
     */
    @Value("${app.info.author}")
    @Getter
    private String author;

    /**
     * 应用版本号
     */
    @Value("${app.info.version}")
    @Getter
    private String version;

    /**
     * 应用URL
     */
    @Value("${app.info.url}")
    @Getter
    private String url;

    /**
     * 应用URL
     */
    @Value("${app.info.blog}")
    @Getter
    private String blog;

    /**
     * 系统管理员邮箱地址
     */
    @Value("${spring.mail.username}")
    @Getter
    private String mail;



    public boolean isDev(){
        return ENV_DEV.equalsIgnoreCase(profile);
    }

    public boolean isTest(){
        return ENV_TEST.equalsIgnoreCase(profile);
    }

    public boolean isProd(){
        return ENV_PROD.equalsIgnoreCase(profile);
    }
}
