package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@TableName(User.TABLE_NAME)
public class User {

    public static final String TABLE_NAME = "user";

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String email;

    private Boolean isActivated;

    private String password;

    private Role role;

    private String username;

    @TableLogic
    private Boolean isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime deleteTime;

    @Getter
    public enum Role {

        USER("USER", "用户"),
        ADMIN("ADMIN", "管理员");

        @EnumValue
        private final String code;

        private final String desc;

        Role(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

}
