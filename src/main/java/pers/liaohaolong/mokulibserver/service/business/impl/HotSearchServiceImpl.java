package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.dao.HotSearchMapper;
import pers.liaohaolong.mokulibserver.model.HotSearch;
import pers.liaohaolong.mokulibserver.service.business.HotSearchService;

@Slf4j
@Service
@AllArgsConstructor
public class HotSearchServiceImpl extends ServiceImpl<HotSearchMapper, HotSearch> implements HotSearchService {
}
