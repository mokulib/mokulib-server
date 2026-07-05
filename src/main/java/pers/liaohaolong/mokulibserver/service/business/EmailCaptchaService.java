package pers.liaohaolong.mokulibserver.service.business;

public interface EmailCaptchaService {

    /**
     * 清理过期验证码
     */
    void clearExpired();

}
