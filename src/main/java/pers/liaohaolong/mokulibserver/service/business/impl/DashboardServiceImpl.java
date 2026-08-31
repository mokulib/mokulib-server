package pers.liaohaolong.mokulibserver.service.business.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.dto.response.DashboardDTO;
import pers.liaohaolong.mokulibserver.service.business.DashboardService;

/**
 * <h3>数据概览服务实现</h3>
 *
 * <p>本服务实现仅控制是否从缓存中获取数据，具体数据获取逻辑由 {@link CachedDashboardServiceImpl} 实现。</p>
 */
@Slf4j
@Primary
@Service
public class DashboardServiceImpl implements DashboardService {

    private final DashboardService dashboardService;
    private final CacheManager cacheManager;

    public DashboardServiceImpl(@Qualifier("cachedDashboardServiceImpl") DashboardService dashboardService, CacheManager cacheManager) {
        this.dashboardService = dashboardService;
        this.cacheManager = cacheManager;
    }

    @Override
    public DashboardDTO get(boolean noCache) {
        if (noCache) {
            // 获取缓存
            Cache cache = cacheManager.getCache("dashboard");
            // 失效已有缓存数据
            if (cache != null)
                cache.invalidate();
        }
        return dashboardService.get(noCache);
    }

}
