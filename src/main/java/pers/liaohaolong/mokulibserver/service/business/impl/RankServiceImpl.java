package pers.liaohaolong.mokulibserver.service.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.virtual.RankMapper;
import pers.liaohaolong.mokulibserver.dto.response.RankDTO;
import pers.liaohaolong.mokulibserver.service.business.RankService;

import java.time.LocalDateTime;

@Slf4j
@Service
@CacheConfig(cacheNames = "rank")
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {

    private final RankMapper rankMapper;

    @Override
    @Cacheable(key = "#root.methodName")
    @Transactional(readOnly = true)
    public RankDTO borrow() {
        return new RankDTO(rankMapper.borrow(), LocalDateTime.now());
    }

    @Override
    @Cacheable(key = "#root.methodName")
    @Transactional(readOnly = true)
    public RankDTO favorite() {
        return new RankDTO(rankMapper.favorite(), LocalDateTime.now());
    }

    @Override
    @Cacheable(key = "#root.methodName")
    @Transactional(readOnly = true)
    public RankDTO newMonthly() {
        return new RankDTO(rankMapper.newMonthly(), LocalDateTime.now());
    }

    @Override
    @Cacheable(key = "#root.methodName")
    @Transactional(readOnly = true)
    public RankDTO newStore() {
        return new RankDTO(rankMapper.newStore(), LocalDateTime.now());
    }

}
