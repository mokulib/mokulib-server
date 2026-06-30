package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(EmailVerification.TABLE_NAME)
public class EmailVerification {

    public static final String TABLE_NAME = "email_verification";

    private Integer userId;

    private String businessType;

    private String code;

    private Boolean isUsed;

    private LocalDateTime createTime;

    private LocalDateTime sendTime;

}
