package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(UserBookReviewLike.TABLE_NAME)
public class UserBookReviewLike {

    public static final String TABLE_NAME = "user_book_review_like";

    private Integer userId;

    private Integer bookReviewId;

    private LocalDateTime createTime;

}
