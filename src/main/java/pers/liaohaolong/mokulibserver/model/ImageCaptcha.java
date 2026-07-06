package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(ImageCaptcha.TABLE_NAME)
public class ImageCaptcha {

    public static final String TABLE_NAME = "image_captcha";

    @TableId(type = IdType.ASSIGN_UUID)
    private String token;

    private String captcha;

    private LocalDateTime expireTime;

}
