package pers.liaohaolong.mokulibserver.service.business;

import java.util.List;

public interface RankService {

    List<Integer> borrow();

    List<Integer> favorite();

    List<Integer> newMonthly();

    List<Integer> newStore();
}
