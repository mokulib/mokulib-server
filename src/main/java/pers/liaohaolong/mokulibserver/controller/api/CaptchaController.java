package pers.liaohaolong.mokulibserver.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.service.business.CaptchaService;

import java.io.IOException;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    @Autowired
    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping
    public ResultDTO getCaptcha() throws IOException {
        return ResultDTO.ok().data(captchaService.getCaptcha()).build();
    }

}
