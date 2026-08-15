package pers.liaohaolong.mokulibserver.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.dto.response.SearchResultsDTO;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.HotSearch;
import pers.liaohaolong.mokulibserver.service.business.SearchService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/search")
@AllArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping()
    public SearchResultsDTO<Book> search(@RequestParam @NotBlank String keyword, @RequestParam @NotNull SortModeDTO sortMode, @RequestParam @NotNull @Min(1) Integer pageNum) {
        return SearchResultsDTO.of(keyword, sortMode, searchService.search(keyword, sortMode, pageNum));
    }

    @GetMapping("/hot")
    public List<String> hot() {
        return searchService.list(new LambdaQueryWrapper<HotSearch>()
                .orderByDesc(HotSearch::getCount)
                .orderByDesc(HotSearch::getUpdateTime)
                .last("limit 10")
        ).stream().map(HotSearch::getKeyword).toList();
    }

}
