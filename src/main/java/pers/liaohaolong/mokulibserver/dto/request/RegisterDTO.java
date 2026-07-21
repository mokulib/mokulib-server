package pers.liaohaolong.mokulibserver.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import pers.liaohaolong.mokulibserver.config.RegexpConfigurations;

@Data
public class RegisterDTO {

    @NotNull(message = "邮箱不能为空")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotNull(message = "昵称不能为空")
    @NotBlank(message = "昵称不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = RegexpConfigurations.PASSWORD_REGEXP, message = "密码格式不正确")
    private String password;

}
