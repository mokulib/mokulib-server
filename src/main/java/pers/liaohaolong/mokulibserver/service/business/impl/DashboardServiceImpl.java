package pers.liaohaolong.mokulibserver.service.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.virtual.DashboardMapper;
import pers.liaohaolong.mokulibserver.dto.TrendPointDTO;
import pers.liaohaolong.mokulibserver.dto.response.DashboardDTO;
import pers.liaohaolong.mokulibserver.service.business.DashboardService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper dashboardMapper;

    private DashboardDTO cache;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void refresh() {
        log.info("开始更新数据概览...");
        DashboardDTO dashboardDTO = new DashboardDTO();

        // 统计数据
        dashboardDTO.setAvailableCopies(dashboardMapper.countAvailableCopies());
        dashboardDTO.setBookTypes(dashboardMapper.countBookTypes());
        dashboardDTO.setBorrowing(dashboardMapper.countBorrowing());
        dashboardDTO.setTodayBorrowed(dashboardMapper.countTodayBorrowed());
        dashboardDTO.setTodayReturned(dashboardMapper.countTodayReturned());

        // 可流通馆藏较上月
        dashboardDTO.setAvailableCopiesChange(dashboardDTO.getAvailableCopies() - dashboardMapper.countLastMonthAvailableCopies());
        // 图书种类较上月
        dashboardDTO.setBookTypesChange(dashboardMapper.countNewBookTypesThisMonth());
        // 借阅中占可流通馆藏比例（保留一位小数）
        dashboardDTO.setBorrowingPercentage(dashboardDTO.getAvailableCopies() == 0 ? -1 : Math.round(dashboardDTO.getBorrowing().doubleValue() / dashboardDTO.getAvailableCopies().doubleValue() * 1000.0) / 10.0);
        // 借出量较昨日
        dashboardDTO.setTodayBorrowedChange(dashboardDTO.getTodayBorrowed() - dashboardMapper.countYesterdayBorrowed());
        // 归还量较昨日
        dashboardDTO.setTodayReturnedChange(dashboardDTO.getTodayReturned() - dashboardMapper.countYesterdayReturned());

        // 趋势数据
        LocalDate today = LocalDate.now(); // 计算基准
        dashboardDTO.setBorrowTrend(processTrendData(today, dashboardMapper.getBorrowTrend(today)));
        dashboardDTO.setReturnTrend(processTrendData(today, dashboardMapper.getReturnTrend(today)));
        dashboardDTO.setNewBookCopyTrend(processTrendData(today, dashboardMapper.getNewBookCopyTrend(today)));
        dashboardDTO.setNewBookTrend(processTrendData(today, dashboardMapper.getNewBookTrend(today)));
        dashboardDTO.setTrendDays(IntStream.range(0, 7).mapToObj(i -> today.minusDays(6 - i)).collect(Collectors.toList()));

        // 分类统计
        dashboardDTO.setCategoryStats(dashboardMapper.getCategoryStats());

        // 逾期记录
        dashboardDTO.setOverdueRecords(dashboardMapper.getOverdueRecords());

        // 下架统计
        dashboardDTO.setWithdrawnCount(dashboardMapper.countWithdrawn());
        dashboardDTO.setLostWithdrawnCount(dashboardMapper.countLostWithdrawn());
        dashboardDTO.setDamagedWithdrawnCount(dashboardMapper.countDamagedWithdrawn());
        dashboardDTO.setOtherWithdrawnCount(dashboardMapper.countOtherWithdrawn());

        // 更新时间
        dashboardDTO.setUpdateTime(LocalDateTime.now());

        cache = dashboardDTO;
        log.info("数据概览更新完成");
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO get() {
        return cache;
    }

    /**
     * 处理趋势数据，确保最近 7 天（包括今天）每天都有数据点，无数据的日期补 0。
     *
     * @param today 基准日期
     * @param trendPoints 从数据库查询出的原始趋势数据（可能缺少某些日期）
     * @return 趋势数据，按日期升序排列
     */
    private List<Integer> processTrendData(LocalDate today, List<TrendPointDTO> trendPoints) {
        log.info("trendPoints: {}", trendPoints);
        // 将查询结果转换为 Map<日期, 数量>，便于快速查找
        Map<LocalDate, Integer> dateCountMap = trendPoints.stream().collect(Collectors.toMap(TrendPointDTO::getDate, TrendPointDTO::getCount));
        // 使用 IntStream 生成最近 7 天的日期，并从 Map 中获取对应计数，如果不存在则返回 0
        return IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = today.minusDays(6 - i); // 日期按从远到近
                    return dateCountMap.getOrDefault(date, 0);
                })
                .collect(Collectors.toList());
    }

}
