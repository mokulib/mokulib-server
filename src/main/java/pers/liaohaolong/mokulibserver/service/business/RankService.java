package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.response.RankDTO;

public interface RankService {

    void refresh();

    RankDTO borrow();

    RankDTO favorite();

    RankDTO newMonthly();

    RankDTO newStore();
}
