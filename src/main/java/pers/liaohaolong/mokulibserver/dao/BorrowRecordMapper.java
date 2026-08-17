package pers.liaohaolong.mokulibserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;

import java.util.List;

public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {

    @Select("SELECT book_id FROM borrow_record LEFT JOIN book_copy ON borrow_record.book_copy_id = book_copy.id GROUP BY book_copy.book_id ORDER BY COUNT(*) DESC LIMIT 10")
    List<Integer> getTopBorrowedBookIds();

}
