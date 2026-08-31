package pers.liaohaolong.mokulibserver.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfigurations {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();

        // 默认配置
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
        );

        // 独立配置
        manager.registerCustomCache("rank", Caffeine.newBuilder().maximumSize(4).build());
        manager.registerCustomCache("dashboard", Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(1).build());

        return manager;
    }

}
