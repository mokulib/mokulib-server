package pers.liaohaolong.mokulibserver.service.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.dao.CaptchaMapper;
import pers.liaohaolong.mokulibserver.dto.GetCaptchaDTO;
import pers.liaohaolong.mokulibserver.model.Captcha;
import pers.liaohaolong.mokulibserver.service.business.CaptchaService;
import pers.liaohaolong.mokulibserver.util.ImageCaptchaUtils;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final CaptchaMapper captchaMapper;

    @Override
    public GetCaptchaDTO getCaptcha() throws IOException {
        // 生成验证码
        String code = ImageCaptchaUtils.generateCaptchaText();
        // 生成图片
        byte[] image = ImageCaptchaUtils.generateCaptchaImage(code);
        // 创建 DO 对象
        Captcha imageCaptchaDO = new Captcha();
        imageCaptchaDO.setCaptcha(code);
        imageCaptchaDO.setExpireTime(LocalDateTime.now().plusMinutes(1));
        // 保存
        captchaMapper.insert(imageCaptchaDO);
        // 返回验证码 Token 和图片
        return new GetCaptchaDTO(imageCaptchaDO.getToken(), image);
    }

}
