package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(HotSearch.TABLE_NAME)
public class HotSearch {

    public static final String TABLE_NAME = "hot_search";

    private Integer id;

    private String keyword;

    private Integer count;

    private LocalDateTime updateTime;

}
