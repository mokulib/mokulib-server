package pers.liaohaolong.mokulibserver.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.dto.response.DashboardDTO;
import pers.liaohaolong.mokulibserver.service.business.DashboardService;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DashboardDTO get() {
        return dashboardService.get();
    }

}
