package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(Category.TABLE_NAME)
public class Category {

    public static final String TABLE_NAME = "category";

    private Integer id;

    private Integer parentId;

    private Integer order;

    private String name;

}
