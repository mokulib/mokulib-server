package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(Captcha.TABLE_NAME)
public class Captcha {

    public static final String TABLE_NAME = "captcha";

    private String token;

    private String captcha;

    private LocalDateTime expireTime;

}
