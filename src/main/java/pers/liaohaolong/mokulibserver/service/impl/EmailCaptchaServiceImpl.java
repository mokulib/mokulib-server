package pers.liaohaolong.mokulibserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import pers.liaohaolong.mokulibserver.dao.EmailCaptchaMapper;
import pers.liaohaolong.mokulibserver.dao.UserMapper;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.EmailCaptchaService;
import pers.liaohaolong.mokulibserver.util.MailUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class EmailCaptchaServiceImpl implements EmailCaptchaService {

    private final UserMapper userMapper;

    private final EmailCaptchaMapper emailCaptchaMapper;

    private final MailUtils mailUtils;

    @Autowired
    public EmailCaptchaServiceImpl(UserMapper userMapper, EmailCaptchaMapper emailCaptchaMapper, MailUtils mailUtils) {
        this.userMapper = userMapper;
        this.emailCaptchaMapper = emailCaptchaMapper;
        this.mailUtils = mailUtils;
    }

    @Override
    public ResultDTO newEmailCaptcha(String email, EmailCaptcha.BusinessType businessType) {
        // 查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        // 用户检查
        if (user == null)
            return ResultDTO.error().businessType(businessType.getDesc()).message("用户不存在").build();
        if (!user.getIsActivated())
            return ResultDTO.error().businessType(businessType.getDesc()).message("账户未激活，请先激活账户").build();

        LocalDateTime now = LocalDateTime.now();

        // 查询同用户同业务是否已有数据（决定后续是插入还是更新）
        EmailCaptcha emailCaptcha = emailCaptchaMapper.selectOne(new LambdaQueryWrapper<EmailCaptcha>()
                .eq(EmailCaptcha::getUserId, user.getId())
                .eq(EmailCaptcha::getBusinessType, businessType)
        );
        boolean isInsert = emailCaptcha == null;

        // 数据不存在，或已过期，或已使用过且已过发送冷却期
        if (isInsert || now.isAfter(emailCaptcha.getExpireTime()) || emailCaptcha.getIsUsed() && now.isAfter(emailCaptcha.getCoolingTime())) {
            LocalDateTime coolingTime = now.plusMinutes(businessType.getEmailSendCoolingMinutes());
            LocalDateTime expireTime = now.plusMinutes(businessType.getCaptchaValidationMinutes());
            // 生成验证码
            ThreadLocalRandom random = ThreadLocalRandom.current();
            char c1 = (char) ('A' + random.nextInt(26));
            char c2 = (char) ('A' + random.nextInt(26));
            String captcha = String.format("%c%c-%06d", c1, c2, random.nextInt(1000000));
            // 保存验证码
            if (isInsert) {
                EmailCaptcha newEmailCaptcha = new EmailCaptcha();
                newEmailCaptcha.setUserId(user.getId());
                newEmailCaptcha.setBusinessType(businessType);
                newEmailCaptcha.setCaptcha(captcha);
                newEmailCaptcha.setCoolingTime(coolingTime);
                newEmailCaptcha.setExpireTime(expireTime);
                emailCaptchaMapper.insert(newEmailCaptcha);
            } else {
                emailCaptchaMapper.update(new LambdaUpdateWrapper<EmailCaptcha>()
                        .eq(EmailCaptcha::getUserId, user.getId())
                        .eq(EmailCaptcha::getBusinessType, businessType)
                        .set(EmailCaptcha::getCaptcha, captcha)
                        .set(EmailCaptcha::getIsUsed, false)
                        .set(EmailCaptcha::getCoolingTime, coolingTime)
                        .set(EmailCaptcha::getExpireTime, expireTime)
                );
            }
            // 异步发送
            sendMail(email, captcha);
            // 发送成功（无论异步发送成功与否）
            return ResultDTO.ok()
                    .businessType(businessType.getDesc())
                    .message("验证码已发送，请注意查收")
                    .data(Map.of("codePrefix", captcha.substring(0, 2), "coolingTo", coolingTime))
                    .build();
        }

        // 已经使用过或仍在发送冷却期内，反馈请求过于频繁
        if (emailCaptcha.getIsUsed() || now.isBefore(emailCaptcha.getCoolingTime())) {
            // 若未使用过，重复返回验证码前缀；否则不返回有效值
            String codePrefix = emailCaptcha.getIsUsed() ? "--" : emailCaptcha.getCaptcha().substring(0, 2);
            // 返回下一次可以请求的起始时间
            LocalDateTime coolingTime = emailCaptcha.getCoolingTime();
            LocalDateTime expireTime = emailCaptcha.getExpireTime();
            // 返回发送冷却到期时间和验证码过期时间中较早的一个
            LocalDateTime coolingTo = coolingTime.isBefore(expireTime) ? coolingTime : expireTime;

            return ResultDTO.builder()
                    .status(ResultDTO.TOO_FREQUENT)
                    .businessType(businessType.getDesc())
                    .message("请求过于频繁，请稍后再试")
                    .data(Map.of("codePrefix", codePrefix, "coolingTo", coolingTo))
                    .build();
        }

        // 未使用过且超过发送冷却期，更新发送时间
        emailCaptchaMapper.update(new LambdaUpdateWrapper<EmailCaptcha>()
                .eq(EmailCaptcha::getUserId, user.getId())
                .eq(EmailCaptcha::getBusinessType, businessType.getCode())
                .set(EmailCaptcha::getCoolingTime, now.plusMinutes(businessType.getEmailSendCoolingMinutes()))
        );
        // 异步发送
        sendMail(email, emailCaptcha.getCaptcha());
        // 发送成功（无论异步发送成功与否）
        return ResultDTO.ok()
                .businessType(businessType.getDesc())
                .message("验证码已发送，请注意查收")
                .data(Map.of(
                        "codePrefix", emailCaptcha.getCaptcha().substring(0, 2),
                        "coolingTo", now.plusMinutes(businessType.getEmailSendCoolingMinutes())
                ))
                .build();
    }

    @Override
    public void clearExpired() {
        emailCaptchaMapper.delete(new LambdaQueryWrapper<EmailCaptcha>()
                .lt(EmailCaptcha::getExpireTime, LocalDateTime.now())
        );
    }

    @Async
    public void sendMail(String email, String captcha) {
        Context context = new Context();
        context.setVariable("captcha", captcha);
        try {
            mailUtils.sendThymeleafMail(email, "验证码", "email-captcha", context);
        } catch (MailException e) {
            log.error("发送验证码邮件失败，邮箱：{}，验证码：{}", email, captcha, e);
        }
    }

}
