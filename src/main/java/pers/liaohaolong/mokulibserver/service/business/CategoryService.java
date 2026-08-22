package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Category;

public interface CategoryService extends IService<Category> {

    Category add(String name);

    Category get(Integer id) throws BusinessException;

    IPage<Integer> getBooks(Integer id, Integer pageNum, SortModeDTO sortMode) throws BusinessException;

}
