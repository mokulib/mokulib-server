package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public List<Category> add(List<String> names) {
        // 预处理，过滤，去重
        List<String> validNames = names.stream().map(String::trim).filter(name -> !name.isBlank()).distinct().toList();

        // 提前返回
        if (validNames.isEmpty())
            return List.of();

        // 构造插入实体
        List<Category> categoryList = validNames.stream().map(name -> {
            Category category = new Category();
            category.setName(name);
            return category;
        }).toList();

        // 查询已存在的分类
        List<String> existCategories = list(new LambdaQueryWrapper<Category>().in(Category::getName, validNames)).stream().map(Category::getName).toList();

        // 过滤掉已存在的分类
        List<Category> notExistCategories = categoryList.stream().filter(category -> !existCategories.contains(category.getName())).toList();

        // 批量插入
        saveBatch(notExistCategories);

        return notExistCategories;
    }

    @Override
    @Transactional
    public List<Category> delete(List<Integer> ids) {
        // 过滤，去重
        List<Integer> validIds = ids.stream().filter(Objects::nonNull).filter(id -> id > 0).distinct().toList();

        // 提前返回
        if (validIds.isEmpty())
            return List.of();

        // 查询已存在的分类
        List<Category> existCategories = list(new LambdaQueryWrapper<Category>().in(Category::getId, validIds));

        // 过滤掉有关联的分类
        List<Category> notRelatedCategories = existCategories.stream()
                .filter(category -> bookMapper.selectCount(new LambdaQueryWrapper<Book>().eq(Book::getCategoryId, category.getId())) == 0)
                .toList();

        // 批量删除
        removeBatchByIds(notRelatedCategories.stream().map(Category::getId).toList());

        return notRelatedCategories;
    }

    @Override
    @Transactional
    public List<Category> update(List<Category> categories) {
        // 查询已存在的分类
        List<Integer> existCategories = list(new LambdaQueryWrapper<Category>().in(Category::getId, categories.stream().map(Category::getId).toList())).stream().map(Category::getId).toList();

        // 过滤掉不存在的分类
        categories = categories.stream().filter(category -> existCategories.contains(category.getId())).toList();

        // 更新分类
        updateBatchById(categories);

        return categories;
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
    public IPage<Integer> getBooks(Integer id, Integer pageNum, SortModeDTO sortMode) throws BusinessException {
        if (!exists(new LambdaQueryWrapper<Category>().eq(Category::getId, id)))
            throw new BusinessException("分类未找到");

        return bookMapper
                .selectPage(new Page<>(pageNum, 12), SortModeDTO.apply(new LambdaQueryWrapper<Book>().eq(Book::getCategoryId, id), sortMode))
                .convert(Book::getId);
    }

}
