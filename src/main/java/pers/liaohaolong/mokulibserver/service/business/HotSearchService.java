package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.model.HotSearch;

import java.util.List;

public interface HotSearchService extends IService<HotSearch> {

    List<String> hotSearch();

}
