package pers.liaohaolong.mokulibserver.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pers.liaohaolong.mokulibserver.MokuLibServerTest;
import pers.liaohaolong.mokulibserver.dao.BookTagRelationMapper;
import pers.liaohaolong.mokulibserver.model.BookTagRelation;

@Slf4j
@MokuLibServerTest
public class BookTagRelationMapperTest {

    @Autowired
    private BookTagRelationMapper bookTagRelationMapper;

    @Test
    @Order(1)
    @DisplayName("联合主键插入测试")
    public void insertTest() {
        BookTagRelation bookTagRelation = new BookTagRelation();
        bookTagRelation.setBookId(1);
        bookTagRelation.setTagId(1);

        bookTagRelationMapper.insert(bookTagRelation);

        Assertions.assertEquals(1, bookTagRelation.getBookId());
        Assertions.assertEquals(1, bookTagRelation.getTagId());
    }

    @Test
    @Order(2)
    @DisplayName("联合主键查询测试")
    public void selectTest() {
        BookTagRelation bookTagRelation = bookTagRelationMapper.selectOne(new LambdaQueryWrapper<BookTagRelation>()
                .eq(BookTagRelation::getBookId, 1)
                .eq(BookTagRelation::getTagId, 1)
        );

        Assertions.assertNotNull(bookTagRelation);

        log.info("bookTagRelation={}", bookTagRelation);
    }

}
