package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.ActivationTokenMapper;
import pers.liaohaolong.mokulibserver.dao.EmailCaptchaMapper;
import pers.liaohaolong.mokulibserver.dao.UserMapper;
import pers.liaohaolong.mokulibserver.dto.GetEmailCaptchaResultDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.ActivationToken;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha.BusinessType;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.base.EmailCaptchaBaseService;
import pers.liaohaolong.mokulibserver.service.base.MailService;
import pers.liaohaolong.mokulibserver.service.business.AuthService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmailCaptchaBaseService emailCaptchaBaseService;

    private final MailService mailService;

    private final ActivationTokenMapper activationTokenMapper;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;
    private final EmailCaptchaMapper emailCaptchaMapper;

    @Override
    @Transactional
    public GetEmailCaptchaResultDTO getLoginCaptcha(String email) throws BusinessException {
        // 查询用户
        User user = userMapper.selectByEmail(email);
        // 用户检查
        if (user == null)
            throw new BusinessException("用户不存在");
        if (!user.getIsActivated())
            throw new BusinessException("账户未激活，请先激活账户");

        return emailCaptchaBaseService.getEmailCaptcha(user.getId(), email, BusinessType.LOGIN);
    }

    @Override
    @Transactional
    public void activate(String token) throws BusinessException {
        ActivationToken activationToken = activationTokenMapper.selectByToken(token);
        // 有效验证
        if (activationToken == null)
            throw new BusinessException("链接失效，激活失败");
        // 过期验证
        if (LocalDateTime.now().isAfter(activationToken.getExpireTime())) {
            throw new BusinessException("链接失效，激活失败");
        }

        // 查询需要激活的账户
        User user = userMapper.selectById(activationToken.getUserId());
        // 账户检查（是否已注销）
        if (user == null)
            throw new BusinessException("链接失效，激活失败");
        // 账户检查（是否已激活）
        if (user.getIsActivated())
            throw new BusinessException("链接失效，激活失败");

        // 用户存在，且未激活，激活账户
        userMapper.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId, activationToken.getUserId())
                .set(User::getIsActivated, true)
        );
    }

    @Override
    @Transactional
    public GetEmailCaptchaResultDTO getCloseAccountCaptcha(User user) {
        return emailCaptchaBaseService.getEmailCaptcha(user.getId(), user.getEmail(), BusinessType.CLOSE_ACCOUNT);
    }

    @Override
    @Transactional
    public void closeAccount(User user, String captcha) throws BusinessException {
        // 加载验证码
        EmailCaptcha emailCaptcha = emailCaptchaMapper.selectOne(new LambdaQueryWrapper<EmailCaptcha>()
                .eq(EmailCaptcha::getUserId, user.getId())
                .eq(EmailCaptcha::getBusinessType, BusinessType.CLOSE_ACCOUNT)
        );
        if (emailCaptcha == null || LocalDateTime.now().isAfter(emailCaptcha.getExpireTime()) || emailCaptcha.getIsUsed()) { // 过期验证码、重复使用验证码视为不存在
            throw new BusinessException("验证码错误或验证码已过期");
        }
        // 使用验证码
        emailCaptchaMapper.update(new LambdaUpdateWrapper<EmailCaptcha>()
                .eq(EmailCaptcha::getUserId, user.getId())
                .eq(EmailCaptcha::getBusinessType, BusinessType.CLOSE_ACCOUNT)
                .set(EmailCaptcha::getIsUsed, true)
        );
        // 验证邮箱验证码
        if (captcha == null || !captcha.equals(emailCaptcha.getCaptcha())) {
            throw new BusinessException("验证码错误或验证码已过期");
        }
        // 关闭账户
        userMapper.deleteById(user.getId());
    }

}
