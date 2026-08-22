package pers.liaohaolong.mokulibserver.dao.virtual;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pers.liaohaolong.mokulibserver.dto.TrendPointDTO;
import pers.liaohaolong.mokulibserver.dto.response.DashboardDTO;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DashboardMapper {

    // 可流通馆藏数量（状态不为 WITHDRAWN 的副本）
    @Select("SELECT COUNT(*) FROM book_copy WHERE status != 'WITHDRAWN'")
    int countAvailableCopies();

    // 图书种类数量（拥有副本的图书数量）
    @Select("SELECT COUNT(DISTINCT book_id) FROM book_copy WHERE status != 'WITHDRAWN'")
    int countBookTypes();

    // 借阅中数量
    @Select("SELECT COUNT(*) FROM borrow_record WHERE close_status = 'OPEN'")
    int countBorrowing();

    // 今日借出数量
    @Select("SELECT COUNT(*) FROM borrow_record WHERE create_time >= CURDATE() AND create_time < CURDATE() + INTERVAL 1 DAY")
    int countTodayBorrowed();

    // 今日归还数量（包含借阅丢失和借阅损毁）
    @Select("SELECT COUNT(*) FROM borrow_record WHERE close_time >= CURDATE() AND close_time < CURDATE() + INTERVAL 1 DAY AND close_status != 'OPEN'")
    int countTodayReturned();

    // 上月最后一天可流通馆藏数量
    @Select("SELECT COUNT(*) FROM book_copy WHERE status != 'WITHDRAWN' AND create_time < DATE_FORMAT(CURDATE(), '%Y-%m-01')")
    int countLastMonthAvailableCopies();

    // 本月新增图书种类（本月首次出现副本的图书）
    @Select("SELECT COUNT(DISTINCT book_id) FROM book_copy WHERE create_time >= DATE_FORMAT(CURDATE(), '%Y-%m-01') AND book_id NOT IN (SELECT DISTINCT book_id FROM book_copy WHERE create_time < DATE_FORMAT(CURDATE(), '%Y-%m-01'))")
    int countNewBookTypesThisMonth();

    // 昨日借出数量
    @Select("SELECT COUNT(*) FROM borrow_record WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) AND create_time < CURDATE()")
    int countYesterdayBorrowed();

    // 昨日归还数量
    @Select("SELECT COUNT(*) FROM borrow_record WHERE close_time >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) AND close_time < CURDATE()")
    int countYesterdayReturned();

    // 近7天借阅趋势（按自然日）
    @Select("SELECT DATE(create_time) as date, COUNT(*) as count FROM borrow_record WHERE create_time >= DATE_SUB(#{today}, INTERVAL 6 DAY) GROUP BY DATE(create_time) ORDER BY date")
    List<TrendPointDTO> getBorrowTrend(@Param("today") LocalDate today);

    // 近7天归还趋势（按自然日）
    @Select("SELECT DATE(close_time) as date, COUNT(*) as count FROM borrow_record WHERE close_time >= DATE_SUB(#{today}, INTERVAL 6 DAY) AND close_status = 'CLOSED' GROUP BY DATE(close_time) ORDER BY date")
    List<TrendPointDTO> getReturnTrend(@Param("today") LocalDate today);

    // 近7天新增馆藏趋势（按自然日）
    @Select("SELECT DATE(create_time) as date, COUNT(*) as count FROM book_copy WHERE create_time >= DATE_SUB(#{today}, INTERVAL 6 DAY) GROUP BY DATE(create_time) ORDER BY date")
    List<TrendPointDTO> getNewBookCopyTrend(@Param("today") LocalDate today);

    // 近7天新增图书趋势（按自然日，首次出现的新book_id）
    @Select("SELECT DATE(first_date) as date, COUNT(*) as count FROM (SELECT book_id, MIN(create_time) as first_date FROM book_copy GROUP BY book_id HAVING MIN(create_time) >= DATE_SUB(#{today}, INTERVAL 6 DAY)) as new_books GROUP BY DATE(first_date) ORDER BY date")
    List<TrendPointDTO> getNewBookTrend(@Param("today") LocalDate today);

    // 分类统计（统计各分类下拥有副本的图书数量）
    @Select("SELECT c.name as name, COUNT(DISTINCT bc.book_id) as count FROM category c LEFT JOIN book b ON c.id = b.category_id LEFT JOIN book_copy bc ON b.id = bc.book_id AND bc.status != 'WITHDRAWN' GROUP BY c.id, c.name HAVING count > 0 ORDER BY count DESC")
    List<DashboardDTO.CategoryStat> getCategoryStats();

    // 逾期记录（所有未归还且已逾期的）
    @Select("SELECT br.book_copy_id as bookCopyId, bc.book_id as bookId, br.user_id as userId, br.due_time as dueTime FROM borrow_record br JOIN book_copy bc ON br.book_copy_id = bc.id WHERE br.close_status = 'OPEN' AND br.due_time < NOW() ORDER BY br.due_time")
    List<DashboardDTO.OverdueRecord> getOverdueRecords();

    // 已下架总量
    @Select("SELECT COUNT(*) FROM book_copy WHERE status = 'WITHDRAWN'")
    int countWithdrawn();

    // 丢失数量
    @Select("SELECT COUNT(*) FROM book_copy WHERE status = 'WITHDRAWN' AND withdrawn_reason = 'LOST'")
    int countLostWithdrawn();

    // 损坏数量
    @Select("SELECT COUNT(*) FROM book_copy WHERE status = 'WITHDRAWN' AND withdrawn_reason = 'DAMAGED'")
    int countDamagedWithdrawn();

    // 其他下架数量
    @Select("SELECT COUNT(*) FROM book_copy WHERE status = 'WITHDRAWN' AND withdrawn_reason = 'OTHER'")
    int countOtherWithdrawn();

}
