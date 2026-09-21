package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@TableName(BorrowRecord.TABLE_NAME)
public class BorrowRecord {

    public static final String TABLE_NAME = "borrow_record";

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer bookCopyId;

    private Boolean isRenewed;

    private Status status;

    private LocalDateTime createTime;

    private LocalDateTime dueTime;

    private LocalDateTime endTime;

    @Getter
    public enum Status {

        BORROWING("BORROWING", "借阅中"),
        RETURNED("RETURNED", "已归还"),
        LOST("LOST", "已丢失"),
        DAMAGED("DAMAGED", "已损坏");

        @EnumValue
        private final String code;

        private final String desc;

        Status(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

}
