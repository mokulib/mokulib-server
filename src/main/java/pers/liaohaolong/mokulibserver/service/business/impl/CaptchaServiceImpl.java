package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.CaptchaMapper;
import pers.liaohaolong.mokulibserver.dto.GetCaptchaDTO;
import pers.liaohaolong.mokulibserver.model.Captcha;
import pers.liaohaolong.mokulibserver.service.business.CaptchaService;
import pers.liaohaolong.mokulibserver.util.ImageCaptchaUtils;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final CaptchaMapper captchaMapper;

    @Override
    @Transactional
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

    @Override
    @Transactional
    public boolean verify(@Nullable String token, @Nullable String captchaIn) {
        if (token == null || token.isBlank() || captchaIn == null || captchaIn.isBlank())
            return false;

        // 尝试读取记录
        Captcha captcha = captchaMapper.selectOne(new LambdaQueryWrapper<Captcha>()
                .eq(Captcha::getToken, token)
                .ge(Captcha::getExpireTime, LocalDateTime.now())
        );
        // 验证码已过期或不存在
        if (captcha == null)
            return false;

        // 核对后无论成功与否都需要删除记录
        captchaMapper.delete(new LambdaQueryWrapper<Captcha>()
                .eq(Captcha::getToken, token)
        );
        // 返回比较结果
        return captcha.getCaptcha().equalsIgnoreCase(captchaIn);
    }

    @Override
    @Transactional
    public void clearExpired() {
        captchaMapper.delete(new LambdaQueryWrapper<Captcha>()
                .lt(Captcha::getExpireTime, LocalDateTime.now())
        );
    }

}
