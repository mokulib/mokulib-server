package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.CategoryMapper;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Category;
import pers.liaohaolong.mokulibserver.service.business.CategoryService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Category add(String name) {
        // 是否已存在
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, name));
        // 已存在则返回
        if (category != null)
            return category;

        // 不存在则新建
        category = new Category();
        category.setName(name);

        // 插入并返回
        categoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryMapper.selectList(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Category get(Integer id) throws BusinessException {
        if (!categoryMapper.exists(new LambdaQueryWrapper<Category>().eq(Category::getId, id)))
            throw new BusinessException("分类未找到");

        return categoryMapper.selectById(id);
    }

}
