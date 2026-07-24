package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.model.Tag;

import java.util.List;

public interface TagService {

    List<Tag> add(List<String> tags);

    List<Tag> getAll();

}
