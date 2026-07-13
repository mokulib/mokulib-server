package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
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
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
    public void register(String email, String password, String username) throws BusinessException {
        // 查询用户
        if (userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getEmail, email)))
            throw new BusinessException("邮箱已被注册");

        // 保存用户
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(User.regularizeNickname(username));
        userMapper.insert(user);

        user = userMapper.selectByEmail(email);

        // 使用用户 ID 创建激活码
        ActivationToken activationToken = new ActivationToken();
        activationToken.setUserId(user.getId());
        activationToken.setToken(UUID.randomUUID().toString().replace("-", ""));
        activationToken.setExpireTime(user.getCreateTime().plusDays(7));
        activationTokenMapper.insert(activationToken);

        activationToken = activationTokenMapper.selectById(user.getId());

        // 发送邮件
        Context context = new Context();
        context.setVariable("activationCode", activationToken.getToken());
        context.setVariable("textExpiredAt", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(activationToken.getExpireTime()));
        mailService.sendMail(email, "激活您的账户", "register", context);

        log.debug("发送激活邮件：邮箱={}, 激活码={}", email, activationToken.getToken());
    }

    @Override
    @Transactional
    public void activate(String token) throws BusinessException {
        ActivationToken activationToken = activationTokenMapper.selectByToken(token);
        // 有效验证
        if (activationToken == null)
            throw new BusinessException("激活码无效");
        // 过期验证
        if (LocalDateTime.now().isAfter(activationToken.getExpireTime())) {
            activationTokenMapper.deleteById(activationToken);
            throw new BusinessException("激活码无效");
        }

        // 激活账户
        userMapper.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId, activationToken.getUserId())
                .set(User::getIsActivated, true)
        );
        // 删除激活码
        activationTokenMapper.deleteById(activationToken);
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
