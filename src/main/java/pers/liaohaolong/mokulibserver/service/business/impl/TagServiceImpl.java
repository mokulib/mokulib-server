package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.TagMapper;
import pers.liaohaolong.mokulibserver.model.Tag;
import pers.liaohaolong.mokulibserver.service.business.TagService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    @Override
    @Transactional
    public List<Tag> add(List<String> tags) {
        // 去重
        tags = tags.stream().distinct().toList();
        // 返回的数据（不包含已存在的标签）
        List<Tag> resultTags = new ArrayList<>();

        // 去掉已存在的标签 + 预处理（将不存在的 string 转换为 Tag 类型）
        for (String name : tags) {
            Tag tag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name));

            if (tag == null) {
                tag = new Tag();
                tag.setName(name);
                resultTags.add(tag);
            }
        }

        tagMapper.insert(resultTags);

        return resultTags;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> getAll() {
        return tagMapper.selectList(null);
    }

}
