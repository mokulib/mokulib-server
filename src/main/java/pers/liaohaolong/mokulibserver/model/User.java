package pers.liaohaolong.mokulibserver.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@TableName(User.TABLE_NAME)
public class User implements UserDetails, CredentialsContainer {

    public static final String TABLE_NAME = "user";

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String email;

    private Boolean isActivated;

    private String password;

    private Role role;

    private String username;

    private String bio;

    @TableLogic
    private Boolean isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime deleteTime;

    @Getter
    public enum Role {

        USER("USER", "用户", List.of()),
        ADMIN("ADMIN", "管理员", List.of()),;

        /**
         * code 与 枚举名 必须一致
         */
        @EnumValue
        private final String code;

        private final String desc;

        private final List<GrantedAuthority> permissions;

        Role(String code, String desc, List<String> permissions) {
            this.code = code;
            this.desc = desc;
            this.permissions = new ArrayList<>();
            // 添加角色
            this.permissions.add(() -> "ROLE_" + code);
            // 添加权限
            for (String permission : permissions) {
                this.permissions.add(() -> permission);
            }
        }

    }

    // UserDetails

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getPermissions();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActivated;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    // CredentialsContainer

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    /**
     * 规则化昵称，去掉前后空白后，截取前 16 个字符
     *
     * @param nickname 昵称
     * @return 规则化后的昵称
     */
    public static String regularizeNickname(String nickname) {
        nickname = nickname.trim();
        if (nickname.length() > 16)
            nickname = nickname.substring(0, 16);
        return nickname;
    }

}
