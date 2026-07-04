package pers.liaohaolong.mokulibserver.config;

import org.intellij.lang.annotations.RegExp;

/**
 * <h3>正则表达式配置类</h3>
 */
public final class RegexpConfigurations {

    /**
     * 邮箱
     */
    @RegExp
    public static final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /**
     * 密码
     */
    @RegExp
    public static final String PASSWORD_REGEXP = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*_:;,.?])[a-zA-Z\\d!@#$%^&*_:;,.?]{8,20}$";

    /**
     * 邮箱验证码
     */
    @RegExp
    public static final String EMAIL_CAPTCHA_REGEXP = "^[A-Z]{2}-\\d{6}$";

}
