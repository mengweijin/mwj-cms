package com.mwj.cms.framework.mybatis;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Meng Wei Jin
 * @description
 **/
@EnableTransactionManagement
@Configuration
public class MybatisConfig {

    /**
     * map-underscore-to-camel-case: true。
     * 对JavaBean中属性开启自动驼峰命名规则（camel case）映射，
     * 默认对返回类型为Map的对象的key不起作用，所以需要自定义 MybatisMapWrapperFactory 类来处理
     * @return
     */
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer(){
        return configuration -> configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
    }

    /**
     * mybatis-plus 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * mybatis-plus SQL执行效率插件。设置 dev test 环境开启
     */
    @Bean
    @Profile({"dev","test"})
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor interceptor = new PerformanceInterceptor();
        // 格式化输出sql到控制台
        interceptor.setFormat(true);
        return interceptor;
    }
}
