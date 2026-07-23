package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Category;

public interface CategoryService {

    Category get(Integer id) throws BusinessException;

}
