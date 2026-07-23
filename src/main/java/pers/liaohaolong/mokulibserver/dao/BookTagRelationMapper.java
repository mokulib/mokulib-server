package pers.liaohaolong.mokulibserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pers.liaohaolong.mokulibserver.model.BookTagRelation;
import pers.liaohaolong.mokulibserver.model.Tag;

import java.util.List;

public interface BookTagRelationMapper extends BaseMapper<BookTagRelation> {

    @Select("SELECT t.id, t.name FROM book_tag_relation btr LEFT JOIN tag t ON btr.tag_id = t.id WHERE btr.book_id = #{bookId}")
    List<Tag> selectTags(@Param("bookId") Integer bookId);

}
