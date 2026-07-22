package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(Category.TABLE_NAME)
public class Category {

    public static final String TABLE_NAME = "category";

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

}
