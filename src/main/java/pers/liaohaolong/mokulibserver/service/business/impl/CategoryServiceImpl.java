package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.BookMapper;
import pers.liaohaolong.mokulibserver.dao.CategoryMapper;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.Category;
import pers.liaohaolong.mokulibserver.service.business.CategoryService;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public Category add(String name) {
        // 是否已存在
        Category category = getOne(new LambdaQueryWrapper<Category>().eq(Category::getName, name));
        // 已存在则返回
        if (category != null)
            return category;

        // 不存在则新建
        category = new Category();
        category.setName(name);

        // 插入并返回
        save(category);
        return category;
    }

    @Override
    @Transactional(readOnly = true)
    public Category get(Integer id) throws BusinessException {
        Category category = getById(id);

        if (category == null)
            throw new BusinessException("分类未找到");

        return getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> getBooks(Integer id, Integer pageNum, SortModeDTO sortMode) throws BusinessException {
        if (!exists(new LambdaQueryWrapper<Category>().eq(Category::getId, id)))
            throw new BusinessException("分类未找到");

        return bookMapper.selectPage(new Page<>(pageNum, 12), SortModeDTO.apply(new LambdaQueryWrapper<Book>().eq(Book::getCategoryId, id), sortMode));
    }

}
