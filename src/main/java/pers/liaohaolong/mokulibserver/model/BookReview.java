package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName
public class BookReview {

    public static final String TABLE_NAME = "book_review";

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer bookId;

    private Integer score;

    private String content;

    private LocalDateTime createTime;

}
