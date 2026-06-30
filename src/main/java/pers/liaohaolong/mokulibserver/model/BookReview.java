package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName
public class BookReview {

    public static final String TABLE_NAME = "book_review";

    private Integer id;

    private Integer userId;

    private Integer bookId;

    private Integer score;

    private String content;

    private LocalDateTime createTime;

}
