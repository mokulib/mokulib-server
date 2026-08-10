package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.BookTagRelation;
import pers.liaohaolong.mokulibserver.model.Tag;

import java.util.List;

public interface BookTagRelationService extends IService<BookTagRelation> {

    void add(Integer bookId, List<Integer> tagIds) throws BusinessException;

    void delete(Integer bookId, Integer tagId) throws BusinessException;

    List<Tag> getTags(Integer bookId) throws BusinessException;

}
