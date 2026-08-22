package pers.liaohaolong.mokulibserver.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.service.business.DashboardService;

@Slf4j
@Component
@AllArgsConstructor
public class DashboardScheduler {

    private final DashboardService dashboardService;

    @Scheduled(cron = "0 0 * * * ?")
    public void refresh() {
        dashboardService.refresh();
    }

}
