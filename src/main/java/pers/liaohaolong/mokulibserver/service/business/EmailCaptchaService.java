package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;

public interface EmailCaptchaService extends IService<EmailCaptcha> {

    /**
     * 清理过期验证码
     */
    void clearExpired();

}
