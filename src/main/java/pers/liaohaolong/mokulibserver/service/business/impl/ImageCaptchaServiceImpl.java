package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.ImageCaptchaMapper;
import pers.liaohaolong.mokulibserver.dto.GetCaptchaDTO;
import pers.liaohaolong.mokulibserver.model.ImageCaptcha;
import pers.liaohaolong.mokulibserver.service.business.ImageCaptchaService;
import pers.liaohaolong.mokulibserver.util.ImageCaptchaUtils;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageCaptchaServiceImpl implements ImageCaptchaService {

    private final ImageCaptchaMapper imageCaptchaMapper;

    @Override
    @Transactional
    public GetCaptchaDTO getImageCaptcha() throws IOException {
        // 生成验证码
        String code = ImageCaptchaUtils.generateCaptchaText();
        // 生成图片
        byte[] image = ImageCaptchaUtils.generateCaptchaImage(code);
        // 创建 DO 对象
        ImageCaptcha imageCaptcha = new ImageCaptcha();
        imageCaptcha.setCaptcha(code);
        imageCaptcha.setExpireTime(LocalDateTime.now().plusMinutes(1));
        // 保存
        imageCaptchaMapper.insert(imageCaptcha);
        // 返回验证码 Token 和图片
        return new GetCaptchaDTO(imageCaptcha.getToken(), image);
    }

    @Override
    @Transactional
    public boolean verify(@Nullable String token, @Nullable String captcha) {
        if (token == null || token.isBlank() || captcha == null || captcha.isBlank())
            return false;

        // 尝试读取记录
        ImageCaptcha imageCaptcha = imageCaptchaMapper.selectOne(new LambdaQueryWrapper<ImageCaptcha>()
                .eq(ImageCaptcha::getToken, token)
                .ge(ImageCaptcha::getExpireTime, LocalDateTime.now())
        );
        // 验证码已过期或不存在
        if (imageCaptcha == null)
            return false;

        // 核对后无论成功与否都需要删除记录
        imageCaptchaMapper.deleteById(token);
        // 返回比较结果
        return imageCaptcha.getCaptcha().equalsIgnoreCase(captcha);
    }

    @Override
    @Transactional
    public void clearExpired() {
        imageCaptchaMapper.delete(new LambdaQueryWrapper<ImageCaptcha>()
                .lt(ImageCaptcha::getExpireTime, LocalDateTime.now())
        );
    }

}
