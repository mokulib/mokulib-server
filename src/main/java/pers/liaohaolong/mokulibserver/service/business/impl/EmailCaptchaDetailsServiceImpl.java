package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.dao.EmailCaptchaMapper;
import pers.liaohaolong.mokulibserver.dao.UserMapper;
import pers.liaohaolong.mokulibserver.exception.EmailCaptchaNotFoundException;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.EmailCaptchaDetailsService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailCaptchaDetailsServiceImpl implements EmailCaptchaDetailsService {

    private final UserMapper userMapper;

    private final EmailCaptchaMapper emailCaptchaMapper;

    @Override
    public @NonNull UserDetails loadUserCaptchaByEmail(@NonNull String email) throws UsernameNotFoundException, EmailCaptchaNotFoundException {
        // 加载用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (user == null) {
            log.debug("User not found: {}", email);
            throw new UsernameNotFoundException("User not found");
        }
        // 加载验证码
        EmailCaptcha emailCaptcha = emailCaptchaMapper.selectOne(new LambdaQueryWrapper<EmailCaptcha>()
                .eq(EmailCaptcha::getUserId, user.getId())
                .eq(EmailCaptcha::getBusinessType, EmailCaptcha.BusinessType.LOGIN)
        );
        if (emailCaptcha == null || LocalDateTime.now().isAfter(emailCaptcha.getExpireTime()) || emailCaptcha.getIsUsed()) { // 过期验证码、重复使用验证码视为不存在
            log.debug("Captcha not found: {}", email);
            throw new EmailCaptchaNotFoundException("Email Captcha not found");
        }
        // 使用验证码
        emailCaptchaMapper.update(new LambdaUpdateWrapper<EmailCaptcha>()
                .eq(EmailCaptcha::getUserId, user.getId())
                .eq(EmailCaptcha::getBusinessType, EmailCaptcha.BusinessType.LOGIN)
                .set(EmailCaptcha::getIsUsed, true)
        );
        // 使用期望的验证码替换密码字段
        user.setPassword(emailCaptcha.getCaptcha());
        log.debug("User found: {}", user);
        return user;
    }

}
