package pers.liaohaolong.mokulibserver.service.base.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import pers.liaohaolong.mokulibserver.service.base.MailService;
import pers.liaohaolong.mokulibserver.util.MailUtils;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final MailUtils mailUtils;

    @Autowired
    public MailServiceImpl(MailUtils mailUtils) {
        this.mailUtils = mailUtils;
    }

    @Async
    @Override
    public void sendMail(String email, String subject, String templateName, Context context) {
        try {
            mailUtils.sendThymeleafMail(email, subject, templateName, context);
        } catch (MailException e) {
            log.error("发送邮件失败，邮箱：{}", email, e);
        }
    }

}
