package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.model.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {

    List<Tag> add(List<String> tags);

    List<Tag> getAll();

}
