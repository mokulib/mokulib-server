package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.GetEmailCaptchaResultDTO;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.dto.request.RegisterDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("ping")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void ping() {
    }

    @GetMapping("login")
    public ResultDTO getLoginCaptcha(@RequestParam @NotNull @NotBlank @Email String email) throws BusinessException {
        GetEmailCaptchaResultDTO resultDTO = authService.getLoginCaptcha(email);

        return ResultDTO.builder()
                .status(resultDTO.isSent() ? ResultDTO.OK_STATUS : ResultDTO.TOO_FREQUENT)
                .businessType(EmailCaptcha.BusinessType.LOGIN.getDesc())
                .message(resultDTO.isSent() ? "验证码已发送，请注意查收" : "请求过于频繁，请稍后再试")
                .data(Map.of("codePrefix", resultDTO.getCodePrefix(), "coolingTime", resultDTO.getCoolingTime()))
                .build();
    }

    @PostMapping("register")
    public void register(@RequestBody @Valid RegisterDTO registerDTO) throws BusinessException {
        authService.register(registerDTO.getEmail(), registerDTO.getPassword(), registerDTO.getUsername());
    }

    @PostMapping("activate/{token}")
    @SuccessInfo(message = "激活成功，欢迎使用")
    public void activate(@PathVariable @NotNull @NotBlank String token) throws BusinessException {
        authService.activate(token);
    }

    @GetMapping("close-account")
    public ResultDTO getCloseAccountCaptcha(@AuthenticationPrincipal User user) {
        GetEmailCaptchaResultDTO resultDTO = authService.getCloseAccountCaptcha(user);

        return ResultDTO.builder()
                .status(resultDTO.isSent() ? ResultDTO.OK_STATUS : ResultDTO.TOO_FREQUENT)
                .businessType(EmailCaptcha.BusinessType.CLOSE_ACCOUNT.getDesc())
                .message(resultDTO.isSent() ? "验证码已发送，请注意查收" : "请求过于频繁，请稍后再试")
                .data(Map.of("codePrefix", resultDTO.getCodePrefix(), "coolingTime", resultDTO.getCoolingTime()))
                .build();
    }

    @PostMapping("close-account")
    public ResultDTO closeAccount(@AuthenticationPrincipal User user, @RequestParam("emailCaptcha") String emailCaptcha) throws BusinessException {
        // 关闭账户
        authService.closeAccount(user, emailCaptcha);
        // 签发无效 JWT
        return ResultDTO.ok()
                .message("账户已关闭")
                .data(Map.of("jwt", ""))
                .build();
    }

}
