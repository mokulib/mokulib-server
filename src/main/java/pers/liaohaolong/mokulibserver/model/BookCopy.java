package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName(BookCopy.TABLE_NAME)
public class BookCopy {

    public static final String TABLE_NAME = "book_copy";

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer book_id;

    private BigDecimal purchasePrice;

    private LocalDate purchaseDate;

    private String source;

    private Status status;

    private Integer entryBy;

    private LocalDateTime createTime;

    private LocalDateTime removeTime;

    @Getter
    public enum Status {

        AVAILABLE("AVAILABLE", "可借阅"),
        UNAVAILABLE("UNAVAILABLE", "禁止借阅"),
        WITHDRAWN("WITHDRAWN", "已下架");

        @EnumValue
        private final String code;

        private final String desc;

        Status(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

}
