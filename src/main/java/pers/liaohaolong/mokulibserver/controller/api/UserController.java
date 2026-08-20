package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.request.UpdateUsernameDTO;
import pers.liaohaolong.mokulibserver.dto.response.*;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.UserService;
import pers.liaohaolong.mokulibserver.util.JwtUtils;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    @PostMapping(value = "/{id}/avatar", consumes = "application/octet-stream")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "上传成功")
    public void uploadAvatar(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer id, @RequestBody byte[] avatar) throws BusinessException {
        if (!user.getId().equals(id))
            throw new BusinessException("不能修改其他用户的头像");
        userService.uploadAvatar(user.getId(), avatar);
    }

    @PostMapping("/username")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "修改成功")
    public JwtDTO updateUsername(@AuthenticationPrincipal User user, @RequestBody UpdateUsernameDTO updateUsernameDTO) throws BusinessException {
        userService.updateUsername(user.getId(), updateUsernameDTO.getUsername());
        return new JwtDTO(jwtUtils.generateToken(userService.getById(user.getId())));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public NonsensitiveUserDTO get(@RequestParam(required = false) Integer id, @RequestParam(required = false) String email) throws BusinessException {
        if (id != null)
            return userService.get(id);
        if (email != null)
            return userService.get(email);
        throw new BusinessException("id 和 email 参数不能同时为空");
    }

    @GetMapping("/usernames")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UsernameDTO> get(@RequestParam @NotNull @NotEmpty List<Integer> ids) throws BusinessException {
        return userService.getUsernames(ids);
    }

    @GetMapping("/borrowing")
    @PreAuthorize("isAuthenticated()")
    public BorrowingDTO getBorrowing(@AuthenticationPrincipal User user) {
        return userService.getBorrowing(user.getId());
    }

    @GetMapping("/favorites")
    @PreAuthorize("isAuthenticated()")
    public List<Book> getFavorites(@AuthenticationPrincipal User user) {
        return userService.getFavorites(user.getId());
    }

    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public List<HistoryDTO> getHistory(@AuthenticationPrincipal User user) {
        return userService.getHistory(user.getId());
    }

}
