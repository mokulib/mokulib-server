package pers.liaohaolong.mokulibserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.liaohaolong.mokulibserver.model.User;

import java.time.LocalDateTime;

/**
 * <h3>JWT 用户数据传输对象</h3>
 *
 * <p>后端 -> 前端, 前端 -> 后端</p>
 * <p>用于生成/解析 JWT 中的用户信息负载，较 {@link User} 类，减少了许多无用的或敏感的信息。</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserDTO {

    private Integer id;

    private String email;

    private String role;

    private String username;

    private String bio;

    private LocalDateTime createTime;

    public JwtUserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole().getCode();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.createTime = user.getCreateTime();
    }

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setRole(User.Role.valueOf(role));
        user.setUsername(username);
        user.setBio(bio);
        user.setCreateTime(createTime);
        return user;
    }

}
