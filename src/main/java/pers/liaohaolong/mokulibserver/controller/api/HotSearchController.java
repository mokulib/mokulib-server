package pers.liaohaolong.mokulibserver.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.model.HotSearch;
import pers.liaohaolong.mokulibserver.service.business.HotSearchService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/hot-search")
@AllArgsConstructor
public class HotSearchController {

    private final HotSearchService hotSearchService;

    @GetMapping
    public List<String> hotSearch() {
        return hotSearchService.list(new LambdaQueryWrapper<HotSearch>()
                .orderByDesc(HotSearch::getCount)
                .orderByDesc(HotSearch::getUpdateTime)
                .last("limit 10")
        ).stream().map(HotSearch::getKeyword).toList();
    }

}
