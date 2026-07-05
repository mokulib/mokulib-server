package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.dao.EmailCaptchaMapper;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.service.business.EmailCaptchaService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailCaptchaServiceImpl implements EmailCaptchaService {

    private final EmailCaptchaMapper emailCaptchaMapper;

    @Override
    public void clearExpired() {
        emailCaptchaMapper.delete(new LambdaQueryWrapper<EmailCaptcha>()
                .lt(EmailCaptcha::getExpireTime, LocalDateTime.now())
        );
    }

}
