package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(BookTagRelation.TABLE_NAME)
public class BookTagRelation {

    public static final String TABLE_NAME = "book_tag_relation";

    private Integer bookId;

    private Integer tagId;

}
