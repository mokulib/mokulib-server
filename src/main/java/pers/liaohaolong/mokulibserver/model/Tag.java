package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class Tag {

    public static final String TABLE_NAME = "tag";

    private Integer id;

    private String name;

}
