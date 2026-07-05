package pers.liaohaolong.mokulibserver.service.base;

import org.thymeleaf.context.Context;

public interface MailService {

    void sendMail(String email, String subject, String templateName, Context context);

}
