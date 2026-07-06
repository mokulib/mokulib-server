package pers.liaohaolong.mokulibserver.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.service.business.ActivationTokenService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivationTokenScheduler {

    private final ActivationTokenService activationTokenService;

    @Scheduled(cron = "0 0 * * * ?")
    public void clearExpired() {
        log.info("开始清理过期的激活令牌...");
        activationTokenService.clearExpired();
        log.info("激活令牌清理完成");
    }

}
