package pers.liaohaolong.mokulibserver.dto.response;

import lombok.Data;

@Data
public class WishlistDTO {

    public static final WishlistDTO IS_IN_WISHLIST = WishlistDTO.of(true);
    public static final WishlistDTO NOT_IN_WISHLIST = WishlistDTO.of(false);

    private Boolean isInWishlist;

    public static WishlistDTO of(boolean isInWishlist) {
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setIsInWishlist(isInWishlist);
        return wishlistDTO;
    }

}
