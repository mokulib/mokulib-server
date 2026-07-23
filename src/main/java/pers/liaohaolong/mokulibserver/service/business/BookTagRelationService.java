package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Tag;

import java.util.List;

public interface BookTagRelationService {

    void add(Integer bookId, Integer tagId) throws BusinessException;

    void delete(Integer bookId, Integer tagId) throws BusinessException;

    List<Tag> getTags(Integer bookId) throws BusinessException;

}
