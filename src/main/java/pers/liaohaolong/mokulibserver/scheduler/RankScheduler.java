package pers.liaohaolong.mokulibserver.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.service.business.RankService;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankScheduler {

    private final RankService rankService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void refresh() {
        rankService.refresh();
    }

}
