package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.BookTagRelationMapper;
import pers.liaohaolong.mokulibserver.dao.TagMapper;
import pers.liaohaolong.mokulibserver.model.BookTagRelation;
import pers.liaohaolong.mokulibserver.model.Tag;
import pers.liaohaolong.mokulibserver.service.business.TagService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final BookTagRelationMapper bookTagRelationMapper;

    @Override
    @Transactional
    public List<Tag> add(List<String> names) {
        // 预处理，过滤，去重
        List<String> validNames = names.stream().map(String::trim).filter(name -> !name.isBlank()).distinct().toList();

        // 提前返回
        if (validNames.isEmpty())
            return List.of();

        // 查询已有标签
        List<String> existTags = list(new LambdaQueryWrapper<Tag>().in(Tag::getName, validNames)).stream().map(Tag::getName).toList();

        // 过滤掉已有的标签，并构造实体
        List<Tag> tags = validNames.stream().filter(name -> !existTags.contains(name)).map(tag -> {
            Tag newTag = new Tag();
            newTag.setName(tag);
            return newTag;
        }).toList();

        // 批量保存
        saveBatch(tags);

        return tags;
    }

    @Override
    @Transactional
    public List<Tag> delete(List<Integer> ids) {
        // 过滤，去重
        List<Integer> validIds = ids.stream().filter(Objects::nonNull).distinct().toList();

        // 提前返回
        if (validIds.isEmpty())
            return List.of();

        // 查询已存在的标签
        List<Tag> existTags = list(new LambdaQueryWrapper<Tag>().in(Tag::getId, validIds));

        // 过滤掉有关联的标签
        List<Tag> tags = existTags.stream()
                .filter(tag -> !bookTagRelationMapper.exists(new LambdaQueryWrapper<BookTagRelation>().eq(BookTagRelation::getTagId, tag.getId())))
                .toList();

        // 批量删除
        removeBatchByIds(tags.stream().map(Tag::getId).toList());

        return tags;
    }

    @Override
    @Transactional
    public List<Tag> update(List<Tag> tags) {
        // 查询已存在的标签
        List<Integer> existTags = list(new LambdaQueryWrapper<Tag>().in(Tag::getId, tags.stream().map(Tag::getId).toList())).stream().map(Tag::getId).toList();

        // 过滤掉不存在的标签
        tags = tags.stream().filter(tag -> existTags.contains(tag.getId())).toList();

        // 更新标签
        updateBatchById(tags);

        return tags;
    }

}
