package pers.liaohaolong.mokulibserver.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        return hotSearchService.hotSearch();
    }

}
