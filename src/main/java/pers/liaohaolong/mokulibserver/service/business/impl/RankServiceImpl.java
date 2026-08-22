package pers.liaohaolong.mokulibserver.service.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.virtual.RankMapper;
import pers.liaohaolong.mokulibserver.dto.response.RankDTO;
import pers.liaohaolong.mokulibserver.service.business.RankService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {

    private final RankMapper rankMapper;

    private RankDTO borrowRank;
    private RankDTO favoriteRank;
    private RankDTO newMonthlyRank;
    private RankDTO newStoreRank;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void refresh() {
        log.info("开始更新排行榜...");
        borrowRank = new RankDTO(rankMapper.borrow(), LocalDateTime.now());
        favoriteRank = new RankDTO(rankMapper.favorite(), LocalDateTime.now());
        newMonthlyRank = new RankDTO(rankMapper.newMonthly(), LocalDateTime.now());
        newStoreRank = new RankDTO(rankMapper.newStore(), LocalDateTime.now());
        log.info("排行榜更新完成");
    }

    @Override
    @Transactional(readOnly = true)
    public RankDTO borrow() {
        return borrowRank;
    }

    @Override
    @Transactional(readOnly = true)
    public RankDTO favorite() {
        return favoriteRank;
    }

    @Override
    @Transactional(readOnly = true)
    public RankDTO newMonthly() {
        return newMonthlyRank;
    }

    @Override
    @Transactional(readOnly = true)
    public RankDTO newStore() {
        return newStoreRank;
    }

}
