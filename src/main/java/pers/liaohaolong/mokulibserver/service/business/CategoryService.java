package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    Category add(String name);

    List<Category> getAll();

    Category get(Integer id) throws BusinessException;

}
