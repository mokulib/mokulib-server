package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@TableName(EmailCaptcha.TABLE_NAME)
public class EmailCaptcha {

    public static final String TABLE_NAME = "email_captcha";

    private Integer userId;

    private BusinessType businessType;

    private String captcha;

    private Boolean isUsed;

    private LocalDateTime coolingTime;

    private LocalDateTime expireTime;

    @Getter
    public enum BusinessType {

        LOGIN("LOGIN", "登录", 1, 15),
        CLOSE_ACCOUNT("CLOSE_ACCOUNT", "删除账户", 1, 30),;

        @EnumValue
        private final String code;

        private final String desc;

        private final int emailSendCoolingMinutes;

        private final int captchaValidationMinutes;

        BusinessType(String code, String desc, int emailSendCoolingMinutes, int captchaValidationMinutes) {
            this.code = code;
            this.desc = desc;
            this.emailSendCoolingMinutes = emailSendCoolingMinutes;
            this.captchaValidationMinutes = captchaValidationMinutes;
        }

    }

}
