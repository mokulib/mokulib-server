package pers.liaohaolong.mokulibserver.service.business;

public interface ActivationTokenService {

    /**
     * 清理过期验证码
     */
    void clearExpired();

}
