package pers.liaohaolong.mokulibserver.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pers.liaohaolong.mokulibserver.MybatisPlusConfiguration;
import pers.liaohaolong.mokulibserver.dao.BookMapper;
import pers.liaohaolong.mokulibserver.model.Book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(MybatisPlusConfiguration.class)
public class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;

    @Test
    @Order(1)
    void insertTest() {
        // 序列化测试
        Book book = new Book();
        book.setIsbn("9787505863606");
        book.setTitle("计算机网络原理");
        book.setAuthor("李全龙");
        book.setPublisher("机械工业出版社");
        book.setPublishDate(LocalDate.of(2018, 3, 1));
        book.setEdition("2022年9月第1版");
        book.setPageCount(304);
        book.setLanguage(Locale.CHINESE);
        book.setDescription("全国高等教育自学考试指定教材。");
        book.setPrice(new BigDecimal("49"));

        int id = bookMapper.insert(book);

        Assertions.assertNotNull(book.getId());
        Assertions.assertEquals(1, id);
    }

    @Test
    @Order(2)
    void selectTest() {
        // 反序列化测试
        Book found = bookMapper.selectById(1);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(Locale.CHINESE, found.getLanguage());

        log.info(found.toString());
    }

}
