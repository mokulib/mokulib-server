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

    private CloseStatus closeStatus;

    private LocalDateTime createTime;

    private LocalDateTime dueTime;

    private LocalDateTime closeTime;

    @Getter
    public enum CloseStatus {

        OPEN("OPEN", "借阅中"),
        CLOSE("CLOSED", "已归还"),
        LOST("LOST", "已丢失"),
        DAMAGED("DAMAGED", "已损坏");

        @EnumValue
        private final String code;

        private final String desc;

        CloseStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

}
