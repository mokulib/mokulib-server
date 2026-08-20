package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.dto.request.ResetPasswordDTO;
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
    public ResultDTO getLoginCaptcha(@RequestParam @NotBlank @Email String email) throws BusinessException {
        return authService.getLoginCaptcha(email).toResultDTO(EmailCaptcha.BusinessType.LOGIN);
    }

    @PostMapping("activate/{token}")
    @SuccessInfo(message = "激活成功，欢迎使用")
    public void activate(@PathVariable @NotBlank String token) throws BusinessException {
        authService.activate(token);
    }

    @GetMapping("close-account")
    public ResultDTO getCloseAccountCaptcha(@AuthenticationPrincipal User user) {
        return authService.getCloseAccountCaptcha(user).toResultDTO(EmailCaptcha.BusinessType.CLOSE_ACCOUNT);
    }

    @DeleteMapping("close-account")
    @SuccessInfo(message = "账户已关闭")
    public Map<String, String> closeAccount(@AuthenticationPrincipal User user, @RequestParam("emailCaptcha") String emailCaptcha) throws BusinessException {
        // 关闭账户
        authService.closeAccount(user, emailCaptcha);
        // 签发无效 JWT
        return Map.of("jwt", "");
    }

    @GetMapping("reset-password")
    public ResultDTO getResetPasswordCaptcha(@AuthenticationPrincipal User user) {
        return authService.getResetPasswordCaptcha(user).toResultDTO(EmailCaptcha.BusinessType.RESET_PASSWORD);
    }

    @PostMapping("reset-password")
    @SuccessInfo(message = "密码已修改，请重新登录")
    public Map<String, String> resetPassword(@AuthenticationPrincipal User user, @RequestParam("emailCaptcha") String emailCaptcha, @RequestBody @NotNull ResetPasswordDTO resetPasswordDTO) {
        // 修改密码
        authService.resetPassword(user, emailCaptcha, resetPasswordDTO);
        // 签发无效 JWT，强制重新登陆
        return Map.of("jwt", "");
    }

}
