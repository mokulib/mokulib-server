package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.response.FavoriteDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Favorite;

import java.util.List;

public interface FavoriteService extends IService<Favorite> {

    FavoriteDTO add(Integer userId, Integer bookId) throws BusinessException;

    FavoriteDTO delete(Integer userId, Integer bookId) throws BusinessException;

    FavoriteDTO isFavorite(Integer userId, Integer bookId);

    List<Integer> getTopFavoriteBookIds();

}
