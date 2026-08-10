package pers.liaohaolong.mokulibserver.dto.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.model.Book;

import java.util.Map;

/**
 * <h3>通用搜索结果数据传输类型</h3>
 *
 * @param <T> 分页数据类型
 */
@Data
public class SearchResultsDTO<T> {

    private Map<String, String> conditions;

    private Page<T> results;

    public static SearchResultsDTO<Book> of(String keyword, SortModeDTO sortMode, Page<Book> results) {
        SearchResultsDTO<Book> response = new SearchResultsDTO<>();
        response.setConditions(Map.of("keyword", keyword, "sortMode", sortMode.getCode()));
        response.setResults(results);
        return response;
    }

}
