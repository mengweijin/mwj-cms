package com.mwj.cms.framework.cache;

import com.mwj.cms.common.constant.Const;
import com.mwj.cms.common.enums.ConfigEnum;
import com.mwj.cms.system.service.ConfigService;
import org.apache.commons.lang3.math.NumberUtils;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Meng Wei Jin
 * @description
 * EnableCaching 开启缓存
 **/
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String DEFAULT_CACHE_ALIAS = "defaultCache";

    public static final String LOGIN_FAILED_CACHE_ALIAS = "loginFailedCache";

    /**
     * 默认缓存配置属性
     * @return
     */
    public CacheConfiguration<String, String> defaultCacheConfiguration(){
        CacheConfiguration<String, String> cacheConfiguration =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        // 缓存数据K和V的数值类型，在ehcache3.3中必须指定缓存键值类型,如果使用中类型与配置的不同,会报类转换异常
                        String.class,
                        String.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                // 设置缓存堆容纳元素个数(JVM内存空间)超出个数后会存到offheap中
                                .heap(1000L, EntryUnit.ENTRIES)
                                // 设置堆外储存大小(内存存储) 超出offheap的大小会淘汰规则被淘汰，数值大小必须小于磁盘配置的大小
                                .offheap(50L, MemoryUnit.MB)
                                // 配置磁盘持久化储存(硬盘存储)用来持久化到磁盘,这里设置为false不启用
                                .disk(100L, MemoryUnit.MB, false)
                )
                        // 数据最大存活时间
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(1L)))
                        // 数据最大空闲时间
                        .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(1L)))
                        .build();
        return cacheConfiguration;
    }

    /**
     * 用户登录失败缓存配置属性
     * @return
     */
    public CacheConfiguration<String, AtomicInteger> loginFailedCacheConfiguration(){

        long maxMinutes = 10L;

        CacheConfiguration<String, AtomicInteger> loginFailedCacheConfiguration =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        // 缓存数据K和V的数值类型，在ehcache3.3中必须指定缓存键值类型,如果使用中类型与配置的不同,会报类转换异常
                        String.class,
                        AtomicInteger.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                // 设置缓存堆容纳元素个数(JVM内存空间)超出个数后会存到offheap中
                                .heap(10000L, EntryUnit.ENTRIES)
                                // 设置堆外储存大小(内存存储) 超出offheap的大小会淘汰规则被淘汰，数值大小必须小于磁盘配置的大小
                                .offheap(50L, MemoryUnit.MB)
                                // 配置磁盘持久化储存(硬盘存储)用来持久化到磁盘,这里设置为false不启用
                                .disk(100L, MemoryUnit.MB, false)
                )
                        // 数据最大存活时间
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(maxMinutes)))
                        // 数据最大空闲时间
                        .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(maxMinutes)))
                        .build();
        return loginFailedCacheConfiguration;
    }

    /**
     * EhCache缓存管理器
     * @return
     */
    @Bean
    public PersistentCacheManager persistentCacheManager() {
        // CacheManager管理缓存
        PersistentCacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                // 设置一个默认缓存配置
                .withCache(DEFAULT_CACHE_ALIAS, defaultCacheConfiguration())
                // 设置一个用户登录失败缓存
                .withCache(LOGIN_FAILED_CACHE_ALIAS, loginFailedCacheConfiguration())
                // 硬盘持久化地址
                .with(CacheManagerBuilder.persistence(Const.JAVA_TMP_PATH))
                // 初始化CacheManager 必须传入true；或者通过cacheManager.init()初始化
                .build(true);

         return cacheManager;
    }

}
