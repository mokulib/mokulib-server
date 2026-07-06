package pers.liaohaolong.mokulibserver.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.dto.GetCaptchaDTO;
import pers.liaohaolong.mokulibserver.service.business.ImageCaptchaService;

import java.io.IOException;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class ImageCaptchaController {

    private final ImageCaptchaService imageCaptchaService;

    @GetMapping
    public GetCaptchaDTO getCaptcha() throws IOException {
        return imageCaptchaService.getImageCaptcha();
    }

}
