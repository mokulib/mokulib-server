package pers.liaohaolong.mokulibserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;

import java.time.LocalDateTime;
import java.util.Map;

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

    public ResultDTO toResultDTO(EmailCaptcha.BusinessType businessType) {
        return ResultDTO.builder()
                .status(isSent ? ResultDTO.OK_STATUS : ResultDTO.TOO_FREQUENT)
                .businessType(businessType.getDesc())
                .message(isSent ? "验证码已发送，请注意查收" : "请求过于频繁，请稍后再试")
                .data(Map.of("code_prefix", codePrefix, "cooling_time", coolingTime))
                .build();
    }

}
