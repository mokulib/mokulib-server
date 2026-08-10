package pers.liaohaolong.mokulibserver.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.GetEmailCaptchaResultDTO;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;

public interface EmailCaptchaBaseService extends IService<EmailCaptcha> {

    /**
     * 向用户发送对应业务的邮件
     *
     * @param userId 用户 ID
     * @param email 用户邮箱
     * @param businessType 业务类型
     * @return 验证码前缀和冷却时间
     */
    GetEmailCaptchaResultDTO getEmailCaptcha(int userId, String email, EmailCaptcha.BusinessType businessType);

    /**
     * 验证邮箱验证码
     *
     * @param userId 用户 ID
     * @param businessType 业务类型
     * @param captcha 验证码
     * @return 验证结果
     */
    boolean verifyEmailCaptcha(int userId, EmailCaptcha.BusinessType businessType, String captcha);

}
