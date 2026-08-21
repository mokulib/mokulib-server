package pers.liaohaolong.mokulibserver.service.business.impl;

import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.dto.response.DashboardDTO;
import pers.liaohaolong.mokulibserver.service.business.DashboardService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Override
    public DashboardDTO get() {
        DashboardDTO dashboardDTO = new DashboardDTO();

        dashboardDTO.setAvailableCopies(1272);
        dashboardDTO.setBookTypes(684);
        dashboardDTO.setBorrowing(47);
        dashboardDTO.setTodayBorrowed(23);
        dashboardDTO.setTodayReturned(18);
        dashboardDTO.setAvailableCopiesChange(12);
        dashboardDTO.setBookTypesChange(8);
        dashboardDTO.setBorrowingPercentage(3.7);
        dashboardDTO.setTodayBorrowedChange(5);
        dashboardDTO.setTodayReturnedChange(-2);
        dashboardDTO.setBorrowTrend(List.of(12, 18, 15, 22, 19, 14, 8));
        dashboardDTO.setReturnTrend(List.of(8, 10, 12, 16, 14, 9, 6));
        dashboardDTO.setNewBookCopyTrend(List.of(2, 3, 1, 4, 2, 0, 3));
        dashboardDTO.setNewBookTrend(List.of(1, 2, 0, 3, 1, 0, 2));

        dashboardDTO.setCategoryStats(List.of(
                new DashboardDTO.CategoryStat("政治", 2),
                new DashboardDTO.CategoryStat("历史", 1),
                new DashboardDTO.CategoryStat("语言", 1),
                new DashboardDTO.CategoryStat("自然科学", 3),
                new DashboardDTO.CategoryStat("计算机", 13),
                new DashboardDTO.CategoryStat("其他", 4)
        ));

        dashboardDTO.setOverdueRecords(List.of(
                new DashboardDTO.OverdueRecord(1, 1, 2, LocalDateTime.of(2026, 8, 10, 0, 0)),
                new DashboardDTO.OverdueRecord(2, 2, 3, LocalDateTime.of(2026, 8, 12, 0, 0)),
                new DashboardDTO.OverdueRecord(3, 3, 5, LocalDateTime.of(2026, 8, 14, 0, 0)),
                new DashboardDTO.OverdueRecord(4, 4, 7, LocalDateTime.of(2026, 8, 15, 0, 0)),
                new DashboardDTO.OverdueRecord(5, 5, 5, LocalDateTime.of(2026, 8, 16, 0, 0)),
                new DashboardDTO.OverdueRecord(6, 6, 3, LocalDateTime.of(2026, 8, 20, 0, 0))
        ));

        dashboardDTO.setWithdrawnCount(12);
        dashboardDTO.setLostWithdrawnCount(5);
        dashboardDTO.setDamagedWithdrawnCount(4);
        dashboardDTO.setOtherWithdrawnCount(3);

        return dashboardDTO;
    }

}
