package pers.liaohaolong.mokulibserver.dto.response;

import lombok.Data;

@Data
public class FavoriteDTO {

    public static final FavoriteDTO IS_FAVORITE = FavoriteDTO.of(true);
    public static final FavoriteDTO NOT_FAVORITE = FavoriteDTO.of(false);

    private Boolean isFavorite;

    public static FavoriteDTO of(boolean isFavorite) {
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setIsFavorite(isFavorite);
        return favoriteDTO;
    }

}
