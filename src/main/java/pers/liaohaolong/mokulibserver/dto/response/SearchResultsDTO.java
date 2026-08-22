package pers.liaohaolong.mokulibserver.dto.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;

import java.util.Map;

/**
 * <h3>通用搜索结果数据传输类型</h3>
 *
 * @param <T> 分页数据类型
 */
@Data
public class SearchResultsDTO<T> {

    private Map<String, String> conditions;

    private IPage<T> results;

    public static <T> SearchResultsDTO<T> of(String keyword, SortModeDTO sortMode, IPage<T> results) {
        SearchResultsDTO<T> response = new SearchResultsDTO<>();
        response.setConditions(Map.of("keyword", keyword, "sortMode", sortMode.getCode()));
        response.setResults(results);
        return response;
    }

}
