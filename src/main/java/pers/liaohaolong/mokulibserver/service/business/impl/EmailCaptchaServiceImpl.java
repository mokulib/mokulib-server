package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.EmailCaptchaMapper;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.service.business.EmailCaptchaService;

import java.time.LocalDateTime;

@Slf4j
@Service
public class EmailCaptchaServiceImpl extends ServiceImpl<EmailCaptchaMapper, EmailCaptcha> implements EmailCaptchaService {

    @Override
    @Transactional
    public void clearExpired() {
        remove(new LambdaQueryWrapper<EmailCaptcha>()
                .lt(EmailCaptcha::getExpireTime, LocalDateTime.now())
        );
    }

}
