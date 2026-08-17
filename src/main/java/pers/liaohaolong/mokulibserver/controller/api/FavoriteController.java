package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.response.FavoriteDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.FavoriteService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/favorites")
@AllArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "添加成功")
    public FavoriteDTO add(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer bookId) throws BusinessException {
        return favoriteService.add(user.getId(), bookId);
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "删除成功")
    public FavoriteDTO delete(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer bookId) throws BusinessException {
        return favoriteService.delete(user.getId(), bookId);
    }

    @GetMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public FavoriteDTO isFavorite(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer bookId) {
        return favoriteService.isFavorite(user.getId(), bookId);
    }

    @GetMapping("/hot")
    public List<Integer> getTopFavoriteBookIds() {
        return favoriteService.getTopFavoriteBookIds();
    }

}
