package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(UserBookLike.TABLE_NAME)
public class UserBookLike {

    public static final String TABLE_NAME = "user_book_like";

    private Integer userId;

    private Integer bookId;

    private LocalDateTime createTime;

}
