package com.mwj.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 * EnableScheduling: 	开启定时任务调度支持。 	扫描Spring Bean中的 @Scheduled(cron="0 0 0 * * ?")
 * EnableAsync: 		开启异步任务支持。  	扫描Spring Bean中的 @Async
 * @author Meng Wei Jin
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class MwjCmsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MwjCmsAdminApplication.class, args);
    }

}

