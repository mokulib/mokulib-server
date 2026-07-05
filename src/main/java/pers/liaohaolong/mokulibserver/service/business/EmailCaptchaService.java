package pers.liaohaolong.mokulibserver.service.business;

import jakarta.validation.constraints.Email;
import org.springframework.validation.annotation.Validated;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;

@Validated
public interface EmailCaptchaService {

    ResultDTO newEmailCaptcha(@Email(message = "邮箱不存在") String email, EmailCaptcha.BusinessType businessType);

    void clearExpired();

}
