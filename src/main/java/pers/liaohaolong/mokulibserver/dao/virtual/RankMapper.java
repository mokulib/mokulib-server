package pers.liaohaolong.mokulibserver.dao.virtual;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RankMapper {

    @Select("SELECT book_id FROM borrow_record LEFT JOIN book_copy ON borrow_record.book_copy_id = book_copy.id GROUP BY book_copy.book_id ORDER BY COUNT(*) DESC LIMIT 10")
    List<Integer> borrow();

    @Select("SELECT book_id FROM favorite GROUP BY book_id ORDER BY COUNT(*) DESC LIMIT 10")
    List<Integer> favorite();

    @Select("SELECT book_id FROM book_copy GROUP BY book_id HAVING MIN(create_time) >= NOW() - INTERVAL 30 DAY ORDER BY MAX(create_time) DESC LIMIT 10")
    List<Integer> newMonthly();

    @Select("SELECT book_id FROM book_copy GROUP BY book_id ORDER BY MAX(create_time) DESC LIMIT 10")
    List<Integer> newStore();

}
