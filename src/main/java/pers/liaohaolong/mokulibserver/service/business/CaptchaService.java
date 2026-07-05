package pers.liaohaolong.mokulibserver.service.business;

import org.jspecify.annotations.Nullable;
import pers.liaohaolong.mokulibserver.dto.GetCaptchaDTO;

import java.io.IOException;

public interface CaptchaService {

    /**
     * <p>创建验证码，包括 Token、验证码以及图片。</p>
     * <p>需要注意，验证码和图片不应依赖 Token，Token 应保持其自身的独立性，仅作为本次验证的唯一标识。</p>
     *
     * @return {@link GetCaptchaDTO}
     */
    GetCaptchaDTO getCaptcha() throws IOException;

    /**
     * 验证用户输入的验证码是否正确，此接口应是一次性的，无论验证成功与否，ID 与验证码的关联记录都应被删除
     *
     * @param token Token
     * @param captcha 验证码（用户输入的，应忽略大小写）
     * @return 验证是否成功
     */
    boolean verify(@Nullable String token, @Nullable String captcha);

    /**
     * 清理过期验证码
     */
    void clearExpired();

}
