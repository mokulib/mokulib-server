package pers.liaohaolong.mokulibserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import pers.liaohaolong.mokulibserver.model.Favorite;

import java.util.List;

public interface FavoriteMapper extends BaseMapper<Favorite> {

    @Select("SELECT book_id FROM favorite GROUP BY book_id ORDER BY COUNT(*) DESC LIMIT 10")
    List<Integer> getTopFavoriteBookIds();

}
