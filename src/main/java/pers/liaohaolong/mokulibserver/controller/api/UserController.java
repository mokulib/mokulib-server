package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.dto.response.UsernameDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public User get(@RequestParam(required = false) Integer id, @RequestParam(required = false) String email) throws BusinessException {
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

}
