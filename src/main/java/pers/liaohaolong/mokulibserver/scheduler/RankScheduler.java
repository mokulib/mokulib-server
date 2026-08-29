package pers.liaohaolong.mokulibserver.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.service.business.RankService;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankScheduler {

    private final CacheManager cacheManager;
    private final RankService rankService;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 0 * * ?")
    public void refresh() {
        log.info("更新排行榜缓存...");

        // 获取缓存
        Cache cache = cacheManager.getCache("rank");
        // 失效已有缓存数据
        if (cache != null)
            cache.invalidate();

        rankService.borrow();
        rankService.favorite();
        rankService.newMonthly();
        rankService.newStore();

        log.info("排行榜缓存更新完成！");
    }

}
