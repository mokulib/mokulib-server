package pers.liaohaolong.mokulibserver.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.service.EmailCaptchaService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EmailCaptchaService emailCaptchaService;

    @Autowired
    public AuthController(EmailCaptchaService emailCaptchaService) {
        this.emailCaptchaService = emailCaptchaService;
    }

    @GetMapping("login")
    public ResultDTO getNewEmailCaptcha(@RequestParam String email) {
        return emailCaptchaService.newEmailCaptcha(email, EmailCaptcha.BusinessType.LOGIN);
    }

}
