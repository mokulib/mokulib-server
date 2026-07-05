package pers.liaohaolong.mokulibserver.service.base;

public interface CaptchaBaseService {

    /**
     * 清理过期验证码
     */
    void clearExpired();

}
