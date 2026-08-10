package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.BookMapper;
import pers.liaohaolong.mokulibserver.dao.WishlistMapper;
import pers.liaohaolong.mokulibserver.dto.response.WishlistDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.Wishlist;
import pers.liaohaolong.mokulibserver.service.business.WishlistService;

@Slf4j
@Service
@AllArgsConstructor
public class WishlistServiceImpl extends ServiceImpl<WishlistMapper, Wishlist> implements WishlistService {

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public WishlistDTO add(Integer userId, Integer bookId) throws BusinessException {
        if (!bookMapper.exists(new LambdaQueryWrapper<Book>().eq(Book::getId, bookId)))
            throw new BusinessException("图书不存在");

        Wishlist wishlist = new Wishlist();

        wishlist.setUserId(userId);
        wishlist.setBookId(bookId);

        if (exists(new LambdaQueryWrapper<Wishlist>()
                .eq(Wishlist::getUserId, userId)
                .eq(Wishlist::getBookId, bookId)
        ))
            return WishlistDTO.IS_IN_WISHLIST;

        save(wishlist);

        return WishlistDTO.IS_IN_WISHLIST;
    }

    @Override
    @Transactional
    public WishlistDTO delete(Integer userId, Integer bookId) throws BusinessException {
        if (exists(new LambdaQueryWrapper<Wishlist>()
                .eq(Wishlist::getUserId, userId)
                .eq(Wishlist::getBookId, bookId)
        ))
            return WishlistDTO.NOT_IN_WISHLIST;

        remove(new LambdaQueryWrapper<Wishlist>()
                .eq(Wishlist::getUserId, userId)
                .eq(Wishlist::getBookId, bookId)
        );

        return WishlistDTO.NOT_IN_WISHLIST;
    }

    @Override
    @Transactional(readOnly = true)
    public WishlistDTO isInWishlist(Integer userId, Integer bookId) {
        return WishlistDTO.of(exists(new LambdaQueryWrapper<Wishlist>()
                .eq(Wishlist::getUserId, userId)
                .eq(Wishlist::getBookId, bookId)
        ));
    }

}
