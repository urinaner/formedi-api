package kr.co.wasp.api.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@EnableCaching
@Configuration
public class CacheConfig {
    @Getter
    public enum Caches {
        NAVER_GET_JOB_LIST(1, TimeUnit.HOURS, 100);

        Caches(int expireAfterWrite, TimeUnit timeUnit, int maximumSize) {
            this.expireAfterWrite = expireAfterWrite;
            this.maximumSize = maximumSize;
            this.timeUnit = timeUnit;
        }

        private int expireAfterWrite = 10;
        private int maximumSize = 50000;
        private TimeUnit timeUnit = TimeUnit.DAYS;
    }

    @Bean
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> caches = Arrays.stream(Caches.values())
                .map(cache ->
                        new CaffeineCache(cache.name(), Caffeine.newBuilder()
                                .recordStats()
                                .expireAfterWrite(cache.getExpireAfterWrite(), cache.getTimeUnit())
                                .maximumSize(cache.getMaximumSize())
                                .build())
                )
                .collect(Collectors.toList());

        cacheManager.setCaches(caches);

        return cacheManager;
    }
}
