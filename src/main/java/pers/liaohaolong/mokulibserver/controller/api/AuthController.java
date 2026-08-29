package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.service.business.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("ping")
    @PreAuthorize("isAuthenticated()")
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

}
