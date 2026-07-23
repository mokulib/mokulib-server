package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.dao.BookMapper;
import pers.liaohaolong.mokulibserver.dao.BookTagRelationMapper;
import pers.liaohaolong.mokulibserver.dao.TagMapper;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.BookTagRelation;
import pers.liaohaolong.mokulibserver.model.Tag;
import pers.liaohaolong.mokulibserver.service.business.BookTagRelationService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BookTagRelationServiceImpl implements BookTagRelationService {

    private final BookTagRelationMapper bookTagRelationMapper;

    private final BookMapper bookMapper;

    private final TagMapper tagMapper;

    @Override
    public void add(Integer bookId, Integer tagId) throws BusinessException {
        if (!bookMapper.exists(new LambdaQueryWrapper<Book>().eq(Book::getId, bookId)))
            throw new BusinessException("图书不存在");
        if (!tagMapper.exists(new LambdaQueryWrapper<Tag>().eq(Tag::getId, tagId)))
            throw new BusinessException("标签不存在");

        if (bookTagRelationMapper.exists(new LambdaQueryWrapper<BookTagRelation>()
                .eq(BookTagRelation::getBookId, bookId)
                .eq(BookTagRelation::getTagId, tagId)))
            return;

        BookTagRelation bookTagRelation = new BookTagRelation();

        bookTagRelation.setBookId(bookId);
        bookTagRelation.setTagId(tagId);

        bookTagRelationMapper.insert(bookTagRelation);
    }

    @Override
    public void delete(Integer bookId, Integer tagId) throws BusinessException {
        if (!bookMapper.exists(new LambdaQueryWrapper<Book>().eq(Book::getId, bookId)))
            throw new BusinessException("图书不存在");
        if (!tagMapper.exists(new LambdaQueryWrapper<Tag>().eq(Tag::getId, tagId)))
            throw new BusinessException("标签不存在");

        if (!bookTagRelationMapper.exists(new LambdaQueryWrapper<BookTagRelation>()
                .eq(BookTagRelation::getBookId, bookId)
                .eq(BookTagRelation::getTagId, tagId)))
            return;

        bookTagRelationMapper.delete(new LambdaQueryWrapper<BookTagRelation>()
                .eq(BookTagRelation::getBookId, bookId)
                .eq(BookTagRelation::getTagId, tagId));
    }

    @Override
    public List<Tag> getTags(Integer bookId) throws BusinessException {
        if (!bookMapper.exists(new LambdaQueryWrapper<Book>().eq(Book::getId, bookId)))
            throw new BusinessException("图书不存在");

        return bookTagRelationMapper.selectTags(bookId);
    }

}
