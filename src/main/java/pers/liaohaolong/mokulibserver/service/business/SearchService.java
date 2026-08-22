package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.model.HotSearch;

public interface SearchService extends IService<HotSearch> {

    IPage<Integer> search(String keyword, SortModeDTO sortMode, Integer pageNum);

}
