package org.example.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import org.ehcache.config.Builder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.impl.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.ehcache.spi.service.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import java.io.File;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.ehcache.config.builders.CacheEventListenerConfigurationBuilder.newEventListenerConfiguration;
import static org.ehcache.event.EventType.CREATED;
import static org.ehcache.event.EventType.EXPIRED;
import static org.ehcache.event.EventType.UPDATED;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.update-time-in-minutes}")
    private Long cacheExpireTime;
    private ExpireCacheEventListener expireCacheEventListener;

    public CacheConfig(ExpireCacheEventListener expireCacheEventListener) {
        this.expireCacheEventListener = expireCacheEventListener;
    }

    /**
     * Implementation Ehcache manager for storing persistent cache on disk
     * @return JCacheCacheManager with Ehcache under the hood
     */
    @Bean
    public CacheManager cacheManager() {
        var config = CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        Long.class, JsonNode.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .disk(20, MemoryUnit.MB, true)
                )
                .withExpiry(Expirations.timeToIdleExpiration(Duration.of(cacheExpireTime, TimeUnit.SECONDS)))
                .withService(getCacheEventListenerBuilder())
                .build();

        Map<String, CacheConfiguration<?,?>> caches = Map.of("playerCache", config);

        var cachingProvider = Caching.getCachingProvider();
        var ehcacheProvider = (EhcacheCachingProvider) cachingProvider;

        var configuration = new DefaultConfiguration(
                caches,
                ehcacheProvider.getDefaultClassLoader(),
                new DefaultPersistenceConfiguration(new File("tmp/ehcache")));

        javax.cache.CacheManager cacheManager = ehcacheProvider.getCacheManager(
                ehcacheProvider.getDefaultURI(), configuration);
        // add shutdown hook to close proper cacheManager
        Runtime.getRuntime().addShutdownHook(new Thread(cacheManager::close));
        return new JCacheCacheManager(cacheManager);
    }

    private CacheEventListenerConfigurationBuilder getCacheEventListenerBuilder() {
        return newEventListenerConfiguration(expireCacheEventListener, EXPIRED, CREATED, UPDATED)
                .asynchronous()
                .unordered();
    }
}

