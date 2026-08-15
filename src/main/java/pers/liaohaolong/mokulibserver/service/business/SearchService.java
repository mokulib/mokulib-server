package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.HotSearch;

public interface SearchService extends IService<HotSearch> {

    Page<Book> search(String keyword, SortModeDTO sortMode, Integer pageNum);

}
