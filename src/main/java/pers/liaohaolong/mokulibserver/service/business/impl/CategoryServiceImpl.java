package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.CategoryMapper;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Category;
import pers.liaohaolong.mokulibserver.service.business.CategoryService;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

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
        if (!exists(new LambdaQueryWrapper<Category>().eq(Category::getId, id)))
            throw new BusinessException("分类未找到");

        return getById(id);
    }

}
