package pers.liaohaolong.mokulibserver.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.service.business.EmailCaptchaService;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailCaptchaScheduler {

    private final EmailCaptchaService emailCaptchaService;

    @Scheduled(cron = "0 0 * * * ?")
    public void clearExpired() {
        log.info("开始清理过期的邮箱验证码...");
        emailCaptchaService.clearExpired();
        log.info("邮箱验证码清理完成");
    }

}
