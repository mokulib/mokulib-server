package pers.liaohaolong.mokulibserver.service.business.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.dao.virtual.RankMapper;
import pers.liaohaolong.mokulibserver.service.business.RankService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RankServiceImpl implements RankService {

    private final RankMapper rankMapper;

    @Override
    public List<Integer> borrow() {
        return rankMapper.borrow();
    }

}
