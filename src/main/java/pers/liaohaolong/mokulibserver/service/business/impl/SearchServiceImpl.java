package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.BookMapper;
import pers.liaohaolong.mokulibserver.dao.HotSearchMapper;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.HotSearch;
import pers.liaohaolong.mokulibserver.service.business.SearchService;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class SearchServiceImpl extends ServiceImpl<HotSearchMapper, HotSearch> implements SearchService {

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public Page<Book> search(String keyword, SortModeDTO sortMode, Integer pageNum) throws BusinessException {
        // 热搜统计，只统计对默认状态的搜索
        if (sortMode == SortModeDTO.PUBLISH_DATE_FROM_NEW_TO_OLD && pageNum == 1) {
            HotSearch hotSearch = getOne(new LambdaQueryWrapper<HotSearch>().eq(HotSearch::getKeyword, keyword));
            if (hotSearch == null) {
                hotSearch = new HotSearch();
                hotSearch.setKeyword(keyword);
                hotSearch.setCount(1);
            } else {
                hotSearch.setCount(hotSearch.getCount() + 1);
            }
            hotSearch.setUpdateTime(LocalDateTime.now());
            saveOrUpdate(hotSearch);
        }
        // 搜索业务
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Book::getTitle, keyword);
        switch (sortMode) {
            case PUBLISH_DATE_FROM_NEW_TO_OLD -> wrapper.orderByDesc(Book::getPublishDate);
            case PUBLISH_DATE_FROM_OLD_TO_NEW -> wrapper.orderByAsc(Book::getPublishDate);
            case PRICE_FROM_LOW_TO_HIGH -> wrapper.orderByAsc(Book::getPrice);
            case PRICE_FROM_HIGH_TO_LOW -> wrapper.orderByDesc(Book::getPrice);
        }
        return bookMapper.selectPage(new Page<>(pageNum, 5), wrapper);
    }

}
