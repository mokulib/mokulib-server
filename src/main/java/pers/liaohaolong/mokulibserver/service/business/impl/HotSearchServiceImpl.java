package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.HotSearchMapper;
import pers.liaohaolong.mokulibserver.model.HotSearch;
import pers.liaohaolong.mokulibserver.service.business.HotSearchService;

import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "hotSearch")
@AllArgsConstructor
public class HotSearchServiceImpl extends ServiceImpl<HotSearchMapper, HotSearch> implements HotSearchService {

    @Override
    @Cacheable(key = "#root.methodName")
    @Transactional(readOnly = true)
    public List<String> hotSearch() {
        return list(new LambdaQueryWrapper<HotSearch>()
                .orderByDesc(HotSearch::getCount)
                .orderByDesc(HotSearch::getUpdateTime)
                .last("limit 10")
        ).stream().map(HotSearch::getKeyword).toList();
    }

}
