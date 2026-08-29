package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.dto.request.ResetPasswordDTO;
import pers.liaohaolong.mokulibserver.dto.request.UpdateUsernameDTO;
import pers.liaohaolong.mokulibserver.dto.response.BorrowRecordWithBookIdDTO;
import pers.liaohaolong.mokulibserver.dto.response.HistoryDTO;
import pers.liaohaolong.mokulibserver.dto.response.JwtDTO;
import pers.liaohaolong.mokulibserver.dto.response.NonsensitiveUserDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.EmailCaptcha;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.UserService;
import pers.liaohaolong.mokulibserver.util.JwtUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public NonsensitiveUserDTO get(@RequestParam(required = false) Integer id, @RequestParam(required = false) String email) throws BusinessException {
        if (id != null)
            return userService.get(id);
        if (email != null)
            return userService.get(email);
        throw new BusinessException("id 和 email 参数不能同时为空");
    }

    @GetMapping("list")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<NonsensitiveUserDTO> list(@RequestParam @NotNull @NotEmpty List<Integer> ids) throws BusinessException {
        return userService.listByIds(ids.stream().distinct().toList()).stream().map(NonsensitiveUserDTO::fromUser).toList();
    }

    @PostMapping(value = "me/avatar", consumes = "application/octet-stream")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "上传成功")
    public void uploadAvatar(@AuthenticationPrincipal User user, @RequestBody byte[] avatar) throws BusinessException {
        userService.uploadAvatar(user.getId(), avatar);
    }

    @PostMapping("me/username")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "修改成功")
    public JwtDTO updateUsername(@AuthenticationPrincipal User user, @RequestBody UpdateUsernameDTO updateUsernameDTO) throws BusinessException {
        userService.updateUsername(user.getId(), updateUsernameDTO.getUsername());
        return new JwtDTO(jwtUtils.generateToken(userService.getById(user.getId())));
    }

    @GetMapping("me/borrowing")
    @PreAuthorize("isAuthenticated()")
    public List<BorrowRecordWithBookIdDTO> getBorrowing(@AuthenticationPrincipal User user) {
        return userService.getBorrowing(user.getId());
    }

    @GetMapping("me/favorites")
    @PreAuthorize("isAuthenticated()")
    public List<Integer> getFavorites(@AuthenticationPrincipal User user) {
        return userService.getFavorites(user.getId());
    }

    @GetMapping("me/history")
    @PreAuthorize("isAuthenticated()")
    public List<HistoryDTO> getHistory(@AuthenticationPrincipal User user) {
        return userService.getHistory(user.getId());
    }

    @GetMapping("me/close-account")
    @PreAuthorize("isAuthenticated()")
    public ResultDTO getCloseAccountCaptcha(@AuthenticationPrincipal User user) {
        return userService.getCloseAccountCaptcha(user).toResultDTO(EmailCaptcha.BusinessType.CLOSE_ACCOUNT);
    }

    @DeleteMapping("me/close-account")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "账户已关闭")
    public Map<String, String> closeAccount(@AuthenticationPrincipal User user, @RequestParam("emailCaptcha") String emailCaptcha) throws BusinessException {
        // 关闭账户
        userService.closeAccount(user, emailCaptcha);
        // 签发无效 JWT
        return Map.of("jwt", "");
    }

    @GetMapping("me/reset-password")
    @PreAuthorize("isAuthenticated()")
    public ResultDTO getResetPasswordCaptcha(@AuthenticationPrincipal User user) {
        return userService.getResetPasswordCaptcha(user).toResultDTO(EmailCaptcha.BusinessType.RESET_PASSWORD);
    }

    @PostMapping("me/reset-password")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "密码已修改，请重新登录")
    public Map<String, String> resetPassword(@AuthenticationPrincipal User user, @RequestParam("emailCaptcha") String emailCaptcha, @RequestBody @NotNull ResetPasswordDTO resetPasswordDTO) {
        // 修改密码
        userService.resetPassword(user, emailCaptcha, resetPasswordDTO);
        // 签发无效 JWT，强制重新登陆
        return Map.of("jwt", "");
    }

}
