package pers.liaohaolong.mokulibserver.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.service.business.CaptchaService;

import java.io.IOException;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping
    public ResultDTO getCaptcha() throws IOException {
        return ResultDTO.ok().data(captchaService.getCaptcha()).build();
    }

}
