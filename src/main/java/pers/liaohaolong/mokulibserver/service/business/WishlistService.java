package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.response.WishlistDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;

public interface WishlistService {

    WishlistDTO add(Integer userId, Integer bookId) throws BusinessException;

    WishlistDTO delete(Integer userId, Integer bookId) throws BusinessException;

    WishlistDTO isInWishlist(Integer userId, Integer bookId);

}
