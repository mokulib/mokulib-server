package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.WishlistMapper;
import pers.liaohaolong.mokulibserver.model.Wishlist;
import pers.liaohaolong.mokulibserver.service.business.WishlistService;

@Slf4j
@Service
@AllArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistMapper wishlistMapper;

    @Override
    @Transactional
    public void add(Integer userId, Integer bookId) {
        Wishlist wishlist = new Wishlist();

        wishlist.setUserId(userId);
        wishlist.setBookId(bookId);

        if (wishlistMapper.exists(new LambdaQueryWrapper<Wishlist>()
                .eq(Wishlist::getUserId, userId)
                .eq(Wishlist::getBookId, bookId)
        ))
            return;

        wishlistMapper.insert(wishlist);
    }

    @Override
    @Transactional
    public void delete(Integer userId, Integer bookId) {
        if (wishlistMapper.exists(new LambdaQueryWrapper<Wishlist>()
                .eq(Wishlist::getUserId, userId)
                .eq(Wishlist::getBookId, bookId)
        ))
            return;

        wishlistMapper.delete(new LambdaQueryWrapper<Wishlist>()
                .eq(Wishlist::getUserId, userId)
                .eq(Wishlist::getBookId, bookId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInWishlist(Integer userId, Integer bookId) {
        return wishlistMapper.exists(new LambdaQueryWrapper<Wishlist>()
                .eq(Wishlist::getUserId, userId)
                .eq(Wishlist::getBookId, bookId)
        );
    }

}
