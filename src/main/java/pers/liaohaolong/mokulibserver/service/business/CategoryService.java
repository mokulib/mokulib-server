package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Category;

import java.util.List;

public interface CategoryService {

    Category add(String name);

    List<Category> getAll();

    Category get(Integer id) throws BusinessException;

}
