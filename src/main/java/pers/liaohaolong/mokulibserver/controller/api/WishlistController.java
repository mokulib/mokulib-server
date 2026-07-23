package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.WishlistService;

@Slf4j
@RestController
@RequestMapping("/api/wishlist")
@AllArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public void add(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer bookId) {
        wishlistService.add(user.getId(), bookId);
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public void delete(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer bookId) {
        wishlistService.delete(user.getId(), bookId);
    }

    @GetMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public boolean isInWishlist(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer bookId) {
        return wishlistService.isInWishlist(user.getId(), bookId);
    }

}
