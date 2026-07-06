package pers.liaohaolong.mokulibserver.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.service.business.ImageCaptchaService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageCaptchaScheduler {

    private final ImageCaptchaService imageCaptchaService;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void clearExpired() {
        log.info("开始清理过期的验证码...");
        imageCaptchaService.clearExpired();
        log.info("验证码清理完成");
    }

}
