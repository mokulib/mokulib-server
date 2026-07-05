package pers.liaohaolong.mokulibserver.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import pers.liaohaolong.mokulibserver.dao.EmailCaptchaMapper;
import pers.liaohaolong.mokulibserver.dto.GetEmailCaptchaResultDTO;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.service.base.EmailCaptchaBaseService;
import pers.liaohaolong.mokulibserver.service.base.MailService;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class EmailCaptchaBaseServiceImpl implements EmailCaptchaBaseService {

    private final EmailCaptchaMapper emailCaptchaMapper;

    private final MailService mailService;

    @Autowired
    public EmailCaptchaBaseServiceImpl(EmailCaptchaMapper emailCaptchaMapper, MailService mailService) {
        this.emailCaptchaMapper = emailCaptchaMapper;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public GetEmailCaptchaResultDTO getEmailCaptcha(int userId, String email, EmailCaptcha.BusinessType businessType) {
        // 准备返回
        GetEmailCaptchaResultDTO resultDTO = new GetEmailCaptchaResultDTO();
        // 准备时间戳
        LocalDateTime now = LocalDateTime.now();

        // 查询同用户同业务是否已有数据（决定后续是插入还是更新）
        EmailCaptcha emailCaptcha = emailCaptchaMapper.selectOne(new LambdaQueryWrapper<EmailCaptcha>()
                .eq(EmailCaptcha::getUserId, userId)
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
                newEmailCaptcha.setUserId(userId);
                newEmailCaptcha.setBusinessType(businessType);
                newEmailCaptcha.setCaptcha(captcha);
                newEmailCaptcha.setCoolingTime(coolingTime);
                newEmailCaptcha.setExpireTime(expireTime);
                emailCaptchaMapper.insert(newEmailCaptcha);
            } else {
                emailCaptchaMapper.update(new LambdaUpdateWrapper<EmailCaptcha>()
                        .eq(EmailCaptcha::getUserId, userId)
                        .eq(EmailCaptcha::getBusinessType, businessType)
                        .set(EmailCaptcha::getCaptcha, captcha)
                        .set(EmailCaptcha::getIsUsed, false)
                        .set(EmailCaptcha::getCoolingTime, coolingTime)
                        .set(EmailCaptcha::getExpireTime, expireTime)
                );
            }
            // 异步发送
            Context context = new Context();
            context.setVariable("captcha", captcha);
            mailService.sendMail(email, businessType.getDesc(), businessType.getTemplateName(), context);
            // 发送成功（无论异步发送成功与否）
            resultDTO.setSent(true);
            resultDTO.setCodePrefix(captcha.substring(0, 2));
            resultDTO.setCoolingTime(coolingTime);
            return resultDTO;
        }

        // 已经使用过或仍在发送冷却期内，拒绝发送
        if (emailCaptcha.getIsUsed() || now.isBefore(emailCaptcha.getCoolingTime())) {
            resultDTO.setSent(false);
            // 若未使用过，重复返回验证码前缀；否则不返回有效值
            resultDTO.setCodePrefix(emailCaptcha.getIsUsed() ? "--" : emailCaptcha.getCaptcha().substring(0, 2));
            // 返回下一次可以请求的起始时间，发送冷却到期时间和验证码过期时间中较早的一个
            resultDTO.setCoolingTime(emailCaptcha.getCoolingTime().isBefore(emailCaptcha.getExpireTime()) ? emailCaptcha.getCoolingTime() : emailCaptcha.getExpireTime());
            return resultDTO;
        }

        // 未使用过且超过发送冷却期，更新发送时间
        emailCaptchaMapper.update(new LambdaUpdateWrapper<EmailCaptcha>()
                .eq(EmailCaptcha::getUserId, userId)
                .eq(EmailCaptcha::getBusinessType, businessType.getCode())
                .set(EmailCaptcha::getCoolingTime, now.plusMinutes(businessType.getEmailSendCoolingMinutes()))
        );
        // 异步发送
        Context context = new Context();
        context.setVariable("captcha", emailCaptcha.getCaptcha());
        mailService.sendMail(email, businessType.getDesc(), businessType.getTemplateName(), context);
        // 发送成功（无论异步发送成功与否）
        resultDTO.setSent(true);
        resultDTO.setCodePrefix(emailCaptcha.getCaptcha().substring(0, 2));
        resultDTO.setCoolingTime(now.plusMinutes(businessType.getCaptchaValidationMinutes()));
        return resultDTO;
    }

    @Override
    public void clearExpired() {
        emailCaptchaMapper.delete(new LambdaQueryWrapper<EmailCaptcha>()
                .lt(EmailCaptcha::getExpireTime, LocalDateTime.now())
        );
    }

}
