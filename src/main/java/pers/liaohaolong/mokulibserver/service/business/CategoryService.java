package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> add(List<String> names);

    List<Category> delete(List<Integer> ids);

    List<Category> update(List<Category> categories);

    Category get(Integer id) throws BusinessException;

    IPage<Integer> getBooks(Integer id, Integer pageNum, SortModeDTO sortMode) throws BusinessException;

}
