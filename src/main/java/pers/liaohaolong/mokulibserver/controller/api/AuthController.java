package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.dto.GetEmailCaptchaResultDTO;
import pers.liaohaolong.mokulibserver.dto.RegisterDTO;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.service.business.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("login")
    public ResultDTO getLoginCaptcha(@RequestParam @NotNull @NotBlank @Email String email) throws BusinessException {
        GetEmailCaptchaResultDTO resultDTO = authService.getLoginCaptcha(email);

        if (resultDTO.isSent())
            return ResultDTO.ok()
                    .businessType(EmailCaptcha.BusinessType.LOGIN.getDesc())
                    .message("验证码已发送，请注意查收")
                    .data(Map.of("codePrefix", resultDTO.getCodePrefix(), "coolingTime", resultDTO.getCoolingTime()))
                    .build();
        else
            return ResultDTO.builder()
                    .status(ResultDTO.TOO_FREQUENT)
                    .businessType(EmailCaptcha.BusinessType.LOGIN.getDesc())
                    .message("请求过于频繁，请稍后再试")
                    .data(Map.of("codePrefix", resultDTO.getCodePrefix(), "coolingTime", resultDTO.getCoolingTime()))
                    .build();
    }

    @PostMapping("register")
    public void register(@RequestBody @Valid RegisterDTO registerDTO) throws BusinessException {
        authService.register(registerDTO.getEmail(), registerDTO.getPassword(), registerDTO.getUsername());
    }

    @GetMapping("activate/{token}")
    public void activate(@PathVariable @NotNull @NotBlank String token) throws BusinessException {
        authService.activate(token);
    }

}
