package pers.liaohaolong.mokulibserver.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import pers.liaohaolong.mokulibserver.config.RegexpConfigurations;

@Data
public class ChangePasswordDTO {

    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = RegexpConfigurations.PASSWORD_REGEXP, message = "密码格式错误")
    private String newPassword;

}
