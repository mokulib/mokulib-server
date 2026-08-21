package pers.liaohaolong.mokulibserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    // 统计
    private int availableCopies;      // 可流通馆藏
    private int bookTypes;            // 图书种类
    private int borrowing;            // 借阅中
    private int todayBorrowed;        // 今日借出
    private int todayReturned;        // 今日归还

    // 卡片辅助数据
    private int availableCopiesChange;   // 可流通馆藏较上月变化
    private int bookTypesChange;         // 图书种类较上月变化
    private double borrowingPercentage;  // 借阅中占馆藏比例
    private int todayBorrowedChange;     // 今日借出较昨日变化
    private int todayReturnedChange;     // 今日归还较昨日变化

    // 趋势图
    private List<Integer> borrowTrend;       // 借阅趋势
    private List<Integer> returnTrend;       // 归还趋势
    private List<Integer> newBookCopyTrend;  // 新增馆藏趋势
    private List<Integer> newBookTrend;      // 新增图书趋势

    // 分类分布
    private List<CategoryStat> categoryStats;

    // 逾期列表
    private List<OverdueRecord> overdueRecords;

    // 已下架状态
    private int withdrawnCount;         // 已下架总量
    private int lostWithdrawnCount;     // 丢失数量
    private int damagedWithdrawnCount;  // 损坏数量
    private int otherWithdrawnCount;    // 其他下架数量

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStat {
        private String name;
        private int count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverdueRecord {
        private Integer bookCopyId;
        private Integer bookId;
        private Integer userId;
        private LocalDateTime dueTime;
    }

}
