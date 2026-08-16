package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.Category;

public interface CategoryService extends IService<Category> {

    Category add(String name);

    Category get(Integer id) throws BusinessException;

    Page<Book> getBooks(Integer id, Integer pageNum, SortModeDTO sortMode) throws BusinessException;

}
