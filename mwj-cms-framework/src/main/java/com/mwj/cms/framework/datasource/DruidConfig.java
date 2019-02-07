package com.mwj.cms.framework.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description druid 配置多数据源
 *
 * @author Meng Wei Jin
 *
 * 当一个接口有2个不同实现时,使用@Autowired注解时，
 * 会报org.springframework.beans.factory.NoUniqueBeanDefinitionException异常信息
 * 1. 方案1-@Qualifier：使用Qualifier注解，选择一个对象的名称,通常比较常用
 * 2. 方案2-@Primary：Primary可以理解为默认优先选择,同时不可以同时设置多个，内部实质是设置BeanDefinition的primary属性
 **/
@Configuration
public class DruidConfig {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource(DataSource masterDataSource, DataSource slaveDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DynamicDataSource.MASTER, masterDataSource);
        targetDataSources.put(DynamicDataSource.SLAVE, slaveDataSource);
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }
}
