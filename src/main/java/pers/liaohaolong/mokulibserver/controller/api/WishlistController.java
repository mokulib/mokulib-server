package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.response.WishlistDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.WishlistService;

@Slf4j
@RestController
@RequestMapping("/api/wishlists")
@AllArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "添加成功")
    public WishlistDTO add(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer bookId) throws BusinessException {
        return wishlistService.add(user.getId(), bookId);
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "删除成功")
    public WishlistDTO delete(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer bookId) throws BusinessException {
        return wishlistService.delete(user.getId(), bookId);
    }

    @GetMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public WishlistDTO isInWishlist(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer bookId) {
        return wishlistService.isInWishlist(user.getId(), bookId);
    }

}
