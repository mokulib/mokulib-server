package pers.liaohaolong.mokulibserver.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.dao.CaptchaMapper;
import pers.liaohaolong.mokulibserver.model.Captcha;
import pers.liaohaolong.mokulibserver.service.base.CaptchaBaseService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaBaseServiceImpl implements CaptchaBaseService {

    private final CaptchaMapper captchaMapper;

    @Override
    public void clearExpired() {
        captchaMapper.delete(new LambdaQueryWrapper<Captcha>()
                .lt(Captcha::getExpireTime, LocalDateTime.now())
        );
    }

}
