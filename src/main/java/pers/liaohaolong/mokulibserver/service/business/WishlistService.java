package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.response.WishlistDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Wishlist;

public interface WishlistService extends IService<Wishlist> {

    WishlistDTO add(Integer userId, Integer bookId) throws BusinessException;

    WishlistDTO delete(Integer userId, Integer bookId) throws BusinessException;

    WishlistDTO isInWishlist(Integer userId, Integer bookId);

}
