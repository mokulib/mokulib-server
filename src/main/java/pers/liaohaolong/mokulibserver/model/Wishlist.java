package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(Wishlist.TABLE_NAME)
public class Wishlist {

    public static final String TABLE_NAME = "wishlist";

    private Integer userId;

    private Integer bookId;

    private LocalDateTime createTime;

}
