package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.BookMapper;
import pers.liaohaolong.mokulibserver.dao.FavoriteMapper;
import pers.liaohaolong.mokulibserver.dto.response.FavoriteDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.Favorite;
import pers.liaohaolong.mokulibserver.service.business.FavoriteService;

@Slf4j
@Service
@AllArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public FavoriteDTO add(Integer userId, Integer bookId) throws BusinessException {
        if (!bookMapper.exists(new LambdaQueryWrapper<Book>().eq(Book::getId, bookId)))
            throw new BusinessException("图书不存在");

        Favorite favorite = new Favorite();

        favorite.setUserId(userId);
        favorite.setBookId(bookId);

        if (exists(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getBookId, bookId)
        ))
            return FavoriteDTO.IS_FAVORITE;

        save(favorite);

        return FavoriteDTO.IS_FAVORITE;
    }

    @Override
    @Transactional
    public FavoriteDTO delete(Integer userId, Integer bookId) throws BusinessException {
        if (!exists(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getBookId, bookId)
        ))
            return FavoriteDTO.NOT_FAVORITE;

        remove(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getBookId, bookId)
        );

        return FavoriteDTO.NOT_FAVORITE;
    }

    @Override
    @Transactional(readOnly = true)
    public FavoriteDTO isFavorite(Integer userId, Integer bookId) {
        return FavoriteDTO.of(exists(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getBookId, bookId)
        ));
    }

}
