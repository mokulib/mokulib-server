package pers.liaohaolong.mokulibserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <h3>获取验证码数据传输对象</h3>
 *
 * <p>Service -> Service, Service -> Controller</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetEmailCaptchaResultDTO {

    private boolean isSent;

    private String codePrefix;

    private LocalDateTime coolingTime;

}
