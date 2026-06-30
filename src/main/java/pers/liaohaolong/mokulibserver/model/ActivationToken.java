package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(ActivationToken.TABLE_NAME)
public class ActivationToken {

    public static final String TABLE_NAME = "activation_token";

    @TableId
    private Integer userId;

    private String token;

    private LocalDateTime expireTime;

}
