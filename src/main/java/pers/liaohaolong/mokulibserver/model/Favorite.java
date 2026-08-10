package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(Favorite.TABLE_NAME)
public class Favorite {

    public static final String TABLE_NAME = "favorite";

    private Integer userId;

    private Integer bookId;

    private LocalDateTime createTime;

}
