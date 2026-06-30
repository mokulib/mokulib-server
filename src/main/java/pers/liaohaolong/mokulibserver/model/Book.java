package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

@Data
@TableName
public class Book {

    public static final String TABLE_NAME = "book";

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String isbn;

    private String title;

    private String subtitle;

    private String author;

    private String publisher;

    private LocalDate publishDate;

    private String edition;

    private Integer pageCount;

    private Locale language;

    private String description;

    private BigDecimal price;

}
