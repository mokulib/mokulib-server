package pers.liaohaolong.mokulibserver.dto.response;

import lombok.Data;
import pers.liaohaolong.mokulibserver.model.User;

import java.time.LocalDateTime;

@Data
public class NonsensitiveUserDTO {

    private Integer id;

    private String email;

    private User.Role role;

    private String username;

    private String bio;

    private LocalDateTime createTime;

    public static NonsensitiveUserDTO fromUser(User user) {
        NonsensitiveUserDTO nonsensitiveUserDTO = new NonsensitiveUserDTO();
        nonsensitiveUserDTO.id = user.getId();
        nonsensitiveUserDTO.email = user.getEmail();
        nonsensitiveUserDTO.role = user.getRole();
        nonsensitiveUserDTO.username = user.getUsername();
        nonsensitiveUserDTO.bio = user.getBio();
        nonsensitiveUserDTO.createTime = user.getCreateTime();
        return nonsensitiveUserDTO;
    }

}
