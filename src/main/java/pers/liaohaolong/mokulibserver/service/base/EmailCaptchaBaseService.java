package pers.liaohaolong.mokulibserver.service.base;

import pers.liaohaolong.mokulibserver.dto.GetEmailCaptchaResultDTO;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;

public interface EmailCaptchaBaseService {

    /**
     * 向用户发送对应业务的邮件
     *
     * @param userId 用户 ID
     * @param email 用户邮箱
     * @param businessType 业务类型
     * @return 验证码前缀和冷却时间
     */
    GetEmailCaptchaResultDTO getEmailCaptcha(int userId, String email, EmailCaptcha.BusinessType businessType);

}
